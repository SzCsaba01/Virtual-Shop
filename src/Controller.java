import java.awt.event.*;
import java.sql.SQLException;
import java.math.BigInteger;
import java.sql.*;
import java.io.*;
import java.lang.Integer;
import java.lang.Double;

public class Controller {
    private View1 v_view;
    private ViewRegistration r_view;
    private ViewAdmin admin_view;
    private ViewUser user_view;
    private String url = "jdbc:mysql://localhost/Magazin";
    private	String uid = "root";
    private	String pw = "1808054795CSsz";
    private Connection con;

    public Controller(View1 view, ViewRegistration view_registration, ViewAdmin viewAdmin, ViewUser viewUser){
        v_view = view;
        r_view = view_registration;
        admin_view = viewAdmin;
        user_view = viewUser;

        v_view.addLoginListener(new LoginListener());
        v_view.addRegistrationListener(new RegistrationListener());
        r_view.addBackListener( new RegisterBackListener());
        r_view.addRegistrationListener(new RegisterListener());

        admin_view.addAddCategoryButtonListener(new AdminAddCategoryListener());
        admin_view.addDeleteCategoryButtonListener(new AdminDeleteCategoryListener());
        admin_view.addProviderAddListener(new AdminAddProviderListener());
        admin_view.addProviderDeleteListener(new AdminDeleteProviderListener());
        admin_view.addDeleteClientListener(new AdminDeleteClientListener());
        admin_view.addBackButtonListener(new AdminBackListener());
        admin_view.addProductUpdateListener(new AdminUpdateProductListener());
        admin_view.addListAllOrdersButtonListener(new AdminAllOrdersClientListener());
        admin_view.addListAllProductGaranteeButtonListener(new AdminSoldProductsGaranteeListener());
        admin_view.addShowTheClientButtonListener(new AdminClienMostProductsListener());
        admin_view.addShowBestSellingButtonListener(new AdminBestSellingProductListener());
        admin_view.addShowTheDateButtonListener(new AdminShowTheDateListener());
        admin_view.addProductAddListener(new AdminAddProductButtonListener());
        admin_view.addProductDeleteListener(new AdminDeleteProductButtonListener());

        user_view.addBackButtonListener(new UserBackListener());
        user_view.addCategoryBoxListener(new UserCategoryBoxListener());
        user_view.addProductgBoxListener(new UserProductBoxListener());
        user_view.addBuyButtonListener(new UserBuyButtonListener());
    }

    public void init(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(java.lang.ClassNotFoundException e){
            System.err.println("ClassNotFoundException: " +e);
        }
        con = null;
        try {
            con = DriverManager.getConnection(url, uid, pw);
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
            System.exit(1);
        }

    }

    class LoginListener extends Controller implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            String firstName = null, lastName = null, password = null, sqlSt, id = null, tip_client = null;
            try{
                Controller.this.init();
                firstName = v_view.getFirstName();
                lastName = v_view.getLastName();
                password = String.valueOf(v_view.getPassword());

                firstName = Controller.this.convertSQLString(firstName);
                lastName = Controller.this.convertSQLString(lastName);
                password = Controller.this.convertSQLString(password);
                sqlSt = "SELECT clients.id, clients.tip_client FROM clients WHERE clients.prenume = '" + firstName + "' " +
                "AND clients.nume = '" + lastName + "' " + "AND clients.parola = '" + password + "';";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if (rst.next()) {
                    id = rst.getString("id");
                    tip_client = rst.getString("tip_client");
                }
                rst.close();
                if(id == null || tip_client == null)
                    v_view.showError("INVALID INPUT");
                else{
                    v_view.showError("Succesfully logged in");
                    if(tip_client.equals("admin")) {
                        v_view.getFrame().setVisible(false);
                        admin_view.getFrame().setVisible(true);
                    }
                    else{
                        v_view.getFrame().setVisible(false);
                        user_view.getFrame().setVisible(true);
                        Controller.this.setCategoryBox();
                        Controller.this.setUserProviderBox();
                    }
                }

            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }


        }
    }

    class RegistrationListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            r_view.getFrame().setVisible(true);
            v_view.getFrame().setVisible(false);
        }
    }

    class RegisterListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String firstName=null, lastName=null, year=null, month=null, day=null, password1=null, password2=null;
            String dateOfBirth=null, sqlSt = null, cardNumber = null;
            try{
                Controller.this.init();
                firstName = r_view.getFirstName();
                lastName = r_view.getLastName();
                password1 = String.valueOf(r_view.getPassword1());
                password2 = String.valueOf(r_view.getPassword2());
                year = String.valueOf(r_view.getYear());
                month = String.valueOf(r_view.getMonth());
                day = String.valueOf((r_view.getDay()));
                cardNumber = r_view.getCardNumber();

                dateOfBirth = year + "-" + month + "-" + day;

                firstName = Controller.this.convertSQLString(firstName);
                lastName = Controller.this.convertSQLString(lastName);
                password1 = Controller.this.convertSQLString(password1);
                password2 = Controller.this.convertSQLString(password2);
                dateOfBirth = Controller.this.convertSQLString(dateOfBirth);
                cardNumber = Controller.this.convertSQLString(cardNumber);

                if(cardNumber.length() != 8)
                    r_view.showError("Invalid card number");
                else{
                    sqlSt = "INSERT INTO card (NumarCard)VALUES ('"+
                            cardNumber + "');";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sqlSt);

                    if(!password1.equals(password2))
                        r_view.showError("password 1 does not match password 2");
                    else{
                        sqlSt = "SELECT prenume, nume FROM clients WHERE prenume = '" +
                                firstName + "' AND nume = '" + lastName + "';";
                        ResultSet rst = stmt.executeQuery(sqlSt);
                        if(rst.next())
                            r_view.showError("User already exists !");
                        else{
                            sqlSt = "INSERT INTO clients (prenume, nume, data_nasterii, parola, tip_client, id_card)" +
                                    "VALUES ('" +firstName + "','" + lastName + "','" + dateOfBirth + "','" +
                                    password1 + "','user', (SELECT MAX(card.id) FROM card))";
                            r_view.showError("Succesfully Registered");
                            v_view.getFrame().setVisible(true);
                            r_view.getFrame().setVisible(false);
                            stmt.executeUpdate(sqlSt);
                        }
                        rst.close();
                    }
                }


            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class RegisterBackListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            r_view.getFrame().setVisible(false);
            v_view.getFrame().setVisible(true);
        }
    }

    class AdminAddCategoryListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){

            String categoryName, categoryDescription, sqlSt;
            categoryDescription = admin_view.getCategoryDescriptionField();
            categoryName = admin_view.getCategoryNameField();

            categoryDescription = Controller.this.convertSQLString(categoryDescription);
            categoryName = Controller.this.convertSQLString(categoryName);

            sqlSt = "SELECT id from categorie WHERE categorie.nume_categorie = '" + categoryName + "';";
            try{
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    admin_view.showError("Category Already Exists !");
                }
                else{
                    sqlSt = "INSERT INTO categorie (nume_categorie, descriere)VALUES ('" +
                            categoryName + "','" + categoryDescription + "');";
                    stmt.executeUpdate(sqlSt);
                    admin_view.showError("Category Succesfully Added !");
                }
                rst.close();
            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }

        }
    }

    class AdminDeleteCategoryListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String categoryName, sqlSt;
            categoryName = admin_view.getCategoryNameField();
            categoryName = Controller.this.convertSQLString(categoryName);

            sqlSt = "SELECT id FROM categorie WHERE nume_categorie = '" + categoryName + "';";

            try{
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    sqlSt = "DELETE FROM categorie WHERE nume_categorie = '" + categoryName+"';";
                    stmt.executeUpdate(sqlSt);
                    admin_view.showError("Category Succesfully Deleted !");
                }
                else
                    admin_view.showError("Category Does Not Exits !");
                rst.close();

            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }

        }
    }

    class AdminAddProviderListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String providerName, sqlSt;
            providerName = admin_view.getAddProviderName();
            providerName = Controller.this.convertSQLString(providerName);

            sqlSt = "SELECT id FROM expeditor WHERE expeditor.Nume = '" + providerName +"';";

            try{
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    admin_view.showError("Provider Already Exist !");
                }
                else {
                    sqlSt = "INSERT INTO expeditor (Nume)VALUES ('" + providerName + "');";
                    stmt.executeUpdate(sqlSt);
                    admin_view.showError("Provider Succesfully Added");
                }
                rst.close();
            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }

        }
    }

    class AdminDeleteProviderListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String providerName, sqlSt;
            providerName = admin_view.getAddProviderName();
            providerName = Controller.this.convertSQLString(providerName);

            sqlSt = "SELECT id FROM expeditor WHERE expeditor.Nume = '" + providerName +"';";

            try{
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    sqlSt = "DELETE FROM expeditor WHERE Nume = '" + providerName + "';";
                    stmt.executeUpdate(sqlSt);
                    admin_view.showError("Provider Succesfully Deleted");
                }
                else{
                    admin_view.showError("Provider Does not exist");
                }
                rst.close();

            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class AdminDeleteClientListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String firstName, lastName, sqlSt, tip_client = null;
            int id_card;

            firstName = admin_view.getDeleteFirstName();
            lastName = admin_view.getDeleteLastName();

            firstName = Controller.this.convertSQLString(firstName);
            lastName = Controller.this.convertSQLString(lastName);

            sqlSt = "SELECT id_card, tip_client FROM clients WHERE prenume = '" + firstName + "' AND nume = '" +
            lastName + "';";

            try{
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    tip_client = rst.getString("tip_client");
                    if(tip_client.equals("user")) {
                        id_card = Integer.parseInt(rst.getString("id_card"));
                        sqlSt = "DELETE FROM clients WHERE prenume = '" + firstName + "' AND nume = '" +
                                lastName + "';";
                        stmt.executeUpdate(sqlSt);
                        sqlSt = "DELETE FROM card WHERE id = " + id_card + ";";
                        stmt.executeUpdate(sqlSt);
                        admin_view.showError("SUCCESFULLY DELETED CLIENT !");
                    }
                    else
                        admin_view.showError("ERROR! You are trying to delete an ADMIN");
                }
                else{
                    admin_view.showError("Client Does not exist");
                }
                rst.close();
            }catch (SQLException ex){
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class AdminBackListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            admin_view.getFrame().setVisible(false);
            v_view.getFrame().setVisible(true);
        }
    }

    class AdminDeleteProductButtonListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String productName, sqlSt;
            productName = admin_view.getAddProductName();
            productName = Controller.this.convertSQLString(productName);
            try{
                sqlSt = "DELETE FROM detaliicomenzi WHERE detaliicomenzi.id_produse = (SELECT id FROM produse WHERE" +
                        " produse.nume = '"+ productName + "');";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sqlSt);
                sqlSt = "DELETE FROM produse WHERE produse.nume = '" + productName + "';";
                stmt.executeUpdate(sqlSt);
                admin_view.showError("Succesfully Deleted The Product");
            }catch (SQLException ex){
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class AdminAddProductButtonListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String productName, productCategory,  productPicture, sqlSt;
            float rating, reduction;
            double price;
            int id_garantee, id_category, stock, productGarantee;

            stock = Integer.parseInt(admin_view.getAddProductStock());
            productName = admin_view.getAddProductName();
            productCategory = admin_view.getAddProductCategory();
            rating = Float.parseFloat(admin_view.getAddProductRating());
            reduction = Float.parseFloat(admin_view.getAddProductReduction());
            price = Double.parseDouble(admin_view.getAddProductPrice());
            productGarantee = Integer.parseInt(admin_view.getAddProductGarantee());
            productPicture = admin_view.getAddProductPicture();

            productPicture = Controller.this.convertSQLString(productPicture);
            productName = Controller.this.convertSQLString(productName);
            productCategory = Controller.this.convertSQLString(productCategory);

            try{
                sqlSt = "SELECT id FROM garantie WHERE timp = " + productGarantee + ";";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    id_garantee = Integer.parseInt(rst.getString("id"));
                    sqlSt = "SELECT id FROM categorie WHERE nume_categorie = '" + productCategory + "';";
                    rst = stmt.executeQuery(sqlSt);
                    if(rst.next()){
                        id_category = Integer.parseInt(rst.getString("id"));
                        sqlSt = "SELECT id FROM produse WHERE nume = '" + productName + "';";
                        rst = stmt.executeQuery(sqlSt);
                        if(!rst.next()){
                            System.out.println(productPicture);
                            sqlSt = "INSERT INTO produse (nume, id_categorie, valoare_unitara, stoc, rating, " +
                                    "reducere, id_garantie, poza)VALUES ('" +productName + "'," +
                                    id_category + "," + price + "," + stock + "," + rating + "," + reduction +
                                    "," + id_garantee + ",'" + productPicture +"');";
                            stmt.executeUpdate(sqlSt);
                            admin_view.showError("Product Succesfully Added!");
                        }
                        else
                            admin_view.showError("Product Already exists!");
                    }
                    else
                        admin_view.showError("Category Does Not Exists!");
                }
                else{
                    admin_view.showError("Garantee Does Not Exists!");
                }
                rst.close();
            }catch (SQLException ex){
                System.err.println("SQLException: " + ex);
            }
        }
    }


    class AdminUpdateProductListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String Name, Category, sqlSt, picture;
            float rating, reduction;
            double price;
            int id_garantee, id_category, garantee, stock;

            stock = Integer.parseInt(admin_view.getAddProductStock());
            Name = admin_view.getAddProductName();
            Category = admin_view.getAddProductCategory();
            rating = Float.parseFloat(admin_view.getAddProductRating());
            reduction = Float.parseFloat(admin_view.getAddProductReduction());
            price = Double.parseDouble(admin_view.getAddProductPrice());
            garantee = Integer.parseInt(admin_view.getAddProductGarantee());
            picture = admin_view.getAddProductPicture();

            picture = Controller.this.convertSQLString(picture);
            Name = Controller.this.convertSQLString(Name);
            Category = Controller.this.convertSQLString(Category);


            try{
                sqlSt = "SELECT id FROM garantie WHERE timp = " + garantee + ";";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    id_garantee = Integer.parseInt(rst.getString("id"));
                    sqlSt = "SELECT id FROM categorie WHERE nume_categorie = '" + Category + "';";
                    rst = stmt.executeQuery(sqlSt);
                    if(rst.next()){
                        id_category = Integer.parseInt(rst.getString("id"));
                        sqlSt = "SELECT id FROM produse WHERE nume = '" + Name + "';";
                        rst = stmt.executeQuery(sqlSt);
                        if(rst.next()){
                            sqlSt = "UPDATE produse SET id_categorie = " + id_category + ", valoare_unitara = " +
                                    price + ", stoc = " + stock + ", rating = " + rating +
                                    ", reducere = " +reduction + ", id_garantie = " + id_garantee + ", poza = '" + picture +
                                    "' WHERE nume = '" + Name + "';";
                            stmt.executeUpdate(sqlSt);
                            admin_view.showError("Product Succesfully Updated!");
                        }
                        else
                            admin_view.showError("Product Does Not Exists!");
                    }
                    else
                        admin_view.showError("Category Does Not Exists!");
                }
                else{
                    admin_view.showError("Garantee Does Not Exists!");
                }
                rst.close();
            }catch (SQLException ex){
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class AdminAllOrdersClientListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String sqlSt, text = "";
            int id;

            id = Integer.parseInt(admin_view.getAllOrdersField());

            try{
                sqlSt = "SELECT id FROM clients WHERE id = "+id + ";";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){

                    sqlSt = "SELECT clients.nume, clients.prenume, produse.nume AS Produs, cantitate, pret_total" +
                            " FROM clients " +
                            "INNER JOIN comenzi ON clients.id = comenzi.id_client " +
                            "INNER JOIN detaliicomenzi ON detaliicomenzi.id = comenzi.id " +
                            "INNER JOIN produse ON detaliicomenzi.id_produse = produse.id" +
                            " WHERE clients.id = '"+id+"';";
                    text = Controller.this.doQuery(sqlSt);
                    admin_view.setAllOrders(text);
                }
                else{
                    admin_view.showError("Invaild Id!");
                }
                rst.close();
            }catch (SQLException ex){
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class AdminSoldProductsGaranteeListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String text = "", sqlSt;
            sqlSt = "SELECT produse.nume as Produs, detaliicomenzi.garantie as DataExpirarii" +
                    " FROM produse, detaliicomenzi" +
                    " WHERE produse.id = detaliicomenzi.id_produse" +
                    " AND detaliicomenzi.garantie > CURDATE();";
            text = Controller.this.doQuery(sqlSt);
            admin_view.setAllProductsGarantee(text);

        }
    }

    class AdminClienMostProductsListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = "", sqlSt;
            sqlSt = "SELECT clients.nume, clients.prenume, SUM(cantitate) as Cantitate, SUM(pret_total) as Pret_Total" +
                    " FROM clients, comenzi, detaliicomenzi" +
                    " WHERE comenzi.id_client = clients.id" +
                    " AND detaliicomenzi.id_comanda = comenzi.id" +
                    " GROUP BY clients.nume, clients.prenume" +
                    " ORDER BY sum(cantitate) DESC" +
                    " LIMIT 1;";
            text = Controller.this.doQuery(sqlSt);
            admin_view.setClientMostProducts(text);

        }
    }

    class AdminBestSellingProductListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String text = "", sqlSt;
            int rowCount = 0;
            sqlSt = "SELECT produse.nume as Produs, SUM(detaliicomenzi.cantitate) as Cantitate" +
                    " FROM detaliicomenzi, produse" +
                    " WHERE id_produse = produse.id" +
                    " GROUP BY produse.nume" +
                    " ORDER BY SUM(detaliicomenzi.cantitate) DESC" +
                    " LIMIT 1;";
            text = Controller.this.doQuery(sqlSt);
            admin_view.setBestSellingProduct(text);
        }
    }

    class AdminShowTheDateListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = "", sqlSt;
            int rowCount = 0;
            sqlSt = "SELECT data_comanda, SUM(cantitate) as Cantitate" +
                    " FROM detaliicomenzi" +
                    " GROUP BY data_comanda" +
                    " ORDER BY SUM(cantitate) DESC" +
                    " LIMIT 1;";
            text = Controller.this.doQuery(sqlSt);
            admin_view.setMostSalesDate(text);
        }
    }

    class UserBackListener extends Controller implements ActionListener{
        public void actionPerformed(ActionEvent e){
            user_view.getFrame().setVisible(false);
            v_view.getFrame().setVisible(true);
        }
    }

    public void setUserProviderBox(){
        if(user_view.getProviderBox() != null)
            user_view.deleteProviderBoxItems();
        String sqlSt;
        try{
            sqlSt = "SELECT nume FROM expeditor;";
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(sqlSt);
            while (rst.next()) {
                user_view.setProviderBox((rst.getString("nume")));
            }
            rst.close();
        }catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }
    }

    public void setCategoryBox() {
        if(user_view.getCategoryBox() != null)
            user_view.deleteCategoryBoxItems();
        String sqlSt;
        try {
            sqlSt = "SELECT nume_categorie FROM categorie;";
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(sqlSt);
            while (rst.next()) {
                user_view.setCategoryBox(rst.getString("nume_categorie"));
            }
            rst.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }
    }

    class UserCategoryBoxListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            user_view.deleteProductBoxItems();
            String categoryName, sqlSt;
            categoryName = String.valueOf(user_view.getCategoryBox());
            categoryName = Controller.this.convertSQLString(categoryName);

            try {
                sqlSt = "SELECT produse.nume FROM categorie, produse " +
                        "WHERE produse.id_categorie = categorie.id AND nume_categorie = '" + categoryName + "';";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                while(rst.next()){
                    user_view.setProductBox(rst.getString("nume"));
                }
                rst.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class UserProductBoxListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String productName, sqlSt, productDescription, productPicture;

            productName = String.valueOf(user_view.getProductBox());
            productName = Controller.this.convertSQLString(productName);

            try{
                sqlSt = "SELECT produse.poza, produse.reducere," +
                        " descriere FROM produse, categorie WHERE id_categorie = categorie.id" +
                        " AND produse.nume = '" + productName + "';";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    user_view.setProductDescriptionField(rst.getString("descriere"));
                    user_view.setProductPicture(rst.getString("poza"));
                    user_view.setProductNameLabel(productName);
                    if(Float.parseFloat(rst.getString("reducere")) > 1){
                        user_view.setReducereLabel("Reducere de " + rst.getString("reducere") + "%");
                    }
                   else
                       user_view.setReducereLabel("");

                }
                rst.close();
            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }
        }
    }

    class UserBuyButtonListener extends Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String numeClient, prenumeClient, productCategory, productName, providerName, errorMessage = null, sqlSt;
            int amount;

            numeClient = v_view.getLastName();
            prenumeClient = v_view.getFirstName();
            productCategory = String.valueOf(user_view.getCategoryBox());
            productName = String.valueOf(user_view.getProductBox());
            amount = Integer.parseInt(String.valueOf(user_view.getProductAmountBox()));
            providerName = String.valueOf(user_view.getProviderBox());


            try{
                sqlSt = "SELECT stoc FROM produse WHERE produse.nume = '" + productName + "';";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlSt);
                if(rst.next()){
                    if(Integer.parseInt(rst.getString("stoc")) >= amount){
                        sqlSt = "{CALL cumparare(?, ?, ?, ?, ?, ?, ?)}";
                        CallableStatement cs = con.prepareCall("{CALL cumparare(?, ?, ?, ?, ?, ?, ?)}");
                        cs.setString(1, numeClient);
                        cs.setString(2, prenumeClient);
                        cs.setString(3, productCategory);
                        cs.setString(4, productName);
                        cs.setString(5, providerName);
                        cs.setInt(6, amount);
                        cs.setString(7, errorMessage);
                        cs.execute();
                        cs.close();
                        user_view.showError("Succesfully Buyed A Product!");
                    }
                    else
                        user_view.showError("Out of stock!");
                }

            }catch (SQLException ex) {
                System.err.println("SQLException: " + ex);
            }
        }
    }

    public String doQuery(String sqlSt){
        String text = "";
        try {
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(sqlSt);

            ResultSetMetaData rsmd = rst.getMetaData();
            int colCount = rsmd.getColumnCount();
            int[] colWidth = new int[colCount];
            for (int i=1; i <= colCount; i++)
            {
                colWidth[i-1] = rsmd.getColumnDisplaySize(i);
                if (colWidth[i-1] > 35)
                    colWidth[i-1] = 35;
            }

            for (int i=1; i <= colCount; i++)
            {	String colName = rsmd.getColumnName(i);
                text += colName+spaces(colWidth[i-1]-colName.length())+' ';
            }
            text +="\n-----------------------------------------------------------------------"+
                    "-------------------------------------\n";

            while (rst.next())
            {
                for (int i=1; i <= colCount; i++)
                {	Object obj = rst.getObject(i);
                    if (obj == null)
                        text = text+spaces(colWidth[i-1]);
                    else
                    {	String data = obj.toString();
                        text = text + data+spaces(colWidth[i-1]-data.length())+' ';
                    }
                }
                text = text + "\n";
            }
            rst.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }
        return text;
    }


    private String convertSQLString(String st)
    {	// Main function is to replace "'" in string with "''"
        return st.replaceAll("'","''");
    }

    private static String spaces(int space)
    {
        String sp = "";
        for(int i=0;i<space;i++)
            sp = sp+" ";
        return sp;
    }

    public Controller(){}

}
