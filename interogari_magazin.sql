USE magazin;
DROP PROCEDURE IF EXISTS cumparare;
DROP TRIGGER IF EXISTS decrementare_stoc;

delimiter //
CREATE TRIGGER decrementare_stoc
AFTER INSERT ON detaliicomenzi
FOR EACH ROW
BEGIN
	UPDATE produse
	SET stoc = stoc - new.cantitate
    WHERE produse.id = (SELECT id_produse FROM detaliicomenzi WHERE new.id = detaliicomenzi.id);
END; //

delimiter ;

delimiter //
CREATE PROCEDURE cumparare
(nume_client VARCHAR(30), prenume_client VARCHAR(30), categorie_produs VARCHAR(30), nume_produse VARCHAR(30),
nume_expeditor VARCHAR(30), ccantitate int, error_message VARCHAR(30))
BEGIN
	SET @id_expeditor = NULL, @id_cclient = NULL, @id_ccategorie = NULL, @rreducere = NULL,
    @id_pprodus = NULL, @ppret = NULL, @ggarantie = NULL, @descriere = NULL, @ccategorie = NULL;
    
    SET @id_expeditor := (SELECT id FROM expeditor WHERE expeditor.Nume = nume_expeditor);
    
    SET @id_cclient := (SELECT id FROM clients 
    WHERE nume_client = nume
    AND prenume_client = prenume);
    
    SET @id_ccategorie := (SELECT id FROM categorie WHERE categorie_produs = nume_categorie);
    
	SET @id_pprodus := (SELECT id FROM produse 
    WHERE nume_produse = nume
    AND id_categorie = @id_ccategorie);
    
    IF @id_cclient = NULL THEN
		SET error_message = 'nu exista clientul';
	ELSEIF @id_pprodus = NULL THEN
		SET error_message = 'nu exista produsul';
	ELSEIF @id_ccategorie = NULL THEN
		SET error_message = 'nu exista categoria';
	ELSEIF (SELECT stoc FROM produse WHERE @id_pprodus = produse.id) < ccantitate THEN
		SET error_message = 'stoc epuizat';
    END IF;
    
		IF( error_message IS NULL) THEN
	BEGIN
		SET @ppret := (SELECT valoare_unitara FROM produse WHERE @id_pprodus = id);
        
        SET @rreducere := (SELECT reducere FROM produse WHERE @id_pprodus = id);
        
		SET @ppret = (@ppret - ( (@rreducere/100)*@ppret ) )*ccantitate;
        
        SET @ggarantie := (SELECT timp FROM garantie, produse WHERE @id_pprodus = produse.id AND produse.id_garantie = garantie.id);
        SET @ggarantie := DATE_ADD(CURDATE(), INTERVAL @ggarantie YEAR);
        
        SET @ccategorie := (SELECT nume_categorie FROM categorie WHERE id = @id_ccategorie);
    
        SET @ddescriere := (SELECT descriere FROM categorie WHERE id = @id_ccategorie);
        
		INSERT INTO comenzi(id_client, id_expeditor) VALUES
        (@id_cclient, @id_expeditor);
        
        INSERT INTO detaliicomenzi(id_comanda, id_produse, pret_total, cantitate, garantie, categorie_produs, descriere_produs, data_comanda)VALUES
        ((SELECT MAX(id) FROM comenzi), @id_pprodus, @ppret, ccantitate, @ggarantie, @ccategorie, @ddescriere, (SELECT CURDATE()));
                
        COMMIT;
        END;
	ELSE ROLLBACK;
		END IF;
    
END; //
delimiter ;

#f) Să se genereze un raport care sa afiseze toate cumparaturile facute de o persoana. Raportul 
#va afişa următoarele coloane: 
#Nume 
#Prenume 
#Produs 
#Cantitate 
#ValoareTotala 

SELECT clients.nume, clients.prenume, produse.nume AS Produs, cantitate, pret_total
FROM clients
INNER JOIN comenzi ON clients.id = comenzi.id_client
INNER JOIN detaliicomenzi ON detaliicomenzi.id = comenzi.id 
INNER JOIN produse ON detaliicomenzi.id_produse = produse.id;

#g) Să se genereze un raport care să afişeze toate produsele vandute pentru care nu a expirat inca 
#garantia. Raportul va contine urmatoarele coloane:
#Produs 
#DataExpirării 

SELECT produse.nume as Produs, detaliicomenzi.garantie as DataExpirarii
FROM produse, detaliicomenzi
WHERE produse.id = detaliicomenzi.id_produse
AND detaliicomenzi.garantie > CURDATE();

#h) Să se afişeze cel mai vandut produs; 
SELECT produse.nume as Produs, SUM(detaliicomenzi.cantitate) as Cantitate
FROM detaliicomenzi, produse
WHERE id_produse = produse.id
GROUP BY produse.nume
ORDER BY SUM(detaliicomenzi.cantitate) DESC
LIMIT 1;

#i) Să se afişeze data în care au avut loc cele mai multe vanzări;
SELECT data_comanda, SUM(cantitate)
FROM detaliicomenzi
GROUP BY data_comanda
ORDER BY SUM(cantitate) DESC
LIMIT 1;

#j) Să se afişeze clientul (nume,prenume) care a cumpărat cele mai multe produse (şi valoarea 
#totală a cumparaturilor). 

SELECT clients.nume, clients.prenume, SUM(cantitate) as Cantitate, SUM(pret_total) as Pret_Total
FROM clients, comenzi, detaliicomenzi
WHERE comenzi.id_client = clients.id
AND detaliicomenzi.id_comanda = comenzi.id
GROUP BY clients.nume, clients.prenume
ORDER BY sum(cantitate) DESC
LIMIT 1;
