

public class Block {
	
	// Constants
	
	// Members
	private Wall m_north, m_south, m_east, m_west;
	
	public Block(int tex_set) {
		this(tex_set,Wall.TEX_RND);
	}
	
	public Block(int tex_set, int tex_tile) {
		m_north = new Wall(tex_set, tex_tile);
		m_south = new Wall(tex_set, tex_tile);
		m_east = new Wall(tex_set, tex_tile);
		m_west = new Wall(tex_set, tex_tile);
	}
	
	public Block(Wall n, Wall s, Wall e, Wall w) {
		m_north = n;
		m_south = s;
		m_east = e;
		m_west = w;
	}
	
	public Wall getN() {
		return m_north;
	}
	
	public Wall getS() {
		return m_south;
	}
	
	public Wall getE() {
		return m_east;
	}
	
	public Wall getW() {
		return m_west;
	}
}