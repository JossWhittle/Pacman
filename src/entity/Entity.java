import java.awt.image.BufferedImage;

public abstract class Entity {

	// Constants
	private static final float MOVE_SPEED = 0.08f, WALK_CYCLE = 200f;
	public static final float LEFT = -1.0f, RIGHT = 1.0f, FORWARD = 1.0f,
			BACK = -1.0f;

	public static final int IMG_MOVE_DOWN = 0, IMG_MOVE_LEFT = 2, IMG_MOVE_RIGHT = 1, IMG_MOVE_UP = 3;

	public static final int MODE_CHASE = 0, MODE_SCATTER = 1,
			MODE_FRIGHTENED = 3, MODE_DYING = 4, MODE_DEAD = 5;

	protected static final int DIR_R = 1, DIR_U = 0, DIR_L = 3,
			DIR_D = 2;

	// Members
	protected float m_x, m_y, m_rot;
	protected float m_homeX, m_homeY, m_targetX, m_targetY, m_spawnX,
			m_spawnY, m_goalX, m_goalY;
	protected int m_direction;
	
	protected int[][] m_map;

	private BufferedImage[][] m_img;
	private int m_walkIndex = 0, m_directionIndex = IMG_MOVE_DOWN;
	protected int m_mode = MODE_CHASE;
	private float m_walkCount = 0.0f;

	public boolean m_visible = false;

	/**
	 * Abstract Constructor
	 * 
	 * @param img
	 *            Array of walk images
	 * @param eimg
	 *            Array of action images
	 */
	protected void init(BufferedImage[][] img, int spawnX,
			int spawnY, int[][] map) {
		m_map = map;
		m_spawnX = spawnX + 0.5f;
		m_spawnY = spawnY + 0.5f;

		m_x = m_spawnX;
		m_y = m_spawnY;

		m_direction = DIR_U;
		m_goalX = m_x - 1;
		m_goalY = m_y;

		m_img = img;

	}

	/**
	 * Updates the ai and moves it to it's new position
	 * 
	 * @param timePassed
	 *            The amount of time since the last call
	 */
	public void update(int timePassed, Player p) {
		float ticks = (float) timePassed / Game.FPS;

		m_visible = false;

		m_walkCount += timePassed;
		if (m_walkCount > WALK_CYCLE) {
			m_walkCount -= WALK_CYCLE;
			m_walkIndex = (m_walkIndex + 1) % 2;
		}
		
		boolean pathfind = false;
		float moveStep = (MOVE_SPEED) * ticks;
		if (m_direction == DIR_R) {
			if (m_y < m_goalY) {
				m_y = m_goalY;
				pathfind = true;
			} else {
				m_y -= moveStep;
			}
		} else if (m_direction == DIR_U) {
			if (m_x < m_goalX) {
				m_x = m_goalX;
				pathfind = true;
			} else {
				m_x -= moveStep;
			}
		} else if (m_direction == DIR_L) {
			if (m_y > m_goalY) {
				m_y = m_goalY;
				pathfind = true;
			} else {
				m_y += moveStep;
			}
		} else if (m_direction == DIR_D) {
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
			float mindist = -1;
			
			if (olddir != DIR_L && canMove(blockX, blockY-1,m_map) && m_map[blockX][blockY] != Map.PATH_SPECIAL) {
				float dist = distanceToTarget(blockX, blockY-1);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_R;
					m_goalY -= 1;
				}
			}
			
			if (olddir != DIR_D && canMove(blockX-1, blockY,m_map)) {
				float dist = distanceToTarget(blockX-1, blockY);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_U;
					m_goalX -= 1;
				}
			}
			
			if (olddir != DIR_R && canMove(blockX, blockY+1,m_map)) {
				float dist = distanceToTarget(blockX, blockY+1);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_L;
					m_goalY += 1;
				}
			}
			
			if (olddir != DIR_U && canMove(blockX+1, blockY,m_map)) {
				float dist = distanceToTarget(blockX+1, blockY);
				if (mindist == -1 || dist < mindist) {
					mindist = dist;
					m_direction = DIR_D;
					m_goalX += 1;
				}
			}	
			
		}
		
		if (p.getRot() > 135 && p.getRot() <= 225) {
			// Looking up
			//System.out.println("UP " + m_direction);
			
			if (m_direction == DIR_L) {
				m_directionIndex = IMG_MOVE_LEFT;
			} else if (m_direction == DIR_U) {
				m_directionIndex = IMG_MOVE_UP;
			} else if (m_direction == DIR_D) {
				m_directionIndex = IMG_MOVE_DOWN;
			} else if (m_direction == DIR_R) {
				m_directionIndex = IMG_MOVE_RIGHT;
			}
		} else if (p.getRot() > 225 && p.getRot() <= 315) {
			// Looking right
			//System.out.println("RIGHT");
			
			if (m_direction == DIR_L) {
				m_directionIndex = IMG_MOVE_DOWN;
			} else if (m_direction == DIR_D) {
				m_directionIndex = IMG_MOVE_RIGHT;
			} else if (m_direction == DIR_U) {
				m_directionIndex = IMG_MOVE_LEFT;
			} else if (m_direction == DIR_R) {
				m_directionIndex = IMG_MOVE_UP;
			}
		} else if (p.getRot() > 45 && p.getRot() <= 135) {
			// Looking down
			//System.out.println("LEFT");
			
			if (m_direction == DIR_L) {
				m_directionIndex = IMG_MOVE_UP;
			} else if (m_direction == DIR_U) {
				m_directionIndex = IMG_MOVE_RIGHT;
			} else if (m_direction == DIR_D) {
				m_directionIndex = IMG_MOVE_LEFT;
			} else if (m_direction == DIR_R) {
				m_directionIndex = IMG_MOVE_DOWN;
			}
		} else if (p.getRot() > 315 || p.getRot() <= 45) {
			// Looking left
			//System.out.println("DOWN");
			
			if (m_direction == DIR_L) {
				m_directionIndex = IMG_MOVE_RIGHT;
			} else if (m_direction == DIR_U) {
				m_directionIndex = IMG_MOVE_DOWN;
			} else if (m_direction == DIR_D) {
				m_directionIndex = IMG_MOVE_UP;
			} else if (m_direction == DIR_R) {
				m_directionIndex = IMG_MOVE_LEFT;
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
	protected abstract void tick(int timePassed, float ticks, Player p);

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
	protected float distanceToTarget(float x, float y) {
		float distX = m_targetX + 0.5f - x;
		float distY = m_targetY + 0.5f - y;
		return (float) Math.sqrt((distX * distX) + (distY * distY));
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
			//return m_img[IMG_PANIC][m_walkIndex];
		} else if (m_mode == MODE_DYING) {
			//return m_img[IMG_DIE][m_walkIndex];
		} else if (m_mode == MODE_DEAD) {
			//return m_img[m_directionIndex + IMG_EYE_TOP][0];
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
