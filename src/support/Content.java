import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Static helper class to help turn raw resources into usable content
 * 
 * @author Joss
 * 
 */
public class Content {

	// Constants

	// Members

	/**
	 * Takes an sprite map of walls and builds a wall array
	 * 
	 * @param img
	 *            The image to split
	 * @param num
	 *            The number of walls contained in the image
	 * @param strip
	 *            The width of the strip to build for each wall
	 * @return The array
	 */
	public static BufferedImage[][][] processWalls(int img, int num, int strip) {
		return processWalls(Loader.getImage(img), num, strip);
	}

	/**
	 * Takes an sprite map of walls and builds a wall array
	 * 
	 * @param img
	 *            The image to split
	 * @param num
	 *            The number of walls contained in the image
	 * @param strip
	 *            The width of the strip to build for each wall
	 * @return The array
	 */
	public static BufferedImage[][][] processWalls(BufferedImage img, int num,
			int strip) {
		BufferedImage[][] smap = Loader.splitImage(img, 2, num);
		BufferedImage[][][] walls = new BufferedImage[num][2][];

		for (int x = 0; x < smap.length; x++) {
			for (int y = 0; y < smap[0].length; y++) {
				walls[x][y] = Loader.splitImage(smap[x][y], strip);
			}
		}

		return walls;
	}

	/**
	 * Loads the specified pill texture
	 * 
	 * @param img
	 *            The loaded image
	 * @param num
	 *            The number of pills it contains
	 * @param sub
	 *            The size of each target image
	 * @return The loaded ghost texture
	 */
	public static BufferedImage[] processPills(int img, int num, int sub) {
		return processPills(Loader.getImage(img), num, sub);
	}

	/**
	 * Loads the specified pill texture
	 * 
	 * @param img
	 *            The loaded image
	 * @param num
	 *            The number of pills it contains
	 * @param sub
	 *            The size of each target image
	 * @return The loaded ghost texture
	 */
	public static BufferedImage[] processPills(BufferedImage img, int num,
			int sub) {
		BufferedImage[][] smap = Loader.splitImage(img, num, 1);
		BufferedImage[] pills = new BufferedImage[num];

		RenderingHints rh = Game.getRH();

		for (int x = 0; x < num; x++) {

			pills[x] = new BufferedImage(sub, sub, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = pills[x].createGraphics();
			g.setRenderingHints(rh);

			g.drawImage(smap[0][x],
					(int) ((sub / 2.0) - (smap[0][x].getWidth() / 2.0)), sub
							- smap[0][x].getHeight(), smap[0][x].getWidth(),
					smap[0][x].getHeight(), null);
			g.dispose();
		}

		return pills;
	}

	/**
	 * Loads the ghost texture into an array of sub images
	 * 
	 * @param img
	 *            The image id
	 * @param w
	 *            The number of horizontal parts
	 * @param h
	 *            The number of vertical parts
	 * @return The loaded ghost texture
	 */
	public static BufferedImage[][] processGhosts(int img, int w, int h) {
		return processGhosts(Loader.getImage(img), w, h);
	}

	/**
	 * Loads the ghost texture into an array of sub images
	 * 
	 * @param img
	 *            The image id
	 * @param w
	 *            The number of horizontal parts
	 * @param h
	 *            The number of vertical parts
	 * @return The loaded ghost texture
	 */
	public static BufferedImage[][] processGhosts(BufferedImage img, int w,
			int h) {
		BufferedImage[][] smap = Loader.splitImage(img, w, h);
		BufferedImage[][] ghosts = new BufferedImage[h][w];

		RenderingHints rh = Game.getRH();

		for (int x = 0; x < smap.length; x++) {
			for (int y = 0; y < smap[0].length; y++) {
				
				ghosts[x][y] = new BufferedImage(40, 40,
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = ghosts[x][y].createGraphics();
				g.setRenderingHints(rh);

				g.drawImage(smap[x][y], 4, 8, smap[x][y].getWidth(),
						smap[x][y].getHeight(), null);
				g.dispose();
			}
		}

		return ghosts;
	}

}
