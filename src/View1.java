import javax.swing.*;
import java.awt.event.ActionListener;

public class View1 extends JFrame {
    private JButton loginButton;
    private JButton registrationButton;
    private JTextField lastNameField;
    private JPasswordField passwordField1;
    private JTextField firstNameField;
    private JPanel MainPanel;
    private JFrame frame;

    public View1() {
        frame = new JFrame("Magazin");
        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }

    public void addLoginListener(ActionListener Login){loginButton.addActionListener(Login);}

    public void addRegistrationListener(ActionListener Registration){registrationButton.addActionListener(Registration);}

    public String getFirstName(){return firstNameField.getText();}

    public String getLastName(){return lastNameField.getText();}

    public char[] getPassword(){return passwordField1.getPassword();}

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}
