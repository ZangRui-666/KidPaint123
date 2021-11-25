/*
    This class is showing a Login UI page for users to input their username and licences.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class LoginUI extends JFrame implements ActionListener {
    JLabel nameJLabel, licenceJLabel;
    JTextField nameJTextField;
    JPasswordField licenceField;
    JButton submitJButton;
    JPanel heroJPane, loginJPane, submitJPane;
    static boolean isLogin = false;
    private static LoginUI instance;

    /**
     * get the instance of LoginUI. Singleton design pattern.
     *
     * @return initialized instance of LoinUI
     */
    public static LoginUI getInstance() {
        if (instance == null)
            instance = new LoginUI();
        return instance;
    }


    private LoginUI() {
        //get container of the window
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0, 1, 10, 10));
        container.setBackground(Color.white);

        //title of KidPaint
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        JTextArea titleArea1 = new JTextArea();
        titleArea1.setFont(font1);
        titleArea1.setEditable(false);
        titleArea1.setText("\nKidPaint");
        titleArea1.setMargin(new Insets(0, 10, 0, 10));
        titleArea1.setBackground(Color.white);

        //description of the KidPaint
        Font font2 = new Font("Georgia", Font.PLAIN, 15);
        JTextArea descriptionArea2 = new JTextArea();
        descriptionArea2.setFont(font2);
        descriptionArea2.setEditable(false);
        descriptionArea2.setLineWrap(true);
        descriptionArea2.setWrapStyleWord(true);
        descriptionArea2.setMargin(new Insets(10, 20, 10, 20));
        descriptionArea2.setText("This application allows  users to use the shared drawing board to draw at the " +
                "same time and synchronize it to other users in the same group. After entering your name, click " +
                "the login button to select an existing group or create a new group, and then start enjoying the " +
                "fun of drawing!");
        descriptionArea2.setBackground(Color.white);
        heroJPane = new JPanel();
        heroJPane.setBackground(Color.white);
        heroJPane.setLayout(new GridLayout(2, 0));
        heroJPane.add(titleArea1);
        heroJPane.add(descriptionArea2);
        container.add(heroJPane, BorderLayout.NORTH);

        //enter name and licence number
        nameJLabel = new JLabel("Enter your name：");
        nameJLabel.setBounds(150, 50, 300, 30);
        licenceJLabel = new JLabel("Enter your licence number(default: 123456): ");
        licenceJLabel.setBounds(150, 100, 300, 30);
        nameJTextField = new JTextField();
        nameJTextField.setBounds(450, 50, 200, 30);
        licenceField = new JPasswordField();
        licenceField.setBounds(450, 100, 200, 30);

        //add login to container
        loginJPane = new JPanel();
        loginJPane.setBackground(Color.white);
        loginJPane.setLayout(null);
        loginJPane.add(nameJLabel);
        loginJPane.add(nameJTextField);
        loginJPane.add(licenceJLabel);
        loginJPane.add(licenceField);
        container.add(loginJPane, BorderLayout.CENTER);

        //submit button
        submitJButton = new JButton("Submit");
        submitJButton.setBounds(350, 30, 80, 50);
        submitJPane = new JPanel();
        submitJPane.setLayout(null);
        submitJPane.setBackground(Color.white);
        container.add(submitJPane, BorderLayout.SOUTH);
        submitJPane.add(submitJButton);

        //lister of submit button
        submitJButton.addActionListener(this);
        this.setTitle("KidPaint.Login");
        this.setSize(800, 600);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //if click submit button
        if (e.getSource() == submitJButton) {
            String licence = "123456";

            String name = nameJTextField.getText();
            String p1 = String.copyValueOf(licenceField.getPassword());

            if (p1.equals(licence) && !Objects.equals(name, "")) {
                KidPaint.name = name;
                isLogin = true;
            }
        }
    }
}

