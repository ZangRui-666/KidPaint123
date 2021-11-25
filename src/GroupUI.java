/*
    This class shows a group page for the user. Users can find all current existing groups and choose one to join in.
    Also, they can create a new group and be the server of the group by himself.
 */

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
    static ArrayList<Integer> portNum = new ArrayList<>();

    ArrayList<JButton> groupButtons = new ArrayList<>();
    private static GroupUI instance;

    public static GroupUI getInstance() {
        if (instance == null)
            instance = new GroupUI();
        return instance;
    }

    private GroupUI() {
        try {
            KidPaint.dSocket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.out.println("There is something wrong on building socket!");
            e.printStackTrace();
        }


        sendMessage();
        long timer = System.currentTimeMillis();
        Thread thread = new Thread(() -> receiveMessage());
        thread.start();

        while (System.currentTimeMillis() - timer < 1500) {
        }
        KidPaint.dSocket.close();

        System.out.println(studioNames);

        //container of the Window
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0, 1, 10, 10));
        container.setBackground(Color.white);

        //area of getting existing group
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

        //set existing group buttons to the button panel
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

        //create new group area
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

        //type new group name
        groupNameJTextField = new JTextField();
        groupNameJTextField.setBounds(220, 90, 200, 30);
        createJPanel.add(groupNameJTextField);

        //create button
        createJButton = new JButton("Create");
        createJButton.setBounds(280, 150, 80, 50);
        createJPanel.add(createJButton);
        createJPanel.setBackground(Color.white);
        container.add(createJPanel);

        //action listener for create new group button
        createJButton.addActionListener(this);
        this.setTitle("KidPaint.FindGroup");
        this.setSize(800, 600);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //click create new group button
        if (e.getSource() == createJButton) {
            String newGroupName = groupNameJTextField.getText().trim();
            if (newGroupName.isEmpty()) return;
            KidPaint.studioName = newGroupName;
            KidPaint.isServer = true;
            System.out.println("create new group name: " + KidPaint.studioName);
        }

        //click existing group button
        for (int i = 0; i < groupButtons.size(); i++)
            if (e.getSource() == groupButtons.get(i)) {
                KidPaint.isServer = false;
                KidPaint.studioName = studioNames.get(i);
                connectToServer(serverIP.get(i), portNum.get(i));
                System.out.println("join in group name: " + KidPaint.studioName);
                break;
            }
    }

    /**
     * Receive UDP packet from the server
     */
    private void receiveMessage() {
        byte[] b = new byte[1024];
        DatagramPacket packet = new DatagramPacket(b, 1024);
        try {
            while (true) {
                KidPaint.dSocket.receive(packet);
                String str = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received a packet: " + str);
                if (str.charAt(0) == '#' && str.charAt(1) == '#') {
                    str = str.substring(2);
                    String[] splitStr = str.split(";;");
                    studioNames.add(splitStr[0]);
                    portNum.add(Integer.valueOf(splitStr[1]));
                    serverIP.add(packet.getAddress().toString().substring(1));
                }
            }
        } catch (IOException e) {
            System.out.println("Receive UDP over");
        }
    }

    /**
     * Connect to the chosen server by TCP
     *
     * @param IP   The IP of the chosen server
     * @param port The port number of the chosen server
     */
    private void connectToServer(String IP, int port) {
        try {
            KidPaint.socket = new Socket(IP, port);
            DataOutputStream dop = new DataOutputStream(KidPaint.socket.getOutputStream());
            dop.writeInt(KidPaint.name.getBytes().length);
            dop.writeInt(236);
            dop.write(KidPaint.name.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a broadcast package to find the server
     */
    private void sendMessage() {
        try {
            byte[] msg = "Find Studio".getBytes();
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


