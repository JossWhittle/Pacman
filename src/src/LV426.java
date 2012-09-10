import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class LV426 {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DatabaseManager.Open();
			initDatabase();
			Settings.loadSettings();
			
			GUI_Main window = new GUI_Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void initDatabase() {
		
		String buildSettings = 
				"CREATE TABLE IF NOT EXISTS `Settings` ( "
				+ "`id` varchar(20) NOT NULL, "
				+ "`key` varchar(20) NOT NULL, "
				+ "`val` varchar(20) NOT NULL, "
				+ "PRIMARY KEY (`id`,`key`) );";
		
		String buildScores = 
				"CREATE TABLE IF NOT EXISTS `Scores` ( "
				+ "`id` int(11) NOT NULL AUTO_INCREMENT, "
				+ "`name` varchar(20) NOT NULL, "
				+ "`date` varchar(20) NOT NULL, "
				+ "`score` int(11) NOT NULL, "
				+ "PRIMARY KEY (`id`) );";
		
		String fillSettings = 
				"INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','strip_width','1');"
				+ "INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','full_screen','no');"
				+ "INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','fov','60.0f');"
				+ "INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','mouse','0.15f');"
				+ "INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','res_x','1280');"
				+ "INSERT INTO `Settings` (`id`,`key`,`val`) VALUES ('setting','res_y','720');";
		
		String fillScores = 
				"INSERT INTO `Scores` (`name`,`date`,`score`) VALUES ('Joss','n/a',1024);"
				+ "INSERT INTO `Scores` (`name`,`date`,`score`) VALUES ('Joss','n/a',998);"
				+ "INSERT INTO `Scores` (`name`,`date`,`score`) VALUES ('Penny','n/a',340);";
		
		DatabaseManager.SendQuery("DROP TABLE `Settings`;");
		DatabaseManager.SendQuery("DROP TABLE `Scores`;");
		 

		// Build the db tables if they are not already there
		DatabaseManager.SendQuery(buildSettings);
		DatabaseManager.SendQuery(buildScores);
		
		DatabaseManager.SendQuery(fillSettings);
		DatabaseManager.SendQuery(fillScores);
		
		// Clear the db tables
		/*
		 * DatabaseManager.sendUpdateQuery("DELETE FROM `Settings`");
		 * DatabaseManager.sendUpdateQuery("DELETE FROM `Scores`");
		 */
		
	}
	
	public static void restartApplication() {
		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
		try {
			File currentJar = new File(LV426.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
			
			if (!currentJar.getName().endsWith(".jar"))
				return;

			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			
			try {
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
			
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
		
	}
	
}
