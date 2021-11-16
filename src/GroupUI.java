import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupUI extends JFrame implements ActionListener {
    JTextField groupNameJTextField;
    JTextArea groupArea, newGroupArea;
    JButton createJButton;
    JPanel groupJPanel, groupButtonsJPanel, createJPanel;
    String name;

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
        groupArea = new JTextArea();
        groupArea.setEditable(false);
        groupArea.setLineWrap(true);
        groupArea.setWrapStyleWord(true);
        groupArea.setFont(font1);
        groupArea.setText("Current existing groups(click to join in): ");
        groupArea.setBounds(10, 20, 700, 30);

        groupJPanel = new JPanel();
        groupJPanel.setLayout(null);
        groupJPanel.add(groupArea);

        groupButtonsJPanel = new JPanel();
        groupButtonsJPanel.setBounds(50, 70, 600, 500);
        for(int i = 0; i < currentGroupNames.size(); i++){
            JButton button = new JButton(currentGroupNames.get(i));
            button.setPreferredSize(new Dimension(100, 50));
            groupButtonsJPanel.add(button);
        }
        groupButtonsJPanel.setBackground(Color.white);
        groupJPanel.add(groupButtonsJPanel);
        groupJPanel.setBackground(Color.white);
        container.add(groupJPanel);

        newGroupArea = new JTextArea();
        newGroupArea.setFont(font1);
        newGroupArea.setEditable(false);
        newGroupArea.setLineWrap(true);
        newGroupArea.setWrapStyleWord(true);
        newGroupArea.setText("Create a new group, input the group name and click create button: ");
        newGroupArea.setBounds(10, 20, 750, 30);

        createJPanel = new JPanel();
        createJPanel.setLayout(null);
        createJPanel.add(newGroupArea);

        groupNameJTextField = new JTextField();
        groupNameJTextField.setBounds(220, 90, 200, 30);
        createJPanel.add(groupNameJTextField);

        createJButton = new JButton("Create");
        createJButton.setBounds(280, 150, 80, 50);
        createJPanel.add(createJButton);
        createJPanel.setBackground(Color.white);
        container.add(createJPanel);

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

//        if (e.getSource() == jb1) {
//            String student = "123456";
//
//            String name = jtf.getText();
//            String p1 = String.copyValueOf(jpf.getPassword());
//
//            if (p1.equals(student)) {
//                this.name = name;
//            }
//        }
    }

    public String getName() {
        return name;
    }

}

class GroupUITest extends JPanel {

}


