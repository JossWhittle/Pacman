import java.awt.image.BufferedImage;


public class AI_Inky extends Entity {

	// Constants
	
	// Members
	private int m_blinkyX = 0, m_blinkyY = 0;
	
	public AI_Inky(BufferedImage[][] img, int spawnX, int spawnY, int[][] paths) {
		init(img,spawnX,spawnY, paths);
		setHome(32,1);
		targetHome();
		
		scatterMode();
	}
	
	protected void tick(int timePassed, float ticks, Player p) {
		if (m_mode == MODE_SCATTER) {
			targetHome();
		} else if (m_mode == MODE_CHASE) {
			int dx = (int) p.getX(), dy = (int) p.getY();
			if (p.getRelDir() == Player.REL_MOVE_UP) {
				dy -= 2;
				dx -= 2;
			} else if (p.getRelDir() == Player.REL_MOVE_LEFT) {
				dx -= 2;
			} else if (p.getRelDir() == Player.REL_MOVE_RIGHT) {
				dx += 2;
			} else if (p.getRelDir() == Player.REL_MOVE_DOWN) {
				dy += 2;
			}
			
			dx += (m_blinkyX - dx);
			dy += (m_blinkyY - dy);
			
			setTarget((int)(dx),(int)(dy));
		}
	}
	
	public void setBlinky(int x, int y) {
		m_blinkyX = x;
		m_blinkyY = y;
	}

}
