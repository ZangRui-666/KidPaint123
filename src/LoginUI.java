import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame implements ActionListener {
    JLabel jl1, jl2, jl3;
    JTextField jtf;
    JPasswordField jpf;
    JButton jb1, jb2, jb3;
    JPanel jp1, jp2, jp3, jp4;
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
        JTextField textField1 = new JTextField();
        textField1.setFont(font1);
        textField1.setText("KidPaint");


        Font font2 = new Font("Georgia", Font.PLAIN, 15);
        JTextField textField2 = new JTextField();
        textField2.setFont(font1);
        textField2.setText("This application allows  users to use the shared drawing board to draw at the " +
                "same time and synchronize it to other users in the same group. After entering your name, click " +
                "the login button to select an existing group or create a new group, and then start enjoying the " +
                "fun of drawing!");

        //container.add(textField2,BorderLayout.NORTH);

        jl1 = new JLabel("Enter your name：");
        jl2 = new JLabel("Enter your licence number(For normal user, it's 123456");

        jp1 = new JPanel();
        jtf = new JTextField();
        jp1.add(jl1);
        jp1.add(jtf);

        jp2 = new JPanel();
        jp2.add(jl2);
        jpf = new JPasswordField(16);
        jp2.add(jpf);

        jb1 = new JButton("Submit");


        this.add(jp1);
        this.add(jp2);
        this.add(jb1);

        jb1.addActionListener(this);
        this.setSize(400, 300);
        this.setLocation(300, 300);
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

            String name = jtf.getText();
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

class test extends JPanel {

}


