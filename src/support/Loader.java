import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;

/**
 * Static helper class for loading resources
 * 
 * @author Joss
 * 
 */
public class Loader {

	// Constants
	private static final int CONTENT_LIMIT = 64;

	// Members
	private static int m_index = 0;
	private static BufferedImage[] m_content = new BufferedImage[CONTENT_LIMIT];

	private static int m_indexSound = 0;
	private static AudioInputStream[] m_contentSound = new AudioInputStream[CONTENT_LIMIT];

	/**
	 * Gets the loaded image
	 * 
	 * @param id
	 *            The id of the image
	 * @return The image
	 */
	public static BufferedImage getImage(int id) {
		try {
			return m_content[id];
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the image at the given file path
	 * 
	 * @param dir
	 *            The path to the desired file
	 * @return Returns the id of the loaded image
	 */
	public static int loadImage(String dir) {
		System.out.print("Loading \""+dir+"\" ");
		try {
			Image i = (Image) new ImageIcon(Loader.class.getResource(dir))
					.getImage();
			int w = i.getWidth(null);
			int h = i.getHeight(null);

			BufferedImage ii = new BufferedImage((int) w, (int) h,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = ii.createGraphics();
			g.setRenderingHints(Game.getRH());
			g.drawImage(i, 0, 0, w, h, null);

			m_content[m_index] = ii;
			m_index++;
			
			System.out.println("[Success]");
			return m_index - 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("[Fail]");
		}
		return -1;
	}

	/**
	 * Splits an image into an array of sprites
	 * 
	 * @param img
	 *            The image to split
	 * @param w
	 *            The number of horizontal sections
	 * @param h
	 *            The number of vertical sections
	 * @return The array of sub images
	 */
	public static BufferedImage[][] splitImage(int img, int w, int h) {
		return splitImage(Loader.getImage(img), w, h);
	}

	/**
	 * Splits an image into an array of sprites
	 * 
	 * @param img
	 *            The image to split
	 * @param w
	 *            The number of horizontal sections
	 * @param h
	 *            The number of vertical sections
	 * @return The array of sub images
	 */
	public static BufferedImage[][] splitImage(BufferedImage img, int w, int h) {
		BufferedImage[][] map = new BufferedImage[h][w];

		int width = img.getWidth(), height = img.getHeight();
		int n_width = (int) ((double) width / (double) w);
		int n_height = (int) ((double) height / (double) h);

		RenderingHints rh = Game.getRH();

		int xcoord = 0, ycoord = 0;
		for (int x = 0; x < w; x++) {
			xcoord = (int) (x * n_width);

			for (int y = 0; y < h; y++) {
				ycoord = (int) (y * n_height);

				map[y][x] = new BufferedImage(n_width, n_height,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D g = map[y][x].createGraphics();
				g.setRenderingHints(rh);
				g.drawImage(img, -xcoord, -ycoord, width, height, null);
				g.dispose();
			}
		}

		return map;
	}

	/**
	 * Splits an image into an array of sprites
	 * 
	 * @param img
	 *            The image to split
	 * @param w
	 *            The width of the horizontal sections
	 * @return The array of sub images
	 */
	public static BufferedImage[] splitImage(int img, int sw) {
		return splitImage(Loader.getImage(img), sw);
	}

	/**
	 * Splits an image into an array of sprites
	 * 
	 * @param img
	 *            The image to split
	 * @param w
	 *            The width of the horizontal sections
	 * @return The array of sub images
	 */
	public static BufferedImage[] splitImage(BufferedImage img, int sw) {
		int width = img.getWidth(), height = img.getHeight();
		int w = (int) ((double) width / (double) sw);

		BufferedImage[] map = new BufferedImage[w];

		RenderingHints rh = Game.getRH();

		int xcoord = 0;
		for (int x = 0; x < w; x++) {
			xcoord = (int) (x * sw);

			map[x] = new BufferedImage(sw, height, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g = map[x].createGraphics();
			g.setRenderingHints(rh);
			g.drawImage(img, -xcoord, 0, width, height, null);
			g.dispose();
		}

		return map;
	}

	/**
	 * Gets the loaded sound file
	 * 
	 * @param id
	 *            The id of the sound
	 * @return The sound
	 */
	public static AudioInputStream getSound(int id) {
		try {
			return m_contentSound[id];
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the sound file at the given file path
	 * 
	 * @param dir
	 *            The path to the desired file
	 * @return Returns the id of the loaded sound file
	 */
	public static int loadSound(String dir) {
		System.out.print("Loading \""+dir+"\" ");
		
		try {
			AudioInputStream ain = AudioSystem.getAudioInputStream(Loader.class
					.getResource(dir));
			m_contentSound[m_indexSound] = ain;
			m_indexSound++;
			
			System.out.println("[Success]");
			return m_indexSound - 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("[Fail]");
		}
		return -1;
	}
}
