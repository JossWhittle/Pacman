import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The Pseudo-3D renderer
 * 
 * @author Joss
 * 
 */
public class RayCaster extends SimpleDrawable {

	// Constants

	// Members
	private RenderQueue m_renderQueue;
	private BufferedImage[][][] m_texWalls;
	private BufferedImage[] m_texPills;

	/**
	 * Constructor
	 */
	public RayCaster(BufferedImage[][][] texWalls, BufferedImage[] texPills) {
		super(0, 0, Settings.RES_WIDTH, Settings.RES_HEIGHT, 0, 0);
		m_texWalls = texWalls;
		m_texPills = texPills;
		
		m_renderQueue = new RenderQueue();
	}

	/**
	 * Updates the ray caster and prepares the render queue
	 * 
	 * @param player
	 *            The player (camera) object
	 * @param walls
	 *            The array mapping the walls
	 * @param sprites
	 *            The array mapping the sprites
	 * @param entities
	 *            The arraylist of game entities
	 */
	public void update(Player player, int[][] walls, int[][] sprites, ArrayList<Entity> entities) {
		m_renderQueue.clear();
		
		
	}

	/**
	 * Draws the result of the ray casting
	 * 
	 * @param g
	 *            The graphics object passed by the engine
	 */
	public void draw(Graphics2D g) {
		if (m_oy != 0.0) {
			AffineTransform t = new AffineTransform();
			t.translate(0.0, m_oy);
			g.setTransform(t);
		}

		m_renderQueue.draw(g);
		m_renderQueue.clear();

		g.setTransform(NULL_TRANSFORM);
	}

}
