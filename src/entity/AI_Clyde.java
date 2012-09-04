import java.awt.image.BufferedImage;


public class AI_Clyde extends Entity {

	// Constants
	
	// Members
	
	public AI_Clyde(BufferedImage[][] img, int spawnX, int spawnY, int[][] paths) {
		init(img,spawnX,spawnY, paths);
		setHome(31,27);
		targetHome();
		
		scatterMode();
	}
	
	protected void tick(int timePassed, float ticks, Player p) {
		if (m_mode == MODE_SCATTER) {
			targetHome();
		} else if (m_mode == MODE_CHASE) {
			if (distanceToTarget(p.getX(),p.getY()) < 8) {
				targetHome();
			} else {
				setTarget((int)(p.getX()),(int)(p.getY()));
			}
		}
	}

}
