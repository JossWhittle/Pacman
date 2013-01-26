import java.sql.ResultSet;

public class Stitch {

	// Constants

	// Members
	public static int STRIP_WIDTH = 1,

	RES_WIDTH = 640, RES_HEIGHT = 480,

	ZX80 = 1,
	
	TEX_WIDTH = 128;
	
	public static boolean FULL_SCREEN = false;
	
	public static float MOUSE_X  = 0.15f, FIELD_OF_VIEW = 60.0f;

	public static void loadSettings() {
		STRIP_WIDTH = Integer.parseInt(DatabaseManager.getSetting("setting", "strip_width"));
		RES_WIDTH = Integer.parseInt(DatabaseManager.getSetting("setting", "res_x"));
		RES_HEIGHT = Integer.parseInt(DatabaseManager.getSetting("setting", "res_y"));
		
		if (DatabaseManager.getSetting("setting", "aspect").equals("16:9")) {
			RES_WIDTH  = 1280;
			RES_HEIGHT = 720;
		} else {
			RES_WIDTH  = 1024;
			RES_HEIGHT = 768;
		}
		
		MOUSE_X = Float.parseFloat(DatabaseManager.getSetting("setting", "mouse"));
		FIELD_OF_VIEW = Float.parseFloat(DatabaseManager.getSetting("setting", "fov"));
		FULL_SCREEN = DatabaseManager.getSetting("setting", "full_screen").equals("yes");
	}

}
