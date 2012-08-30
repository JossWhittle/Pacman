import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Handles drawing the minimap to the screen
 * 
 * @author Joss
 * 
 */
public class Minimap extends SimpleDrawable {

	protected Minimap(double x, double y, double w, double h, double ox, double oy) {
		super(x, y, w, h, ox, oy);

	}
	
	public void update(Player m_player, int[][] m_map, int[][] m_sprite_map, ArrayList<Entity> m_entities) {
		
	}

	public void draw(Graphics2D g) {

	}

}
