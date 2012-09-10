import java.sql.ResultSet;

public class Settings {

	// Constants

	// Members
	public static int STRIP_WIDTH = 1,

	RES_WIDTH = 1280, RES_HEIGHT = 720,

	ZX80 = 1,
	
	TEX_WIDTH = 128;
	
	public static boolean FULL_SCREEN = false;
	
	public static float MOUSE_X  = 0.15f, FIELD_OF_VIEW = 60.0f;

	public static void loadSettings() {
		STRIP_WIDTH = Integer.parseInt(DatabaseManager.getSetting("setting", "strip_width"));
		RES_WIDTH = Integer.parseInt(DatabaseManager.getSetting("setting", "res_x"));
		RES_HEIGHT = Integer.parseInt(DatabaseManager.getSetting("setting", "res_y"));
		MOUSE_X = Float.parseFloat(DatabaseManager.getSetting("setting", "mouse"));
		FIELD_OF_VIEW = Float.parseFloat(DatabaseManager.getSetting("setting", "fov"));
		FULL_SCREEN = DatabaseManager.getSetting("setting", "full_screen").equals("yes");
	}

}
