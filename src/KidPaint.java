import java.net.DatagramSocket;
import java.net.Socket;

public class KidPaint {
    static String name = "";
    static String studioName = "";
    static boolean isServer = false;
    static Socket socket;
    static DatagramSocket dSocket;

    public static void main(String[] args) {
        LoginUI loginUI = LoginUI.getInstance();
        loginUI.setVisible(true);

        while (!LoginUI.isLogin) {
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
        }
        loginUI.setVisible(false);

        GroupUI groupUI = GroupUI.getInstance();
        groupUI.setVisible(true);
        while (studioName.equals("")) {
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
        }
        groupUI.setVisible(false);

        UI ui = UI.getInstance();// get the instance of UI
        UI.dataList.addLast(new int[20][20]);

        if (isServer)
            ui.setData(new int[20][20], 25);
        ui.setVisible(true);                // set the ui
    }
}
