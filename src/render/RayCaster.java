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
	private BufferedImage[][][][] m_texWalls;
	private BufferedImage m_texPill, m_texMega;

	private int RAYS, MAP_W, MAP_H;
	private float VIEW_DIST, VIEW_DIST2, FOV, FOV_H, PI2;

	private float m_playerX, m_playerY, m_playerRot, m_playerStride;
	private int m_frame;
	
	private int[][] m_visSprite, m_sprite_map;
	private Block[][] m_map;

	/**
	 * Constructor
	 */
	public RayCaster(BufferedImage[][][][] texWalls, int texPill, int texMega, Block[][] map, int[][] sprite_map) {
		super(0, 0, Settings.RES_WIDTH, Settings.RES_HEIGHT, 0, 0);
		m_texWalls = texWalls;
		m_texPill = Loader.getImage(texPill);
		m_texMega = Loader.getImage(texMega);

		m_renderQueue = new RenderQueue();

		FOV = (float) Math.toRadians(60.0f);
		FOV_H = FOV / 2.0f;
		PI2 = (float) (Math.PI * 2.0f);

		RAYS = (int) Math.ceil(getW() / Settings.STRIP_WIDTH);
		VIEW_DIST = (getW() / 2.0f) / (float)Math.tan(FOV_H);
		VIEW_DIST2 = VIEW_DIST * VIEW_DIST;
		
		m_map = map;
		m_sprite_map = sprite_map;
		
		MAP_W = map.length;
		MAP_H = map[0].length;
		
		m_visSprite = new int[MAP_W][MAP_H];
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
	public void update(Player player,
			ArrayList<Entity> entities) {
		m_renderQueue.clear();

		m_playerX = player.getX();
		m_playerY = player.getY();
		m_playerRot = player.getRot();
		m_playerStride = player.getStride();

		for (int i = 0; i < RAYS; i++) {
			float rayScreenPos = (float) (((float) (-RAYS) / 2.0f) + i)
					* (float) Settings.STRIP_WIDTH;
			float rayViewDist = (float) Math.sqrt(rayScreenPos * rayScreenPos
					+ VIEW_DIST2);
			float rayAngle = (float) Math.asin(rayScreenPos / rayViewDist);

			castRay((float)Math.toRadians(m_playerRot) + rayAngle, i, entities);
		}

		m_frame = ((m_frame + 1) % 1000000) + 1;
	}

	/**
	 * Casts an individual ray into the world and gets it's bounces
	 * 
	 * @param angle
	 *            The world angle of the ray
	 */
	private void castRay(float angle, int strip, ArrayList<Entity> entities) {

		angle %= PI2;
		if (angle < 0.0f) {
			angle += PI2;
		}

		boolean right = (angle > PI2 * 0.75f || angle < PI2 * 0.25f);
		boolean up = (angle < 0f || angle > (float)Math.PI);

		float angleSin = (float) Math.sin(angle), angleCos = (float) Math.cos(angle);

		float dist = -1f, xHit = -100f, yHit = -100f, texX = 0f;
		int wallX, wallY;

		float slope = angleSin / angleCos;
		float dx = (right ? 1.0f : -1.0f), dy = dx * slope;

		float x = (float) (right ? Math.ceil(m_playerX) : Math.floor(m_playerX));
		float y = m_playerY + ((x - m_playerX) * slope);

		boolean search = true;

		boolean horizontal = true;
		Block block = null;
		while (x < MAP_W && x >= 0f && y < MAP_H && y >= 0f && search) {
			wallX = (int) Math.max(Math.floor(x + (right ? 0f : -1f)), 0f);
			wallY = (int) Math.floor(y);
			
			if (m_map[wallX][wallY] != null) {
				float distX = x - m_playerX;
				float distY = y - m_playerY;
				dist = (distX * distX) + (distY * distY);
				texX = y - wallY;
				if (!right) {
					texX = 1.0f - texX;
				}

				xHit = x;
				yHit = y;
				block = m_map[wallX][wallY];

				// Break
				search = false;
			} else {
				if (m_sprite_map[wallX][wallY] > 0
						&& m_visSprite[wallX][wallY] != m_frame) {
					m_visSprite[wallX][wallY] = m_frame;

					float distX = wallX + 0.5f - m_playerX;
					float distY = wallY + 0.5f - m_playerY;
					float sprite_dist = (float) Math.sqrt((distX * distX)
							+ (distY * distY));
					float sprite_angle = (float) (Math.atan2(distY, distX)
							- Math.toRadians(m_playerRot));
					float sprite_size = (float) (VIEW_DIST
							/ ((float)Math.cos(sprite_angle) * sprite_dist));

					sprite_dist *= (float)Math.cos(Math.toRadians(m_playerRot) - angle);
					m_renderQueue.addJob(new DepthImage(
							(m_sprite_map[wallX][wallY] == 1 ? m_texMega : m_texPill),
							((getW() / 2.0f)
									+ ((float)Math.tan(sprite_angle) * VIEW_DIST) - (sprite_size / 2.0f)) + (sprite_size/4.0f),
							((getH() - sprite_size) / 2.0f) + (sprite_size/2.0f),
							(sprite_size/2.0f), 
							(sprite_size/2.0f),sprite_dist,false),
							sprite_dist);

				}
				for (int a = 0; a < entities.size(); a++) {
					if (wallX == (int) Math.floor(entities.get(a).m_x)
							&& wallY == (int) Math.floor(entities.get(a).m_y)
							&& !entities.get(a).m_visible) {
						entities.get(a).m_visible = true;

						float distX = (entities.get(a).m_x - m_playerX);
						float distY = (entities.get(a).m_y - m_playerY);
						float sprite_dist = (float) Math.sqrt((distX * distX)
								+ (distY * distY));
						float sprite_angle = (float) (Math.atan2(distY, distX)
								- Math.toRadians(m_playerRot));
						float sprite_size = (float) (VIEW_DIST
								/ ((float)Math.cos(sprite_angle) * sprite_dist));

						sprite_dist *= (float)Math.cos(Math.toRadians(m_playerRot)
								- angle);
						m_renderQueue.addJob(new DepthImage(
								entities.get(a).getImg(),
								((getW() / 2.0f) + ((float)Math.tan(sprite_angle) * VIEW_DIST) - 
										(sprite_size / 2.0f)),
								((getH() - sprite_size) / 2.0f),
								sprite_size, sprite_size,sprite_dist,false)
								, sprite_dist);

					}
				}
			}

			x += dx;
			y += dy;
		}

		slope = angleCos / angleSin;
		dy = (up ? -1.0f : 1.0f);
		dx = dy * slope;

		y = (float) (up ? Math.floor(m_playerY) : Math.ceil(m_playerY));
		x = m_playerX + (y - m_playerY) * slope;

		search = true;
		while (x < MAP_W && x >= 0 && y < MAP_H && y >= 0 && search) {
			wallX = (int) Math.floor(x);
			wallY = (int) Math.max(Math.floor(y + (up ? -1f : 0f)), 0f);

			if (m_map[wallX][wallY] != null) {
				float distX = x - m_playerX;
				float distY = y - m_playerY;
				float ndist = (distX * distX) + (distY * distY);

				if (dist == -1 || ndist < dist) {
					dist = ndist;
					texX = x - wallX;
					if (up) {
						texX = 1.0f - texX;
					}

					xHit = x;
					yHit = y;
					block = m_map[wallX][wallY];

					// Break
					horizontal = false;
					search = false;
				}
			} else {
				if (m_sprite_map[wallX][wallY] > 0
						&& m_visSprite[wallX][wallY] != m_frame) {
					m_visSprite[wallX][wallY] = m_frame;

					float distX = wallX + 0.5f - m_playerX;
					float distY = wallY + 0.5f - m_playerY;
					float sprite_dist = (float) Math.sqrt((distX * distX)
							+ (distY * distY));
					float sprite_angle = (float) (Math.atan2(distY, distX)
							- Math.toRadians(m_playerRot));
					float sprite_size = (float) (VIEW_DIST
							/ ((float)Math.cos(sprite_angle) * sprite_dist));

					sprite_dist *= (float)Math
							.cos(Math.toRadians(m_playerRot) - angle);
					m_renderQueue.addJob(new DepthImage(
							(m_sprite_map[wallX][wallY] == 1 ? m_texMega : m_texPill),
							((getW() / 2.0f)
									+ ((float)Math.tan(sprite_angle) * VIEW_DIST) - (sprite_size / 2.0f)) + (sprite_size/4.0f),
							((getH() - sprite_size) / 2.0f) + (sprite_size/2.0f),
							(sprite_size/2.0f), 
							(sprite_size/2.0f),sprite_dist,false),
							sprite_dist);

				}
				for (int a = 0; a < entities.size(); a++) {
					if (wallX == (int) Math.floor(entities.get(a).m_x)
							&& wallY == (int) Math.floor(entities.get(a).m_y)
							&& !entities.get(a).m_visible) {
						entities.get(a).m_visible = true;

						float distX = entities.get(a).m_x - m_playerX;
						float distY = entities.get(a).m_y - m_playerY;
						float sprite_dist = (float) Math.sqrt((distX * distX)
								+ (distY * distY));
						float sprite_angle = (float) (Math.atan2(distY, distX)
								- Math.toRadians(m_playerRot));
						float sprite_size = (float) (VIEW_DIST
								/ ((float)Math.cos(sprite_angle) * sprite_dist));

						sprite_dist *= (float)Math.cos(Math.toRadians(m_playerRot)
								- angle);
						m_renderQueue.addJob(new DepthImage(
								entities.get(a).getImg(),
								((getW() / 2.0f)
										+ ((float)Math.tan(sprite_angle) * VIEW_DIST) - (sprite_size / 2.0f)),
								((getH() - sprite_size) / 2.0f),
								sprite_size, sprite_size,sprite_dist,false),
								sprite_dist);
					}
				}

			}

			x += dx;
			y += dy;
		}

		if (dist != -1) {

			dist = (float) (Math.sqrt(dist)
					* Math.cos(Math.toRadians(m_playerRot) - angle));
			float stripHeight = Math.round(VIEW_DIST / dist);

			float top = (float)Math.round((getH() - stripHeight) / 2.0f);
			
			Wall wall = block.getFace(angle, horizontal);

			int shade = (horizontal ? 0 : 1);
			int texel = (int) Math.min(Math.floor(texX * (Settings.TEX_WIDTH / Settings.STRIP_WIDTH)), (Settings.TEX_WIDTH / Settings.STRIP_WIDTH) - 1);

			m_renderQueue.addJob(new DepthImage(
					m_texWalls[wall.getSet()][wall.getTile()][shade][texel],
					(float) (strip * Settings.STRIP_WIDTH),
					top, (float) Settings.STRIP_WIDTH,
					stripHeight, dist), dist);
		}
	}

	/**
	 * Draws the result of the ray casting
	 * 
	 * @param g
	 *            The graphics object passed by the engine
	 */
	public void draw(Graphics2D g) {

		m_renderQueue.draw(g,m_playerStride);
		m_renderQueue.clear();

		g.setTransform(NULL_TRANSFORM);
	}

}
