/**
 * Handles the location and movement of the player (camera)
 * 
 * @author Joss
 * 
 */
public class Player {

	// Constants
	private static final float MOVE_SPEED = 0.08f, ROT_SPEED = 4.0f,
			RADIUS = 0.35f, ROT_ACC = 0.8f;
	public static final float LEFT = -1.0f, RIGHT = 1.0f, STRAFE_LEFT = -1.0f, STRAFE_RIGHT = 1.0f, FORWARD = 1.0f,
			BACK = -1.0f, STRIDE_LENGTH = 4f, STRIDE_HEIGHT = 10f, STRIDE_OFFSET = 0f;

	// Members
	private float m_x, m_y, m_rot, m_turn = 0f, m_speed = 0f, m_rotspeed = 0f,
			m_oldturn = 0f, m_stride, m_strideX = 0f, m_strafe = 0f;
	
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

		// Movement acceleration
		float moveStep = MOVE_SPEED * ticks;
		
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

		m_strideX += moveStep * Math.sqrt((dx * dx) + (dy * dy));
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
}
