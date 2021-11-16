import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame implements ActionListener {
    JLabel nameJLabel, licenceJLabel, jl1, jl2, jl3;
    JTextField nameJTextField;
    JPasswordField licenceField, jpf;
    JButton jb1, jb2, jb3;
    JPanel heroJPane, loginJPane, jp1, jp2, jp3, jp4;
    String name;
    static boolean isLogin = false;
    private static LoginUI instance;

    public static LoginUI getInstance() {
        if (instance == null)
            instance = new LoginUI();

        return instance;
    }
    public LoginUI() {
        Container container = this.getContentPane();

        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        JTextField titleField1 = new JTextField();
        titleField1.setFont(font1);
        titleField1.setEditable(false);
        titleField1.setText("KidPaint");


        Font font2 = new Font("Georgia", Font.PLAIN, 15);
        JTextArea descriptionArea2 = new JTextArea();
        descriptionArea2.setFont(font2);
        descriptionArea2.setEditable(false);
        descriptionArea2.setLineWrap(true);
        descriptionArea2.setWrapStyleWord(true);
        descriptionArea2.setText("This application allows  users to use the shared drawing board to draw at the " +
                "same time and synchronize it to other users in the same group. After entering your name, click " +
                "the login button to select an existing group or create a new group, and then start enjoying the " +
                "fun of drawing!");

        heroJPane = new JPanel();
        heroJPane.setLayout(new GridLayout(2,0));
        heroJPane.add(titleField1);
        heroJPane.add(descriptionArea2);
        container.add(heroJPane, BorderLayout.NORTH);

        nameJLabel = new JLabel("Enter your name：");
//        nameJLabel.setSize(100, 50);
//        nameJLabel.setLocation(50, 30);
        nameJLabel.setBounds(100, 50, 50, 30);
        licenceJLabel = new JLabel("Enter your licence number(default: 123456)");
        licenceJLabel.setBounds(110, 60, 50, 30);
        nameJTextField = new JTextField();
        licenceField = new JPasswordField();

        loginJPane = new JPanel();
        loginJPane.setLayout(new GridLayout(2, 2));
        loginJPane.add(nameJLabel);
        loginJPane.add(nameJTextField);
        loginJPane.add(licenceJLabel);
        loginJPane.add(licenceField);
        container.add(loginJPane, BorderLayout.CENTER);


//        jp2 = new JPanel();
//        jp2.add(jl2);
//        jpf = new JPasswordField(16);
//        jp2.add(jpf);
//
//        jb1 = new JButton("Submit");
//
//
//        this.add(jp1);
//        this.add(jp2);
//        this.add(jb1);
//
        //jb1.addActionListener(this);
        this.setSize(800, 600);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jb1) {
            String student = "123456";

            String name = nameJTextField.getText();
            String p1 = String.copyValueOf(jpf.getPassword());

            if (p1.equals(student)) {
                this.name = name;
                this.isLogin = true;
            }
        }
    }

    public String getName() {
        return name;
    }

}

class loginTest extends JPanel {

}


