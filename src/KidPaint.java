import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KidPaint {
	static String name = "XiaoMing";
	static String studioName = "" ;
	static String studioIP;
	static String port;
	static boolean isServer= false;
	static Socket socket;
	static DatagramSocket dSocket;

	public static void main(String[] args) {
//		LoginUI loginUI = LoginUI.getInstance();
//		loginUI.setVisible(true);
//
//		while (!LoginUI.isLogin){
//			try {
//				Thread.sleep(1);
//			} catch(Exception ex) {}
//		}
//		loginUI.setVisible(false);
//		name = loginUI.getName();

		GroupUI groupUI = GroupUI.getInstance();
		groupUI.setVisible(true);
		while (studioName.equals("")){
			try {
				Thread.sleep(0);
			} catch(Exception ex) {}
		}
		groupUI.setVisible(false);

		UI ui = UI.getInstance();// get the instance of UI
		UI.dataList.addLast(new int[20][20]);

		if(isServer)
			ui.setData(new int[20][20],25);
		ui.setVisible(true);				// set the ui
	}
}
