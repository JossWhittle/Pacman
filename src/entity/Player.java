/**
 * Handles the location and movement of the player (camera)
 * 
 * @author Joss
 * 
 */
public class Player {

	// Constants
	private static final double MOVE_SPEED = 0.08, ROT_SPEED = 4.0,
			RADIUS = 0.35, ROT_ACC = 0.8;
	public static final double LEFT = -1.0, RIGHT = 1.0, FORWARD = 1.0,
			BACK = -1.0;

	// Members
	private double m_x, m_y, m_rot, m_turn = 0, m_speed = 0, m_rotspeed = 0,
			m_oldturn = 0;

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
	public Player(double x, double y, double r) {
		m_x = x;
		m_y = y;
		m_rot = r;
	}

	/**
	 * Updates the player and moves it to it's new position
	 * 
	 * @param timePassed
	 *            The amount of time since the last call
	 */
	public void update(long timePassed, int[][] map) {
		double ticks = (double) timePassed / Game.FPS;

		// Movement acceleration
		double moveStep = (m_speed * MOVE_SPEED) * ticks;

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

		double x = m_x + Math.cos(Math.toRadians(m_rot)) * moveStep;
		double y = m_y + Math.sin(Math.toRadians(m_rot)) * moveStep;

		int width = map.length, height = map[0].length;
		if (!(x < 0 || x >= width || y < 0 || y >= height)) {

			int blockX = (int) Math.floor(x), blockY = (int) Math.floor(y);

			if (canMove(blockX, blockY, map)) {

				boolean blockTop = canMove(blockX, blockY - 1, map);
				boolean blockBottom = canMove(blockX, blockY + 1, map);
				boolean blockLeft = canMove(blockX - 1, blockY, map);
				boolean blockRight = canMove(blockX + 1, blockY, map);

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
	private boolean canMove(int x, int y, int[][] map) {
		return (map[x][y] < Map.SOLID_BLOCK);
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
	public void setTurn(double v) {
		m_turn = v;
	}

	/**
	 * Gets Turn
	 * 
	 * @return The value
	 */
	public double getTurn() {
		return m_turn;
	}

	/**
	 * Sets Speed
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setSpeed(double v) {
		m_speed = v;
	}

	/**
	 * Gets Speed
	 * 
	 * @return The value
	 */
	public double getSpeed() {
		return m_speed;
	}

	/**
	 * Sets X
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setX(double v) {
		m_x = v;
	}

	/**
	 * Gets X
	 * 
	 * @return The value
	 */
	public double getX() {
		return m_x;
	}

	/**
	 * Sets Y
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setY(double v) {
		m_y = v;
	}

	/**
	 * Gets Y
	 * 
	 * @return The value
	 */
	public double getY() {
		return m_y;
	}

	/**
	 * Sets Rot
	 * 
	 * @param v
	 *            The desired value
	 */
	public void setRot(double v) {
		m_rot = v;
	}

	/**
	 * Gets Rot
	 * 
	 * @return The value
	 */
	public double getRot() {
		return m_rot;
	}
}
