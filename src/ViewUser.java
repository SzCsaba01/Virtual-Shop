import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ViewUser extends JFrame{
    private JPanel MainPanel;
    private JButton buyButton;
    private JLabel Picture;
    private JComboBox productBox;
    private JComboBox categoryBox;
    private JTextArea productDescriptionField;
    private JLabel productName;
    private JButton backButton;
    private JComboBox productProviderBox;
    private JComboBox productAmountBox;
    private JLabel reducereLabel;
    private JFrame frame;

    public ViewUser(){
        frame = new JFrame("User");
        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);
        productDescriptionField.setEditable(false);
    }

    public void setReducereLabel(String txt){reducereLabel.setText(txt);}

    public Object getProviderBox(){return productProviderBox.getSelectedItem();}
    public void deleteProviderBoxItems(){productProviderBox.removeAllItems();}
    public void setProviderBox(String a){ productProviderBox.addItem(a);}

    public Object getProductAmountBox(){return productAmountBox.getSelectedItem();}

    public void addBuyButtonListener(ActionListener e){buyButton.addActionListener(e);}

    public void addBackButtonListener(ActionListener e){backButton.addActionListener(e);}

    public void setProductNameLabel(String a){productName.setText(a);}
    public String getProductNameLabel(){return productName.getText();}

    public void setProductDescriptionField(String a){productDescriptionField.setText(a);}
    public String getProductDescriptionField(){return productDescriptionField.getText();}

    public void setProductPicture(String txt){
        Picture.setIcon(new ImageIcon(new ImageIcon(txt).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT)));

    }

    public Object getCategoryBox(){return categoryBox.getSelectedItem();}
    public void deleteCategoryBoxItems(){categoryBox.removeAllItems();}
    public void setCategoryBox(String a){ categoryBox.addItem(a);}
    public void addCategoryBoxListener(ActionListener e){categoryBox.addActionListener(e);}

    public Object getProductBox(){return productBox.getSelectedItem();}
    public void deleteProductBoxItems(){productBox.removeAllItems();}
    public void setProductBox(String a){productBox.addItem(a);}
    public void addProductgBoxListener(ActionListener e){productBox.addActionListener(e);}

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
