
public class KidPaint {
	static String name;
	public static void main(String[] args) {
		LoginUI loginUI = new LoginUI();
		name = loginUI.getName();
		UI ui = UI.getInstance();			// get the instance of UI
		ui.setData(new int[50][50], 20);	// set the data array and block size. comment this statement to use the default data array and block size.
		ui.setVisible(true);				// set the ui
	}
}
