import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupUI extends JFrame implements ActionListener {
    JLabel groupJLabel, newGroupJLabel, jl1, jl2, jl3;
    JTextField groupNameJTextField, jtf;
    JPasswordField jpf;
    JButton createJButton, jb1, jb2, jb3;
    JPanel groupDesJPanel, groupButtonsJPanel, createDesJPanel, createJPanel,  jp1, jp2, jp3, jp4;
    String name;
    static boolean isLogin = false;

    static ArrayList<String> currentGroupNames= new ArrayList<String>();
    private static GroupUI instance;

    public static GroupUI getInstance() {
        if (instance == null)
            instance = new GroupUI();

        return instance;
    }
    public GroupUI() {
        currentGroupNames.add("stu1");
        currentGroupNames.add("stu2");
        currentGroupNames.add("stu3");
        currentGroupNames.add("stu4");
        currentGroupNames.add("stu5");
        currentGroupNames.add("stu6");
        currentGroupNames.add("stu7");
        currentGroupNames.add("stu8");

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0, 1, 10, 10));
        container.setBackground(Color.white);


        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        groupJLabel = new JLabel();
        groupJLabel.setFont(font1);
        groupJLabel.setText("Current existing groups(click to join in): ");
        groupJLabel.setBounds(10, 20, 600, 30);

        groupDesJPanel = new JPanel();
        groupDesJPanel.setLayout(null);
        groupDesJPanel.add(groupJLabel);
        groupDesJPanel.setBounds(0,0,this.getWidth(), 200);

        container.add(groupDesJPanel);




        groupButtonsJPanel = new JPanel();
        groupButtonsJPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        for(int i = 0; i < currentGroupNames.size(); i++){
            JButton button = new JButton(currentGroupNames.get(i));
            button.setPreferredSize(new Dimension(80, 50));
            groupButtonsJPanel.add(button);
        }
        container.add(groupButtonsJPanel);

        newGroupJLabel = new JLabel();
        newGroupJLabel.setFont(font1);
        newGroupJLabel.setText("Create a new group, input the group name and click create button: ");
        newGroupJLabel.setBounds(10, 10, 800, 30);

        createDesJPanel = new JPanel();
        createDesJPanel.setLayout(null);
        createDesJPanel.add(newGroupJLabel);
        createDesJPanel.setBounds(0,0,this.getWidth(), 200);

        container.add(createDesJPanel);

        groupNameJTextField = new JTextField();
        //groupNameJTextField.setBounds(400, 50, 200, 30);
        container.add(groupNameJTextField);

        createJButton = new JButton("Submit");
        createJButton.setBounds(350, 30, 80, 50);
        createJPanel = new JPanel();
        createJPanel.setLayout(null);
        createJPanel.setBackground(Color.white);
        container.add(createJPanel, BorderLayout.SOUTH);
        createJPanel.add(createJButton);




//        jp1 = new JPanel();
//        jtf = new JTextField();
//        jp1.add(jl1);
//        jp1.add(jtf);
//
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


        //submitJButton.addActionListener(this);
        this.setTitle("KidPaint.FindGroup");
        this.setSize(800, 600);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GroupUI();
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

class GroupUITest extends JPanel {

}


