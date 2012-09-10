import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The interface for the program to communicate with the database by
 * 
 * @author Joss Whittle-A5-A6
 * 
 */
public class DatabaseManager {

	// Constants
	private static final int HEX = 16;
	private static final int MD5_OFFSET = 10000;

	// Members
	private static Connection m_conn;
	private static boolean m_connected = false;

	/**
	 * Opens a connection to the SQL database
	 */
	public static void Open() {
		try {
			Class.forName("org.h2.Driver");
			m_conn = DriverManager.getConnection("jdbc:h2:LV426/config","sa", "");
			m_connected = true;

			ResultSet res = SendSelectQuery("SHOW TABLES;");
			while (res.next()) {
				System.out.println("" + res.getRow() + " "
						+ res.getString("TABLE_NAME"));
			}
			res.close();
		} catch (Exception e) {
			System.out
					.println("Internal Database Error... Program already running?");
			if (JOptionPane.showConfirmDialog(null,
					"Oh no! Your database is broken.\nYou can either "
							+ "rebuild the database,\nlosing your data;\nor I "
							+ "can just stop running, \nand you can work out "
							+ "what went wrong...\n\nDo you want to rebuild?",
					"Database Recovery", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				File f1 = new File("LV426/config.h2.db");
				File f2 = new File("LV426/config.lock.db");
				File f3 = new File("LV426/config.trace.db");

				f1.delete();
				f2.delete();
				f3.delete();

				Open();
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * Runs a simple query on the database and returns a boolean about the
	 * transaction
	 * 
	 * @param q
	 *            The query to run
	 * @param params
	 *            The list of protected parameters to use
	 * @return Returns true is the transaction was successful
	 */
	public static boolean SendQuery(String q, Object[] params) {
		if (!m_connected)
			return false;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1] instanceof String) {
					stm.setString(i, (String) params[i - 1]);
				} else if (params[i - 1] instanceof Integer) {
					stm.setInt(i, (Integer) params[i - 1]);
				}
			}
			return stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Runs a simple query on the database and returns a set of results
	 * 
	 * @param q
	 *            The query to run
	 * @param params
	 *            The list of protected parameters to use
	 * @return The set of results gathered from the database
	 */
	public static ResultSet SendSelectQuery(String q, Object[] params) {
		if (!m_connected)
			return null;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1] instanceof String) {
					stm.setString(i, (String) params[i - 1]);
				} else if (params[i - 1] instanceof Integer) {
					stm.setInt(i, (Integer) params[i - 1]);
				}
			}
			return stm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Runs a simple query on the database and returns a integer about the
	 * transaction
	 * 
	 * @param q
	 *            The query to run
	 * @param params
	 *            The list of protected parameters to use
	 * @return And integer representing the number of rows edited/deleted
	 */
	public static int SendUpdateQuery(String q, Object[] params) {
		if (!m_connected)
			return 0;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1] instanceof String) {
					stm.setString(i, (String) params[i - 1]);
				} else if (params[i - 1] instanceof Integer) {
					stm.setInt(i, (Integer) params[i - 1]);
				}
			}
			return stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Runs a simple query on the database and returns a boolean about the
	 * transaction
	 * 
	 * @param q
	 *            The query to run
	 * @return Returns true is the transaction was successful
	 */
	public static boolean SendQuery(String q) {
		if (!m_connected)
			return false;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			return stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Runs a simple query on the database and returns a set of results
	 * 
	 * @param q
	 *            The query to run
	 * @return The set of results gathered from the database
	 */
	public static ResultSet SendSelectQuery(String q) {
		if (!m_connected)
			return null;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			return stm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Runs a simple query on the database and returns a integer about the
	 * transaction
	 * 
	 * @param q
	 *            The query to run
	 * @return And integer representing the number of rows edited/deleted
	 */
	public static int SendUpdateQuery(String q) {
		if (!m_connected)
			return 0;
		try {
			PreparedStatement stm = m_conn.prepareStatement(q);
			return stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Closes the connection to the database
	 */
	public static void Close() {
		try {
			m_conn.close();
			m_connected = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a unique string off of a given seed (MD5 Sum)
	 * 
	 * @param a
	 *            The seed
	 * @return The unique string
	 */
	public static String GenerateUniqueId(String a) {
		try {
			a += "" + ((Math.random() * Math.random()) * MD5_OFFSET)
					+ System.currentTimeMillis();
			return new BigInteger(1, MessageDigest.getInstance("MD5").digest(
					a.getBytes())).toString(HEX);
		} catch (Exception ex) {

		}
		return null;
	}
	
	public static String getSetting(String id, String key) {
		try {
			ResultSet res = DatabaseManager.SendSelectQuery("SELECT `val` FROM `Settings` WHERE `id`='"+id+"' AND `key`='"+key+"' LIMIT 1");
			while (res.next()) {
				System.out.println("Setting \"" + key + "\" => " + res.getString("val"));
				return res.getString("val");
			}
		} catch (Exception ex) {
			
		}
		
		return null;
	}

	public static Score PullScore(String q) {
		try {
			ResultSet res = SendSelectQuery(q);
			while (res.next()) {
				return new Score(res);
			}
		} catch (Exception ex) {

		}
		return null;
	}

	public static ArrayList<Score> PullScores(String q) {
		try {
			ArrayList<Score> result = new ArrayList<Score>();

			ResultSet res = SendSelectQuery(q);
			while (res.next()) {
				result.add(new Score(res));
			}

			return result;
		} catch (Exception ex) {

		}
		return null;
	}
}
