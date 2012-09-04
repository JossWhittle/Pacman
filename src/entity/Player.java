/**
 * Handles the location and movement of the player (camera)
 * 
 * @author Joss
 * 
 */
public class Player {

	// Constants
	private static final float MOVE_SPEED = 0.05f, ROT_SPEED = 2.0f,
			RADIUS = 0.35f, ROT_ACC = 0.1f, SPRINT_SPEED = 0.08f, STRIDE_LENGTH = 5f, STRIDE_HEIGHT = 5f, STRIDE_OFFSET = 0f,
					STAMINA = 5.0f, STAMINA_BLEED = 0.1f;;
	
	public static final float LEFT = -1.0f, RIGHT = 1.0f, STRAFE_LEFT = -1.0f, STRAFE_RIGHT = 1.0f, FORWARD = 1.0f,
			BACK = -1.0f; 
	
	private static final int MAX_AMMO = 100;
	
	public static final int REL_MOVE_UP = 0, REL_MOVE_LEFT = 1, REL_MOVE_DOWN = 2, REL_MOVE_RIGHT = 3;

	// Members
	private float m_x, m_y, m_rot, m_turn = 0f, m_speed = 0f, m_rotspeed = 0f,
			m_oldturn = 0f, m_stride, m_strideX = 0f, m_strafe = 0f, m_stamina = 0f;
	
	private int m_relDir = 0;
	
	private boolean m_sprint = false;
	
	private int m_ammo = 0;
	
	private Block[][] m_map;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 * @param r
	 *            The rotation angle in degrees
	 */
	public Player(float x, float y, float r, Block[][] map) {
		m_x = x;
		m_y = y;
		m_rot = r;
		
		m_map = map;
	}

	/**
	 * Updates the player and moves it to it's new position
	 * 
	 * @param timePassed
	 *            The amount of time since the last call
	 */
	public void update(int timePassed) {
		float ticks = (float) timePassed / Game.FPS;
		
		//System.out.println(m_stride);

		// Rotation acceleration (smoothing)
		if (m_turn != 0) {
			m_rotspeed += (Math.abs(m_turn) * ROT_ACC);
			if (m_rotspeed > ROT_SPEED) {
				m_rotspeed = ROT_SPEED;
			}
		} else if (m_turn == 0 || m_turn != m_oldturn) {
			m_rotspeed -= ROT_ACC * 2.0;
			if (m_rotspeed < 0) {
				m_rotspeed = 0;
			}
		}
		m_oldturn = m_turn;
		m_rot += (m_turn * m_rotspeed) * ticks;
		
		if (m_rot < 0) {
			m_rot += 360;
		} else if (m_rot > 360) {
			m_rot -= 360;
		}
		
		float dx = 0, dy = 0; 
		
		if (m_speed == FORWARD) {
			dx += (float) Math.cos(Math.toRadians(m_rot));
			dy += (float) Math.sin(Math.toRadians(m_rot));
		} else if (m_speed == BACK) {
			dx -= (float) Math.cos(Math.toRadians(m_rot));
			dy -= (float) Math.sin(Math.toRadians(m_rot));
		}
		
		if (m_strafe == STRAFE_LEFT) {
			dx += (float) Math.cos(Math.toRadians(m_rot-90.0f));
			dy += (float) Math.sin(Math.toRadians(m_rot-90.0f));
		} else if (m_strafe == STRAFE_RIGHT) {
			dx += (float) Math.cos(Math.toRadians(m_rot+90.0f));
			dy += (float) Math.sin(Math.toRadians(m_rot+90.0f));
		}
		
		// Movement acceleration
		float moveStep = (m_sprint && (m_stamina < STAMINA) ? SPRINT_SPEED : MOVE_SPEED) * ticks;
		float delta = (float) (moveStep * Math.sqrt((dx * dx) + (dy * dy)));
		
		if (m_sprint) { 
			m_stamina += delta;
			if (m_stamina > STAMINA) m_stamina = STAMINA;
		} else {
			m_stamina -= STAMINA_BLEED;
			if (m_stamina < 0) m_stamina = 0;
		}
		
		m_strideX += delta;
		m_stride = (float) (STRIDE_HEIGHT * Math.sin(STRIDE_LENGTH * m_strideX) - STRIDE_OFFSET);
		
		float x = (float) (m_x + (dx * moveStep));
		float y = (float) (m_y + (dy * moveStep));

		int width = m_map.length, height = m_map[0].length;
		if (!(x < 0 || x >= width || y < 0 || y >= height)) {

			int blockX = (int) Math.floor(x), blockY = (int) Math.floor(y);

			if (canMove(blockX, blockY)) {

				boolean blockTop = canMove(blockX, blockY - 1);
				boolean blockBottom = canMove(blockX, blockY + 1);
				boolean blockLeft = canMove(blockX - 1, blockY);
				boolean blockRight = canMove(blockX + 1, blockY);

				if (!blockTop && y - blockY < RADIUS) {
					y = blockY + RADIUS;
				}

				if (!blockBottom && blockY + 1 - y < RADIUS) {
					y = blockY + 1 - RADIUS;
				}

				if (!blockLeft && x - blockX < RADIUS) {
					x = blockX + RADIUS;
				}

				if (!blockRight && blockX + 1 - x < RADIUS) {
					x = blockX + 1 - RADIUS;
				}

				m_x = x;
				m_y = y;
			}
		}
		
		if (dy >= 0) { 
			if (dx < 0 && dx < -dy) {
				m_relDir = REL_MOVE_LEFT;
			} else if (dx > 0 && dx > dy) {
				m_relDir = REL_MOVE_RIGHT;
			} else {
				m_relDir = REL_MOVE_DOWN;
			}
		} else {
			if (dx < 0 && dx < dy) {
				m_relDir = REL_MOVE_LEFT;
			} else if (dx > 0 && dx > -dy) {
				m_relDir = REL_MOVE_RIGHT;
			} else {
				m_relDir = REL_MOVE_UP;
			}
		}
	}

	/**
	 * Determines if the player can move to the desired coord
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 * @param map
	 *            The map to check on
	 * @return Whether it is possible to move
	 */
	private boolean canMove(int x, int y) {
		return (m_map[x][y] == null);
	}
	
	public float getSprintRatio() {
		return 1.0f - (m_stamina / STAMINA);
	}
	
	public boolean isSprinting() {
		return m_sprint;
	}
	
	public void setSprint(boolean s) {
		m_sprint = s;
	}
	
	public int getRelDir() {
		return m_relDir;
	}

	/**
	 * Tells the players view to rotate left
	 */
	public void turnLeft() {
		m_turn = LEFT;
	}

	/**
	 * Tells the players view to rotate right
	 */
	public void turnRight() {
		m_turn = RIGHT;
	}

	/**
	 * Tells the players view to move forward
	 */
	public void moveForward() {
		m_speed = FORWARD;
	}

	/**
	 * Tells the players view to move back
	 */
	public void moveBack() {
		m_speed = BACK;
	}

	/**
	 * Sets Turn
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setTurn(float v) {
		m_turn = v;
	}

	/**
	 * Gets Turn
	 * 
	 * @return The value
	 */
	public float getTurn() {
		return m_turn;
	}

	/**
	 * Sets Speed
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setSpeed(float v) {
		m_speed = v;
	}

	/**
	 * Gets Speed
	 * 
	 * @return The value
	 */
	public float getSpeed() {
		return m_speed;
	}

	/**
	 * Sets X
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setX(float v) {
		m_x = v;
	}

	/**
	 * Gets X
	 * 
	 * @return The value
	 */
	public float getX() {
		return m_x;
	}

	/**
	 * Sets Y
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setY(float v) {
		m_y = v;
	}

	/**
	 * Gets Y
	 * 
	 * @return The value
	 */
	public float getY() {
		return m_y;
	}

	/**
	 * Sets Rot
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setRot(float v) {
		m_rot = v;
	}

	/**
	 * Gets Rot
	 * 
	 * @return The value
	 */
	public float getRot() {
		return m_rot;
	}

	public float getStride() {
		return m_stride;
	}

	public void setStrafe(float v) {
		m_strafe = v;		
	}

	public void giveAmmo(int ammoDrop) {
		m_ammo += ammoDrop;
		if (m_ammo > MAX_AMMO) {
			m_ammo = MAX_AMMO;
		}
	}
	
	public int getAmmo() {
		return m_ammo;
	}
	
	public float getAmmoRatio() {
		return (float)m_ammo / (float)MAX_AMMO;
	}
}
