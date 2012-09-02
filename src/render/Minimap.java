import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Handles drawing the minimap to the screen
 * 
 * @author Joss
 * 
 */
public class Minimap extends Drawable {

	// Constants
	
	// Members
	private float m_playerX, m_playerY, m_playerR;
	private int[][] m_sprite_map;
	private Block[][] m_map;
	ArrayList<Entity> m_entities;
	
	private DrawableImage m_playerSprite;
	
	private int MAP_W, MAP_H;
	
	protected Minimap(float x, float y, float w, float h, float ox, float oy, Block[][] map, int[][] sprite_map, int pimg) {
		super(x, y, w, h, ox, oy, 0, 0.2f);
		m_map = map;
		m_sprite_map = sprite_map;
		
		MAP_W = map.length;
		MAP_H = map[0].length;
		
		m_playerSprite = new DrawableImage(pimg,0,0,0,0);
	}
	
	public void update(Player m_player, ArrayList<Entity> entities) {
		m_playerX = m_player.getX();
		m_playerY = m_player.getY();
		m_playerR = m_player.getRot();
		
		m_entities = entities;
	}

	protected void drawContent(Graphics2D g) {
		float BLOCK_W = getW() / MAP_H, BLOCK_H = getH() / MAP_W;
		
		for (int y = 0; y < MAP_H; y++) {
			for (int x = 0; x < MAP_W; x++) {
				if (m_map[x][y] != null) {
					g.setColor(Map.BLOCK_COLOUR[2]);
					g.fillRect((int)(getX() + ((MAP_H - y) * BLOCK_W))-1, (int)(getY() + (x * BLOCK_H))-1, (int)Math.ceil(BLOCK_W)+1, (int)Math.ceil(BLOCK_H)+1);
				} else if (m_sprite_map[x][y] >= Map.SPRITE_PILL) {
					
					g.setColor(Color.orange);
					g.fill(new Ellipse2D.Double(((getX() - getOX()) + ((MAP_H - y) * BLOCK_W) + 2)-1,
							((getY() - getOY()) + (x * BLOCK_H) + 2)-1,
							Math.ceil(BLOCK_W - 4)+1, Math.ceil(BLOCK_H - 4)+1));
				} else if (m_sprite_map[x][y] == Map.SPRITE_MEGA) {
					g.setColor(Color.orange);
					g.fill(new Ellipse2D.Double(((getX() - getOX()) + Math.ceil((MAP_H - y) * BLOCK_W) + 1)-1,
							((getY() - getOY()) + (x * BLOCK_H) + 1)-1,
							Math.ceil(BLOCK_W - 2)+1, Math.ceil(BLOCK_H - 2)+1));
				}
			}
		}
		
		float px = ((getX() - getOX()) + ((MAP_H - m_playerY + 1) * BLOCK_W));
		float py = ((getY() - getOY()) + (m_playerX * BLOCK_H));
		
		m_playerSprite.setW(BLOCK_W*1.5f);
		m_playerSprite.setH(BLOCK_H*1.5f);
		m_playerSprite.setOX((BLOCK_W*1.5f)/2f);
		m_playerSprite.setOY((BLOCK_H*1.5f)/2f);
		
		m_playerSprite.setXY(px,py);
		m_playerSprite.setRot(m_playerR+90f);
		m_playerSprite.draw(g);
	}

}
