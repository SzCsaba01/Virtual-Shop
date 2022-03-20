Drop database if  exists Magazin;
Create database if not exists Magazin;
use Magazin;

Create table if not exists Card(
id int unique auto_increment primary key,
NumarCard varchar(8),
CHECK(LENGTH(NumarCard) = 8)
);

Create table if not exists Expeditor(
id int unique auto_increment primary key,
Nume varchar(30)
);

Create table if not exists Clients(
id int auto_increment unique primary key,
nume varchar(30),
prenume varchar(30),
data_nasterii date,
parola varchar(30),
tip_client varchar(6),
id_card int unique,
index(id_card),
foreign key (id_card) REFERENCES Card(id)
);

Create table if not exists Comenzi(
id int auto_increment unique primary key,
id_client int,
id_expeditor int,
index (id_client),
index (id_expeditor),
foreign key (id_client) references Clients(id),
foreign key (id_expeditor) references Expeditor(id)
);

Create table if not exists Garantie(
id int unique auto_increment primary key,
timp int,
CHECK(timp <= 5)
);

Create table if not exists Categorie(
id int auto_increment unique primary key,
nume_categorie varchar(30),
descriere varchar(80)
);

Create table if not exists Produse(
id int auto_increment unique primary key,
nume varchar(30),
id_categorie int,
valoare_unitara double,
stoc int,
rating float,
reducere float,
id_garantie int,
poza varchar(100),
index (id_categorie),
index (id_garantie),
CHECK(stoc <= 200),
foreign key (id_categorie) references Categorie(id),
foreign key (id_garantie) references Garantie(id)
);


Create table if not exists DetaliiComenzi(
id int auto_increment unique primary key,
id_comanda int unique,
id_produse int,
pret_total double,
cantitate int,
garantie DATE,
categorie_produs varchar(30),
descriere_produs varchar(80),
data_comanda DATE,
index(id_comanda),
index(id_produse),
foreign key (id_comanda) references Comenzi(id),
foreign key (id_produse) references Produse(id)
);

INSERT INTO categorie(nume_categorie, descriere) VALUES
('Laptop', 'Ocupa foarte puțin spațiu și pot fi folosite ore întregi fără acces la putere.'),
('Electrocasnice','Aparate care se folosesc pentru ușurarea muncilor casnice' ),
('TV','Un televizor Smart este dotat cu o placa de retea, cu un procesor, cu hard disk');

INSERT INTO garantie(timp) VALUES
(3),
(2),
(4),
(5);	

INSERT INTO produse(nume, stoc, valoare_unitara, id_garantie, id_categorie, rating, reducere, poza)VALUES
("Fujitsu Siemens Amilo Pro", 10, 2000, 1, 1, 8.5, 51.5, "D:\\facultate\\BD\\Proiect\\FujitsuSiemensAmiloPro.jpg"),
("Indesit WLI1000", 5, 900, 2, 2, 7, 10, "D:\\facultate\\BD\\Proiect\\IndesitWLI1000.jpg"),
("Gorenje RC400", 4, 15000, 3, 2, 5, 20,"D:\\facultate\\BD\\Proiect\\GorenjeRC400.jpg");

INSERT INTO card(NumarCard) VALUES
("11111111"),
("22222222"),
("33333333"),
("00000000");

INSERT INTO clients(nume, prenume, data_nasterii, id_card, parola, tip_client)VALUES
("Popescu", "Ion", "1985-1-01", 1, '123', 'user'),
("Georgescu", "Andreea", "1983-8-23", 2, '123', 'user'),
("Ionescu", "Robert", "1982-3-08", 3, '123', 'user'),
("Szikszai", "Csaba", "2001-3-20", 4, '123', 'admin');

INSERT INTO expeditor(nume)VALUES
('FAN Courier');

CALL cumparare('Georgescu', 'Andreea', 'Laptop', 'Fujitsu Siemens Amilo Pro', 'FAN Courier', 2, NULL);

CALL cumparare('Popescu', 'Ion', 'Laptop', 'Fujitsu Siemens Amilo Pro', 'FAN Courier', 2, NULL);

CALL cumparare('Popescu', 'Ion',  'Electrocasnice', 'Indesit WLI1000', 'FAN Courier', 5, NULL);

INSERT INTO comenzi(id_client, id_expeditor)VALUES
(3, 1);

INSERT INTO detaliicomenzi(id_comanda, id_produse, pret_total, cantitate, garantie, categorie_produs, descriere_produs, data_comanda)VALUES
(4, 1, 2000, 2, '2021-12-14', 'Laptop', 'Ocupa foarte puțin spațiu și pot fi folosite ore întregi fără acces la putere.', '2019-12-14');

