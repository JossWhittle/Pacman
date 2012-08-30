import java.awt.image.BufferedImage;

public abstract class Entity {

	// Constants
	private static final double MOVE_SPEED = 0.08, WALK_CYCLE = 500;
	public static final double LEFT = -1.0, RIGHT = 1.0, FORWARD = 1.0,
			BACK = -1.0;

	public static final int IMG_MOVE_UP = 0, IMG_MOVE_DOWN = 1,
			IMG_MOVE_LEFT = 2, IMG_MOVE_RIGHT = 3, IMG_PANIC = 4, IMG_DIE = 5,
			IMG_EYE_TOP = 6, IMG_EYE_BOTTOM = 7, IMG_EYE_LEFT = 8,
			IMG_EYE_RIGHT = 9;

	public static final int MODE_CHASE = 0, MODE_SCATTER = 1,
			MODE_FRIGHTENED = 3, MODE_DYING = 4, MODE_DEAD = 5;

	protected static final int DIR_UP = 0, DIR_LEFT = 1, DIR_DOWN = 2,
			DIR_RIGHT = 3;

	// Members
	protected double m_x, m_y, m_rot;
	protected double m_homeX, m_homeY, m_targetX, m_targetY, m_spawnX,
			m_spawnY, m_goalX, m_goalY;
	protected int m_direction;

	private BufferedImage[][] m_img;
	private int m_walkIndex = 0, m_directionIndex = IMG_MOVE_LEFT;
	protected int m_mode = MODE_CHASE;
	private double m_walkCount = 0.0;

	public boolean m_visible = false;

	/**
	 * Abstract Constructor
	 * 
	 * @param img
	 *            Array of walk images
	 * @param eimg
	 *            Array of action images
	 */
	protected void init(BufferedImage[] img, BufferedImage[] eimg, int spawnX,
			int spawnY) {
		m_spawnX = spawnX + 0.5;
		m_spawnY = spawnY + 0.5;

		m_x = m_spawnX;
		m_y = m_spawnY;

		m_direction = DIR_LEFT;
		m_goalX = m_x - 1;
		m_goalY = m_y;

		m_img = new BufferedImage[10][];

		int j = 0;
		for (int i = 0; i < img.length; i += 2) {
			m_img[j] = new BufferedImage[2];
			m_img[j][0] = img[i];
			m_img[j][1] = img[i + 1];
			j++;
		}

		for (int i = 0; i < 4; i += 2) {
			m_img[j] = new BufferedImage[2];
			m_img[j][0] = eimg[i];
			m_img[j][1] = eimg[i + 1];
			j++;
		}

		for (int i = 4; i < eimg.length; i++) {
			m_img[j] = new BufferedImage[1];
			m_img[j][0] = eimg[i];
			j++;
		}

	}

	/**
	 * Updates the ai and moves it to it's new position
	 * 
	 * @param timePassed
	 *            The amount of time since the last call
	 */
	public void update(long timePassed, int[][] map, Player p) {
		double ticks = (double) timePassed / Game.FPS;

		m_visible = false;

		m_walkCount += timePassed;
		if (m_walkCount > WALK_CYCLE) {
			m_walkCount -= WALK_CYCLE;
			m_walkIndex = (m_walkIndex + 1) % 2;
		}
		
		boolean pathfind = false;
		double moveStep = (MOVE_SPEED) * ticks;
		if (m_direction == DIR_UP) {
			if (m_y < m_goalY) {
				m_y = m_goalY;
				pathfind = true;
			} else {
				m_y -= moveStep;
			}
		} else if (m_direction == DIR_LEFT) {
			if (m_x < m_goalX) {
				m_x = m_goalX;
				pathfind = true;
			} else {
				m_x -= moveStep;
			}
		} else if (m_direction == DIR_DOWN) {
			if (m_y > m_goalY) {
				m_y = m_goalY;
				pathfind = true;
			} else {
				m_y += moveStep;
			}
		} else if (m_direction == DIR_RIGHT) {
			if (m_x > m_goalX) {
				m_x = m_goalX;
				pathfind = true;
			} else {
				m_x += moveStep;
			}
		}
		
		if (pathfind) {
			int blockX = (int)Math.floor(m_x), blockY = (int)Math.floor(m_y);
			
			int olddir = m_direction;
			double mindist = -1;
			
			if (olddir != DIR_DOWN && canMove(blockX, blockY-1,map) && map[blockX][blockY] != Map.PATH_SPECIAL) {
				double dist = distanceToTarget(blockX, blockY-1);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_UP;
					m_goalY -= 1;
				}
			}
			
			if (olddir != DIR_RIGHT && canMove(blockX-1, blockY,map)) {
				double dist = distanceToTarget(blockX-1, blockY);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_LEFT;
					m_goalX -= 1;
				}
			}
			
			if (olddir != DIR_UP && canMove(blockX, blockY+1,map)) {
				double dist = distanceToTarget(blockX, blockY+1);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_DOWN;
					m_goalY += 1;
				}
			}
			
			if (olddir != DIR_LEFT && canMove(blockX+1, blockY,map)) {
				double dist = distanceToTarget(blockX+1, blockY);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_RIGHT;
					m_goalX += 1;
				}
			}	
			
		}

		tick(timePassed, ticks, p);
	}

	/**
	 * Update method to be implemented in extension
	 * 
	 * @param timePassed
	 *            Amount of time since last call
	 * @param ticks
	 *            The fractional number of frames that should have occurred
	 * @param p The player to chase
	 */
	protected abstract void tick(long timePassed, double ticks, Player p);

	/**
	 * Determines if the ai can move to the desired coord
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 * @return Whether it is possible to move
	 */
	protected boolean canMove(int x, int y, int[][] map) {
		return (map[x][y] > 0);
	}

	/**
	 * Gets the distance to target from a given x,y
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 * @return The distance
	 */
	protected double distanceToTarget(double x, double y) {
		double distX = m_targetX + 0.5 - x;
		double distY = m_targetY + 0.5 - y;
		return Math.sqrt((distX * distX) + (distY * distY));
	}

	/**
	 * Gets the correct image to render
	 * 
	 * @return The image
	 */
	public BufferedImage getImg() {
		if (m_mode == MODE_CHASE || m_mode == MODE_SCATTER) {
			return m_img[m_directionIndex][m_walkIndex];
		} else if (m_mode == MODE_FRIGHTENED) {
			return m_img[IMG_PANIC][m_walkIndex];
		} else if (m_mode == MODE_DYING) {
			return m_img[IMG_DIE][m_walkIndex];
		} else if (m_mode == MODE_DEAD) {
			return m_img[m_directionIndex + IMG_EYE_TOP][0];
		}
		return null;
	}

	/**
	 * Sets the home grid for the ai
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 */
	public void setHome(int x, int y) {
		m_homeX = x;
		m_homeY = y;
	}

	/**
	 * Sets the traget grid for the ai
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 */
	public void setTarget(int x, int y) {
		m_targetX = x;
		m_targetY = y;
	}

	/**
	 * Targets the home grid
	 */
	public void targetHome() {
		m_targetX = m_homeX;
		m_targetY = m_homeY;
	}

	/**
	 * Sets the mode
	 */
	public void chaseMode() {
		m_mode = MODE_CHASE;
	}

	/**
	 * Sets the mode
	 */
	public void scatterMode() {
		m_mode = MODE_SCATTER;
	}

	/**
	 * Sets the mode
	 */
	public void frightenedMode() {
		m_mode = MODE_FRIGHTENED;
	}

	/**
	 * Sets the mode
	 */
	public void dyingMode() {
		m_mode = MODE_DYING;
	}

	/**
	 * Sets the mode
	 */
	public void deadMode() {
		m_mode = MODE_DEAD;
	}

}
