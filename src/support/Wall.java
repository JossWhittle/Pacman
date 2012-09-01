import java.awt.image.BufferedImage;


public class Wall {
	
	// Constants
	public static final int TEX_OUTER = 0, TEX_BLUE = 1, TEX_ORANGE = 1, TEX_GATE = 0;
	public static final int TEX1 = 0, TEX2 = 1, TEX3 = 2, TEX4 = 3, TEX_RND = -1;
	
	// Members
	private int m_tile, m_set;
	
	public Wall(int val) {
		this(val,TEX1);
	}
	
	public Wall(int val, int i) {
		m_set = val;
		m_tile = (int)(i == TEX_RND ? Math.floor(Math.random() * 3.9999999) : i);
	}
	
	public int getSet() {
		return m_set;
	}
	
	public int getTile() {
		return m_tile;
	}
}