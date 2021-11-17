import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GroupUI extends JFrame implements ActionListener {

    JTextField groupNameJTextField;
    JTextArea groupArea, newGroupArea;
    JButton createJButton;
    JPanel groupJPanel, groupButtonsJPanel, createJPanel;
    String name;

    static ArrayList<String> currentGroupNames = new ArrayList<String>();
    static ArrayList<String> currentServerIP = new ArrayList<>();
    ArrayList<JButton> groupButtons = new ArrayList<>();
    static boolean newGroup = false;
    private static GroupUI instance;

    static DatagramSocket socket;

    public static GroupUI getInstance() {
        if (instance == null)
            instance = new GroupUI();
        return instance;
    }

    public GroupUI() {
        try {
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.out.println("There is something wrong on building socket!");
            e.printStackTrace();
        }
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
        for (int i = 0; i < currentGroupNames.size(); i++) {
            JButton button = new JButton(currentGroupNames.get(i));
            groupButtons.add(button);
            button.addActionListener(this);
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


        createJButton.addActionListener(this);
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

        if (e.getSource() == createJButton) {
            String newGroupName = groupNameJTextField.getText().trim();
            if (newGroupName.isEmpty()) return;
            name = newGroupName;
            KidPaint.isServer = true;
            System.out.println("create new group name: " + name);
        }
        for (int i = 0; i < groupButtons.size(); i++) {
            if (e.getSource() == groupButtons.get(i)) {
                KidPaint.isServer = false;
                name = currentGroupNames.get(i);
                System.out.println("join in group name: " + name);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    private void receiveMessage(DatagramSocket socket){
        byte[] b = new byte[1024];
        DatagramPacket packet = new DatagramPacket(b, 1024);
        try {
            socket.receive(packet);
            String str = new String(packet.getData());
            String[] splitStr = str.split(";;");
            currentGroupNames.add(splitStr[0]);
            currentServerIP.add(splitStr[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer(String IP, int port){
        try {
            KidPaint.socket = new Socket(IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String sentMessage) {
        try {
            byte[] msg = sentMessage.getBytes();
            InetAddress dest = InetAddress.getByName("255.255.255.255");
            int port = 5555;
            DatagramPacket packet = new DatagramPacket(msg, msg.length, dest, port);
            socket.send(packet);
            System.out.println("The message: " + sentMessage + " has been sent.");
        } catch (IOException e) {
            System.out.println("There is something wrong in broadcasting finding group message!");
            e.printStackTrace();
        }
    }
}

class GroupUITest extends JPanel {

}


