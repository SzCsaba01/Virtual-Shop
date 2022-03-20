import javax.swing.*;
import java.awt.event.ActionListener;

public class ViewAdmin extends JFrame{
    private JPanel MainPanel;
    private JTextField addProductName;
    private JTextField addProductCategory;
    private JTextField addProductStock;
    private JTextField addProductGarantee;
    private JTextField addProductRating;
    private JTextField addProductReduction;
    private JTextField addProviderName;
    private JButton addProviderButton;
    private JButton deleteProviderButton;
    private JButton addProductButton;
    private JButton deleteProductButton;
    private JButton updateProductButton;
    private JTextField deleteFirstName;
    private JTextField deleteLastName;
    private JButton listAllOrdersForButton;
    private JButton listAllProductsSoldButton;
    private JButton showTheBestSellingButton;
    private JButton showTheDateWhenButton;
    private JButton showTheClientWhoButton;
    private JTextArea clientMostProducts;
    private JTextArea bestSellingProduct;
    private JTextArea mostSalesDate;
    private JTextArea allProductsGarantee;
    private JTextField categoryNameField;
    private JTextField categoryDescriptionField;
    private JButton addCategoryButton;
    private JButton deleteCategoryButton;
    private JButton deleteClientButton;
    private JButton backButton;
    private JTextField addProductPriceField;
    private JTextArea ordersClientField;
    private JTextField productPictureField;
    private JFrame frame;

    public ViewAdmin(){
        frame = new JFrame("Admin");
        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);
    }

    public void addBackButtonListener(ActionListener e){backButton.addActionListener(e);}

    public String getAddProductPicture(){return productPictureField.getText();}
    public String getAddProductPrice(){return addProductPriceField.getText();}
    public String getAddProductName(){return addProductName.getText();}
    public String getAddProductStock(){return addProductStock.getText();}
    public String getAddProductCategory(){return addProductCategory.getText();}
    public String getAddProductGarantee(){return addProductGarantee.getText();}
    public String getAddProductRating(){return addProductRating.getText();}
    public String getAddProductReduction(){return addProductReduction.getText();}
    public void addProductAddListener(ActionListener e){addProductButton.addActionListener(e);}
    public void addProductDeleteListener(ActionListener e){deleteProductButton.addActionListener(e);}
    public void addProductUpdateListener(ActionListener e){updateProductButton.addActionListener(e);}

    public String getAddProviderName(){return addProviderName.getText();}
    public void addProviderAddListener(ActionListener e){addProviderButton.addActionListener(e);}
    public void addProviderDeleteListener(ActionListener e){deleteProviderButton.addActionListener(e);}

    public String getDeleteFirstName(){return deleteFirstName.getText();}
    public String getDeleteLastName(){return deleteLastName.getText();}
    public void addDeleteClientListener(ActionListener e){deleteClientButton.addActionListener(e);}

    public void setClientMostProducts(String a){clientMostProducts.setText(a);}
    public void addShowTheClientButtonListener(ActionListener e){showTheClientWhoButton.addActionListener(e);}

    public void setBestSellingProduct(String a){bestSellingProduct.setText(a);}
    public void addShowBestSellingButtonListener(ActionListener e){showTheBestSellingButton.addActionListener(e);}

    public void setMostSalesDate(String a){mostSalesDate.setText(a);}
    public void addShowTheDateButtonListener(ActionListener e){showTheDateWhenButton.addActionListener(e);}

    public void setAllProductsGarantee(String a){allProductsGarantee.setText(a);}
    public void addListAllProductGaranteeButtonListener(ActionListener e){listAllProductsSoldButton.addActionListener(e);}

    public String getAllOrdersField(){return ordersClientField.getText();}
    public void setAllOrders(String a){ordersClientField.setText(a);}
    public void addListAllOrdersButtonListener(ActionListener e){listAllOrdersForButton.addActionListener(e);}

    public String getCategoryNameField(){return categoryNameField.getText();}
    public String getCategoryDescriptionField(){return categoryDescriptionField.getText();}
    public void addAddCategoryButtonListener(ActionListener e){addCategoryButton.addActionListener(e);}
    public void addDeleteCategoryButtonListener(ActionListener e){deleteCategoryButton.addActionListener(e);}

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
}
