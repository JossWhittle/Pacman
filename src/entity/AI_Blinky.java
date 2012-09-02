import java.awt.image.BufferedImage;


public class AI_Blinky extends Entity {

	// Constants
	
	// Members
	
	/**
	 * Constructor
	 * @param img The image sprite array
	 * @param eimg The action sprite array
	 * @param spawnX The spawn x
	 * @param spawnY The spawn y
	 * @param paths The paths map
	 */
	public AI_Blinky(BufferedImage[][] img, int spawnX, int spawnY, int[][] paths) {
		init(img,spawnX,spawnY, paths);
		setHome(26,-4);
		targetHome();
		
		scatterMode();
	}
	
	protected void tick(int timePassed, float ticks, Player p) {
		if (m_mode == MODE_SCATTER) {
			targetHome();
		} else {
			
		}
	}

}
