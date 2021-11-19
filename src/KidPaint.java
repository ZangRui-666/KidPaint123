import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KidPaint {
	static String name;
	static String studioName = "";
	static String studioIP;
	static String port;
	static boolean isServer= true;
	static Socket socket;
	static DatagramSocket dSocket;

	public static void main(String[] args) {
		/*LoginUI loginUI = LoginUI.getInstance();
		loginUI.setVisible(true);

		while (!LoginUI.isLogin){}
		loginUI.setVisible(false);
		name = loginUI.getName();

		GroupUI groupUI = GroupUI.getInstance();
		groupUI.setVisible(true);
		while (studioName.equals("")){}
		groupUI.setVisible(false);*/

		UI ui = UI.getInstance();// get the instance of UI
		UI.dataList.addLast(new int[50][50]);

		ui.setData(new int[50][50], 20);	// set the data array and block size. comment this statement to use the default data array and block size.
		ui.setVisible(true);				// set the ui
	}
}
