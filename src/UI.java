import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.awt.Color;
import javax.swing.border.LineBorder;

enum PaintMode {Pixel, Area}

public class UI extends JFrame {
    private JTextField msgField;
    private JTextArea chatArea;
    private JPanel pnlColorPicker;
    private JPanel paintPanel;
    private JToggleButton tglPen;
    private JToggleButton tglBucket;
    private JButton revocationBt;
    static ServerSocket serverSocket;
    static List<Socket> connectedClients = new ArrayList();
    static List<String> clientsNames = new ArrayList<>();

    private String message;
    private static UI instance;
    private int selectedColor = -543230;    //golden

    int[][] data = new int[20][20];            // pixel color data array
    static LinkedList<int[][]> dataList = new LinkedList<>();
    static int MAX = 10;

    int blockSize = 16;
    PaintMode paintMode = PaintMode.Pixel;

    /**
     * get the instance of UI. Singleton design pattern.
     *
     * @return
     */
    public static UI getInstance() {
        if (instance == null)
            instance = new UI();

        return instance;
    }

    /**
     * private constructor. To create an instance of UI, call UI.getInstance() instead.
     */
    private UI() {
        if (KidPaint.isServer) {
            try {
                KidPaint.dSocket = new DatagramSocket(5555);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                while (true) {
                    try {
                        receiveAndNotify();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                try {
                    server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            new Thread(() -> receiveData(KidPaint.socket)).start();
        }
        setTitle("KidPaint");


        clientsNames.add("test1");
        clientsNames.add("test2");
        clientsNames.add("test3");
        clientsNames.add("test4");
        clientsNames.add("test5");

        JPanel basePanel = new JPanel();
        getContentPane().add(basePanel, BorderLayout.CENTER);
        basePanel.setLayout(new BorderLayout(0, 0));

        paintPanel = new JPanel() {

            // refresh the paint panel
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2 = (Graphics2D) g; // Graphics2D provides the setRenderingHints method

                // enable anti-aliasing
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHints(rh);

                // clear the paint panel using black
                g2.setColor(Color.black);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());

                // draw and fill circles with the specific colors stored in the data array
                synchronized (data) {
                    for (int x = 0; x < data.length; x++) {
                        for (int y = 0; y < data[0].length; y++) {
                            g2.setColor(new Color(data[x][y]));
                            g2.fillArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
                            g2.setColor(Color.darkGray);
                            g2.drawArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
                        }
                    }
                }
            }
        };

        paintPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            // handle the mouse-up event of the paint panel
            @Override
            public void mouseReleased(MouseEvent e) {
                boolean isChanged = false;
                int column = e.getX() / blockSize;
                int row = e.getY() / blockSize;
                if (paintMode == PaintMode.Area && e.getX() >= 0 && e.getY() >= 0)
                    isChanged = paintArea(column, row, selectedColor);
                if (!isChanged) return;

                if (KidPaint.isServer)
                    //serverSendData(KidPaint.name);
                    serverSendData(column, row, selectedColor, 150);
                else
                    clientSend(column, row, selectedColor, 150);
            }
        });

        paintPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (paintMode == PaintMode.Pixel && e.getX() >= 0 && e.getY() >= 0) {
                    int column = e.getX() / blockSize;
                    int row = e.getY() / blockSize;
                    synchronized (data){
                    if (data[column][row] != selectedColor) {
                        paintPixel(column, row, selectedColor);
                        if (KidPaint.isServer)
                            //serverSendData(KidPaint.name);
                            serverSendData(column, row, selectedColor, 135);
                        else
                            clientSend(column, row, selectedColor, 135);
                    }}
                }

            }


            @Override
            public void mouseMoved(MouseEvent e) {
            }

        });

        paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));

        JScrollPane scrollPaneLeft = new JScrollPane(paintPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        basePanel.add(scrollPaneLeft, BorderLayout.CENTER);

        JPanel toolPanel = new JPanel();
        toolPanel.setPreferredSize(new Dimension(600, 100));
        basePanel.add(toolPanel, BorderLayout.NORTH);
        toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));


        pnlColorPicker = new JPanel();
        pnlColorPicker.setPreferredSize(new Dimension(24, 24));
        pnlColorPicker.setBackground(new Color(selectedColor));
        pnlColorPicker.setBorder(new LineBorder(new Color(0, 0, 0)));

        // show the color picker
        pnlColorPicker.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ColorPicker picker = ColorPicker.getInstance(UI.instance);
                Point location = pnlColorPicker.getLocationOnScreen();
                location.y += pnlColorPicker.getHeight();
                picker.setLocation(location);
                picker.setVisible(true);
            }

        });

        toolPanel.add(pnlColorPicker);

        tglPen = new JToggleButton("Pen");
        tglPen.setSelected(true);
        toolPanel.add(tglPen);

        tglBucket = new JToggleButton("Bucket");
        toolPanel.add(tglBucket);

        // change the paint mode to PIXEL mode
        tglPen.addActionListener(arg0 -> {
            tglPen.setSelected(true);
            tglBucket.setSelected(false);
            paintMode = PaintMode.Pixel;
        });

        // change the paint mode to AREA mode
        tglBucket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                tglPen.setSelected(false);
                tglBucket.setSelected(true);
                paintMode = PaintMode.Area;
            }
        });


        revocationBt = new JButton("revocation");
        toolPanel.add(revocationBt);
        revocationBt.addActionListener(e -> {
            System.out.println("The size of linkedList is" + dataList.size());
            System.out.println("now the data is");
            for(int i =0;i<20;i++)
                System.out.print(data[i][0]+", ");
            System.out.println();
            if (dataList.size() > 1) {
                dataList.removeLast();
                System.out.println("remove last");
            }
            System.out.println("The data after update is");
            for(int i =0;i<20;i++)
                System.out.print(data[i][0]+", ");
            updatePainting(dataList.getLast());

        });

        //save button
        JButton saveBt = new JButton("save");
        toolPanel.add(saveBt);
        saveBt.addActionListener(e -> {
            //save action
            //get date
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
            System.out.println(dateFormat.format(date));

            String filename = "kidPaint_" + dateFormat.format(date) + ".dat";
            File savedFile = new File(filename);
            try {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(savedFile));
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[0].length; j++) {
                        out.writeInt(data[i][j]);
                    }
                }
                out.close();
            } catch (FileNotFoundException ex) {
                System.out.println("the file is no found");
                //ex.printStackTrace();
            } catch (IOException ioException) {
                System.out.println("there is an IO exception!");
                //ioException.printStackTrace();
            }
        });

        //load button
        JButton loadBt = new JButton("load (input filename)");
        JTextField loadFileText = new JTextField();
        loadFileText.setPreferredSize(new Dimension(150, 30));
        toolPanel.add(loadBt);
        toolPanel.add(loadFileText);
        loadBt.addActionListener(e -> {
            //load action
            if (loadFileText.equals("")) return;
            File inputFile = new File(loadFileText.getText());
            if (!inputFile.exists()) {
                System.out.println("The file is not exist!");
                return;
            }
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
                int[][] inputDataArray = new int[data.length][data[0].length];
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[0].length; j++) {
                        inputDataArray[i][j] = in.readInt();
                    }
                }
                updatePainting(inputDataArray);
                in.close();
                if (KidPaint.isServer) serverSendData(KidPaint.name);
                else clientSend();
            } catch (FileNotFoundException ex) {
                System.out.println("The file is not exist!");
                ex.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        toolPanel.setBackground(Color.white);
        JPanel manageGroupJP = new JPanel();
        manageGroupJP.setLayout(new FlowLayout());

        if (KidPaint.isServer) {
            String[] memberStrArray = new String[clientsNames.size()];
            for (int i = 0; i < clientsNames.size(); i++)
                memberStrArray[i] = " " + clientsNames.get(i);
            JList<String> listView = new JList<>(memberStrArray);
            JScrollPane sp = new JScrollPane(listView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sp.setPreferredSize(new Dimension(100, 25));

            JButton btn = new JButton("Delete");
            manageGroupJP.add(sp);
            manageGroupJP.add(btn);
            manageGroupJP.setBackground(Color.white);

            toolPanel.add(manageGroupJP);
            btn.addActionListener(e -> {
                synchronized (clientsNames) {
                    synchronized (connectedClients) {
                        int index = clientsNames.indexOf(listView.getSelectedValue());
                        try {
                            connectedClients.get(index).close();
                            connectedClients.remove(index);
                            clientsNames.remove(index);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                System.out.println(listView.getSelectedValue().trim());
                System.out.println("the size of the listView" + memberStrArray.length);
            });
        }

        JPanel msgPanel = new JPanel();

        getContentPane().add(msgPanel, BorderLayout.EAST);

        msgPanel.setLayout(new BorderLayout(0, 0));

        msgField = new JTextField();    // text field for inputting message

        msgPanel.add(msgField, BorderLayout.SOUTH);

        // handle key-input event of the message field
        msgField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    // if the user press ENTER
                    String msg = KidPaint.name + ": " + msgField.getText();
                    if (KidPaint.isServer) {
                        serverSendData(msg.getBytes());
                        onTextInputted(msg);
                    } else {
                        clientSend(msg.getBytes());
                    }
                    msgField.setText("");
                }
            }

        });


        chatArea = new JTextArea();        // the read only text area for showing messages
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        JScrollPane scrollPaneRight = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneRight.setPreferredSize(new Dimension(300, this.getHeight()));
        msgPanel.add(scrollPaneRight, BorderLayout.CENTER);

        this.setSize(new Dimension(800, 700));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //for server use
    public void receiveAndNotify() throws IOException {
        message = "##" + KidPaint.studioName + ";;" + "2345";
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            KidPaint.dSocket.receive(packet);
            String srcAddress = packet.getAddress().toString().substring(1);
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received a packet: " + msg);
            int dstPort = packet.getPort();
            System.out.println(msg.equals("Find Studio"));
            if (msg.equals("Find Studio")) {
                DatagramPacket p = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(srcAddress), dstPort);
                KidPaint.dSocket.send(p);
                System.out.println("Sent back a packet");
            }
        }
    }

    private void receiveData(Socket socket) {
        try {
            byte[] buffer = new byte[1024];
            DataInputStream in = new DataInputStream(socket.getInputStream());
            while (true) {
                int len = in.readInt();
                int specifier = in.readInt();
                System.out.println("Server received a message"+ specifier);

                if (specifier == 100) {
                    in.read(buffer, 0, len);
                    updateChatbox(buffer, len);
                    if (KidPaint.isServer) {
                        byte[] trimmedBuffer = new byte[len];
                        System.arraycopy(buffer, 0, trimmedBuffer, 0, len);
                        serverSendData(trimmedBuffer);
                    }
                } else if (specifier == 223) {
                    int[][] newData = new int[20][20];
                    for (int i = 0; i < 20; i++)
                        for (int j = 0; j < 20; j++)
                            newData[i][j] = in.readInt();
                    updatePainting(newData);
                    if (KidPaint.isServer) {
                        String name = clientsNames.get(connectedClients.indexOf(socket));
                        serverSendData(name);

                    }
                } else if (specifier == 236) {
                    if (KidPaint.isServer) {
                        synchronized (clientsNames) {
                            synchronized (connectedClients) {
                                DataInputStream dos = new DataInputStream(socket.getInputStream());
                                byte[] b = new byte[1024];
                                dos.read(b, 0, len);
                                String str = new String(b, 0, len);
                                int index = connectedClients.indexOf(socket);
                                clientsNames.add(index, str);
                                String msg = str + " has joined the studio";
                                serverSendData((msg).getBytes());
                                onTextInputted(msg);
                            }
                        }}
                    } else if (specifier == 135 || specifier == 150) {
                        int row = in.readInt();
                        int column = in.readInt();
                        int color = in.readInt();
                        if (specifier == 135) {
                            paintPixel(row, column, color);
                            System.out.println("Server received a message");
                            if (KidPaint.isServer)
                                serverSendData(row, column, color, 135);
                        }
                        if (specifier == 150) {
                            paintArea(row, column, color);
                            if (KidPaint.isServer)
                                serverSendData(row, column, color, 150);
                        }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateChatbox(byte[] buffer, int length) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(new String(buffer, 0, length) + "\n");
        });
    }

    private void updatePainting(int[][] newData) {
        synchronized (data) {
            data = newData;
        }
        paintPanel.repaint();
    }

    public void clientSend() {
        try {
            DataOutputStream out = new DataOutputStream(KidPaint.socket.getOutputStream());
            out.writeInt(1600);
            out.writeInt(223);
            synchronized (data) {
                for (int i = 0; i < 20; i++)
                    for (int j = 0; j < 20; j++)
                        out.writeInt(data[i][j]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void clientSend(int row, int column, int color, int specifier) {
        try {
            DataOutputStream out = new DataOutputStream(KidPaint.socket.getOutputStream());
            System.out.println("clientSendMessage" + row + ", " + column + ", " + color);
            out.writeInt(12);
            out.writeInt(specifier);
            out.writeInt(row);
            out.writeInt(column);
            out.writeInt(color);
        } catch (IOException e) {
        }


    }

    public void clientSend(byte[] data) {
        try {
            DataOutputStream out = new DataOutputStream(KidPaint.socket.getOutputStream());
            out.writeInt(data.length);
            out.writeInt(100);
            out.write(data);
        } catch (IOException e) {
        }


    }

    public void serverSendData(String name) {
        String msg = name + "has made a change";
        onTextInputted(msg);
        serverSendData(msg.getBytes());
        synchronized (connectedClients) {
            System.out.println("Add data to dataList");
            for (Socket clientSocket : connectedClients) {
                new Thread(() -> {
                    try {
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        out.writeInt(1600);
                        out.writeInt(223);
                        synchronized (data) {
                            for (int i = 0; i < 20; i++)
                                for (int j = 0; j < 20; j++)
                                    out.writeInt(data[i][j]);
                        }
                    } catch (IOException e) {
                    }

                }).start();
            }

        }
    }

    public void serverSendData(byte[] data) {
        synchronized (connectedClients) {
            for (Socket clientSocket : connectedClients) {
                new Thread(() -> {
                    try {
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        System.out.println("serverSendMessage" + new String(data) + data.length);
                        out.writeInt(data.length);
                        out.writeInt(100);
                        out.write(data, 0, data.length);
                    } catch (IOException e) {
                    }


                }).start();
            }
        }
    }

    public void serverSendData(int row, int column, int color, int specifier) {
        synchronized (connectedClients) {
            for (Socket clientSocket : connectedClients) {
                new Thread(() -> {
                    try {
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        System.out.println("serverSendMessage" + row + ", " + column + ", " + ", " + color);
                        out.writeInt(12);
                        out.writeInt(specifier);
                        out.writeInt(row);
                        out.writeInt(column);
                        out.writeInt(color);
                    } catch (IOException e) {
                    }


                }).start();
            }
        }
    }

    /**
     * it will be invoked if the user selected the specific color through the color picker
     *
     * @param colorValue - the selected color
     */
    public void selectColor(int colorValue) {
        SwingUtilities.invokeLater(() -> {
            selectedColor = colorValue;
            pnlColorPicker.setBackground(new Color(colorValue));
        });
    }

    /**
     * it will be invoked if the user inputted text in the message field
     *
     * @param text - user inputted text
     */
    private void onTextInputted(String text) {
        if (KidPaint.isServer)
            chatArea.setText(chatArea.getText() + text + "\n");
    }

    /**
     * change the color of a specific pixel
     *
     * @param col, row - the position of the selected pixel
     */
    public synchronized void paintPixel(int col, int row, int color) {
        synchronized (data) {
            if (col >= data.length || row >= data[0].length) return;

            if (data[col][row] == color)
                return;
        }
        synchronized (data) {
            data[col][row] = color;


        paintPanel.repaint(col * blockSize, row * blockSize, blockSize, blockSize);
        if(KidPaint.isServer){
            if (dataList.size() > MAX) {
                dataList.removeFirst();
            }
            dataList.addLast(this.data);
            System.out.println("add to datalist");
            for(int i =0;i<20;i++)
                System.out.print(data[i][0]+", ");
            System.out.println();
        }}

    }

    public void serve(Socket clientSocket) {
        receiveData(clientSocket);
    }

    public void server() throws IOException {
        serverSocket = new ServerSocket(2345);

        while (true) {
            Socket cSocket = serverSocket.accept();
            DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
            out.writeInt(1600);
            out.writeInt(223);
            synchronized (data) {
                for (int i = 0; i < 20; i++)
                    for (int j = 0; j < 20; j++)
                        out.writeInt(data[i][j]);
            }
            synchronized (connectedClients) {
                connectedClients.add(cSocket);
                System.out.printf("Total %d clients are connected.\n", connectedClients.size());
            }
            Thread t = new Thread(() -> {
                serve(cSocket);
                synchronized (connectedClients) {
                    connectedClients.remove(cSocket);
                }
            });
            t.start();
        }
    }


    /**
     * change the color of a specific area
     *
     * @param col, row - the position of the selected pixel
     * @return a list of modified pixels
     */
    public boolean paintArea(int col, int row, int color) {
        LinkedList<Point> filledPixels = new LinkedList<>();
        boolean signal = false;
        synchronized (data) {
            if (col >= data.length || row >= data[0].length) return false;

            int oriColor = data[col][row];
            LinkedList<Point> buffer = new LinkedList<>();

            if (oriColor != color) {
                buffer.add(new Point(col, row));
                signal = true;

                while (!buffer.isEmpty()) {
                    Point p = buffer.removeFirst();
                    int x = p.x;
                    int y = p.y;

                    if (data[x][y] != oriColor) continue;

                    data[x][y] = color;
                    filledPixels.add(p);

                    if (x > 0 && data[x - 1][y] == oriColor) buffer.add(new Point(x - 1, y));
                    if (x < data.length - 1 && data[x + 1][y] == oriColor) buffer.add(new Point(x + 1, y));
                    if (y > 0 && data[x][y - 1] == oriColor) buffer.add(new Point(x, y - 1));
                    if (y < data[0].length - 1 && data[x][y + 1] == oriColor) buffer.add(new Point(x, y + 1));
                }
            }

            paintPanel.repaint();
        }
        if(KidPaint.isServer){
            if (dataList.size() > MAX) {
                dataList.removeFirst();
            }
            dataList.addLast(this.data);
            System.out.println("add" + Arrays.toString(data[0]));
        }
        return signal;
    }

    /**
     * set pixel data and block size
     *
     * @param data
     * @param blockSize
     */
    public void setData(int[][] data, int blockSize) {
        this.data = data;
        this.blockSize = blockSize;
        paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));
        paintPanel.repaint();
    }
}
