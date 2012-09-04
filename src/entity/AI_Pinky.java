import java.awt.image.BufferedImage;


public class AI_Pinky extends Entity {

	// Constants
	
	// Members
	
	public AI_Pinky(BufferedImage[][] img, int spawnX, int spawnY, int[][] paths) {
		init(img,spawnX,spawnY, paths);
		setHome(-2,24);
		targetHome();
		
		scatterMode();
	}
	
	protected void tick(int timePassed, float ticks, Player p) {
		if (m_mode == MODE_SCATTER) {
			targetHome();
		} else if (m_mode == MODE_CHASE) {
			int dx = 0, dy = 0;
			if (p.getRelDir() == Player.REL_MOVE_UP) {
				dy -= 4;
				dx -= 4;
			} else if (p.getRelDir() == Player.REL_MOVE_LEFT) {
				dx -= 4;
			} else if (p.getRelDir() == Player.REL_MOVE_RIGHT) {
				dx += 4;
			} else if (p.getRelDir() == Player.REL_MOVE_DOWN) {
				dy += 4;
			}
			setTarget((int)(p.getX() + dx),(int)(p.getY() + dy));
		}
	}

}
