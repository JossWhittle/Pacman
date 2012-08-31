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
public class Minimap extends SimpleDrawable {

	// Constants
	
	// Members
	private double m_playerX, m_playerY, m_playerR;
	private int[][] m_map, m_sprite_map;
	ArrayList<Entity> m_entities;
	
	private DrawableImage m_playerSprite;
	
	private int MAP_W, MAP_H;
	
	protected Minimap(double x, double y, double w, double h, double ox, double oy, int[][] map, int[][] sprite_map, int pimg) {
		super(x, y, w, h, ox, oy);
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

	public void draw(Graphics2D g) {
		double BLOCK_W = getW() / MAP_W, BLOCK_H = getH() / MAP_H;
		
		for (int x = 0; x < MAP_W; x++) {
			for (int y = 0; y < MAP_H; y++) {
				if (m_map[x][y] >= Map.SOLID_BLOCK) {
					g.setColor(Map.BLOCK_COLOUR[m_map[x][y]-1]);
					g.fillRect((int)(getX() + (x * BLOCK_W)), (int)(getY() + (y * BLOCK_H)), (int)Math.ceil(BLOCK_W), (int)Math.ceil(BLOCK_H));
				} else if (m_sprite_map[x][y] >= Map.SPRITE_PILL) {
					
					g.setColor(Map.SPRITE_COLOUR[0]);
					g.fill(new Ellipse2D.Double(((getX() - getOX()) + (x * BLOCK_W) + 2),
							((getY() - getOY()) + (y * BLOCK_H) + 2),
							Math.ceil(BLOCK_W - 4), Math.ceil(BLOCK_H - 4)));
				} else if (m_sprite_map[x][y] == Map.SPRITE_MEGA) {
					g.setColor(Map.SPRITE_COLOUR[1]);
					g.fill(new Ellipse2D.Double(((getX() - getOX()) + Math.ceil(x * BLOCK_W) + 1),
							((getY() - getOY()) + (y * BLOCK_H) + 1),
							Math.ceil(BLOCK_W - 2), Math.ceil(BLOCK_H - 2)));
				}
			}
		}
		
		double px = ((getX() - getOX()) + (m_playerX * BLOCK_W));
		double py = ((getY() - getOY()) + (m_playerY * BLOCK_H));
		
		m_playerSprite.setW(BLOCK_W);
		m_playerSprite.setH(BLOCK_H);
		m_playerSprite.setXY(px,py);
		m_playerSprite.setRot(m_playerR);
		m_playerSprite.draw(g);
	}

}
