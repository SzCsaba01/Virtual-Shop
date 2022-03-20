import javax.swing.*;
import java.awt.event.ActionListener;

public class ViewRegistration extends JFrame{
    private JButton registrationButton;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JComboBox yearBox;
    private JComboBox monthBox;
    private JComboBox dayBox;
    private JPanel MainPanel;
    private JTextField cardNumberField;
    private JButton backButton;
    public JFrame frame;

    public ViewRegistration(){
        frame = new JFrame("Registration");
        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);
    }

    void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }

    public void addRegistrationListener(ActionListener e){registrationButton.addActionListener(e);}
    public void addBackListener(ActionListener e){backButton.addActionListener(e);}

    public String getFirstName(){return firstNameField.getText();}
    public String getLastName(){return lastNameField.getText();}
    public String getCardNumber(){return cardNumberField.getText();}
    public char[] getPassword1(){return passwordField1.getPassword();}
    public char[] getPassword2(){return passwordField2.getPassword();}
    public Object getYear(){return yearBox.getSelectedItem();}
    public Object getMonth(){return monthBox.getSelectedItem();}
    public Object getDay(){return dayBox.getSelectedItem();}


    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}
