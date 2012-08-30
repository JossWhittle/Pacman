import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * Handles loading and sorting map items
 * 
 * @author Joss
 *
 */
public class Map {

	// Constants
	public static final int CLEAR = new Color(0, 0, 0, 0).getRGB();
	
	public static final int BLOCK_EMPTY = 0,
							BLOCK_PLAYER = 1,
							BLOCK_ENEMY = 2,
							BLOCK_WALL = 3,
							BLOCK_GATEL = 4,
							BLOCK_GATER = 5,
							BLOCK_BLUE = 6,
							BLOCK_ORANGE = 7,
							
							SOLID_BLOCK = 3,
							
							SPRITE_MEGA = 1,
							SPRITE_PILL = 2,
							
							PATH_MOVE = 1,
							PATH_CROSS = 2,
							PATH_SPECIAL = 3;

	private static final int BLOCK_COUNT = 7, SPRITE_COUNT = 2, PATH_COUNT = 3;
	
	// Members
	public static Color[] BLOCK_COLOUR, SPRITE_COLOUR;
	public int[][] m_map, m_sprite_map, m_path_map;
	private int m_width, m_height, m_startX, m_startY, m_spriteCount, m_enemyX, m_enemyY;

	/**
	 * Constructor
	 * 
	 * @param wallID
	 *            The walls map
	 * @param spriteID
	 *            The sprite map
	 * @param pathID
	 *            The paths map
	 */
	public Map(int wallID, int spriteID, int pathID) {
		BufferedImage wallImg = Loader.getImage(wallID);
		BufferedImage spriteImg = Loader.getImage(spriteID);
		BufferedImage pathImg = Loader.getImage(pathID);

		m_width = wallImg.getWidth();
		m_height = wallImg.getHeight();

		BLOCK_COLOUR = loadKey(wallImg, 1,BLOCK_COUNT);
		SPRITE_COLOUR = loadKey(wallImg, 1,SPRITE_COUNT);

		m_map = loadMap(wallImg,BLOCK_COUNT);
		m_sprite_map = loadMap(spriteImg,SPRITE_COUNT);
		m_path_map = loadMap(pathImg,PATH_COUNT);

		findStart();
		sortSprites();
	}

	/**
	 * Loads a map into an array
	 * 
	 * @param img
	 *            The image
	 * @param count
	 * 			  The number of colours to expect
	 */
	private int[][] loadMap(BufferedImage img, int count) {
		int[][] map = new int[m_width-2][m_height];
		Color[] key = loadKey(img, 0, count);

		for (int y = 0; y < m_height; y++) {
			for (int x = 0; x < m_width-2; x++) {
				int c = img.getRGB(x+2, y);

				boolean search = true;
				for (int k = 0; k < key.length && search; k++) {
					if (c == key[k].getRGB()) {
						map[x][y] = k+1;
						search = false;
					}
				}
				
				System.out.print(map[x][y]);

			}
			System.out.println();
		}
		System.out.println();
		
		return map;
	}

	/**
	 * Gets a key from the xth column of an image
	 * 
	 * @param img
	 *            The image
	 * @param x
	 *            The column
	 * @param count
	 * 			  The number of colours to expect
	 * @return The key
	 */
	private Color[] loadKey(BufferedImage img, int x, int count) {
		Color[] key = new Color[count];
		for (int y = 0; y < count; y++) {
			int c = img.getRGB(x, y);
			key[y] = new Color(c);
		}
		return key;
	}

	/**
	 * Finds the start point in the given map
	 */
	private void findStart() {
		boolean search = true;
		for (int x = 0; x < m_width-2 && search; x++) {
			for (int y = 0; y < m_height && search; y++) {
				if (m_map[x][y] == BLOCK_PLAYER) {
					m_startX = x;
					m_startY = y;
					System.out.println("Start block at: " + x + "," + y);
				} else if (m_map[x][y] == BLOCK_ENEMY) {
					m_enemyX = x;
					m_enemyY = y;
					System.out.println("Enemy block at: " + x + "," + y);
				}
			}
		}
	}
	
	/**
	 * Counts the number of sprites and randomizes pills
	 */
	private void sortSprites() {
		for (int x = 0; x < m_width-2; x++) {
			for (int y = 0; y < m_height; y++) {
				if (m_sprite_map[x][y] >= SPRITE_PILL) {
					m_spriteCount++;
					m_sprite_map[x][y] = SPRITE_PILL + (int)(Math.floor(Math.random() * 9));
				}
			}
		}
	}

	/**
	 * Gets the number of sprites
	 * 
	 * @return The count
	 */
	public int getSpriteCount() {
		return m_spriteCount;
	}
	
	/**
	 * Gets the start x
	 * 
	 * @return The x coord
	 */
	public int getStartX() {
		return m_startX;
	}

	/**
	 * Gets the start y
	 * 
	 * @return The y coord
	 */
	public int getStartY() {
		return m_startY;
	}
	
	/**
	 * Gets the enemy x
	 * 
	 * @return The x coord
	 */
	public int getEnemyX() {
		return m_enemyX;
	}

	/**
	 * Gets the enemy y
	 * 
	 * @return The y coord
	 */
	public int getEnemyY() {
		return m_enemyY;
	}

}
