import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GroupUI extends JFrame implements ActionListener {

    JTextField groupNameJTextField;
    JTextArea groupArea, newGroupArea;
    JButton createJButton;
    JPanel groupJPanel, groupButtonsJPanel, createJPanel;

    static ArrayList<String> studioNames = new ArrayList<>();
    static ArrayList<String> serverIP = new ArrayList<>();
    ArrayList<JButton> groupButtons = new ArrayList<>();
    private static GroupUI instance;

    public static GroupUI getInstance() {
        if (instance == null)
            instance = new GroupUI();
        return instance;
    }

    public GroupUI() {
        try {
            KidPaint.dSocket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.out.println("There is something wrong on building socket!");
            e.printStackTrace();
        }


        System.out.println("sfdf");
        sendMessage();
        long timer = System.currentTimeMillis();
        Thread thread = new Thread(() -> receiveMessage(KidPaint.dSocket));
        thread.start();

        while (System.currentTimeMillis() - timer < 1500) {
        }
        KidPaint.dSocket.close();

        System.out.println(studioNames);

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
        for (String studioName : studioNames) {
            JButton button = new JButton(studioName);
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
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == createJButton) {
            String newGroupName = groupNameJTextField.getText().trim();
            if (newGroupName.isEmpty()) return;
            KidPaint.studioName = newGroupName;
            KidPaint.isServer = true;
            System.out.println("create new group name: " + KidPaint.studioName);
        }
        for (int i = 0; i < groupButtons.size(); i++)
            if (e.getSource() == groupButtons.get(i)) {
                KidPaint.isServer = false;
                KidPaint.studioName = studioNames.get(i);
                connectToServer(serverIP.get(i));
                System.out.println("join in group name: " + KidPaint.studioName);
                break;
            }
    }


    private void receiveMessage(DatagramSocket socket) {
        byte[] b = new byte[1024];
        DatagramPacket packet = new DatagramPacket(b, 1024);
        try {
            KidPaint.dSocket.receive(packet);
            String str = new String(packet.getData());
            if (str.charAt(0) == '#' && str.charAt(1) == '#') {
                str = str.substring(2);
                String[] splitStr = str.split(";;");
                studioNames.add(splitStr[0]);
                serverIP.add(splitStr[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer(String IP) {
        try {
            KidPaint.socket = new Socket(IP, 2345);
            DataOutputStream dop = new DataOutputStream(KidPaint.socket.getOutputStream());
            dop.writeInt(KidPaint.name.getBytes().length);
            dop.writeInt(236);
            dop.write(KidPaint.name.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
            byte[] msg = "abc".getBytes();
            InetAddress dest = InetAddress.getByName("255.255.255.255");
            int port = 5555;
            DatagramPacket packet = new DatagramPacket(msg, msg.length, dest, port);
            KidPaint.dSocket.send(packet);
            System.out.println("The message: " + "Find Studio" + " has been sent.");
        } catch (IOException e) {
            System.out.println("There is something wrong in broadcasting finding group message!");
            e.printStackTrace();
        }
    }
}


