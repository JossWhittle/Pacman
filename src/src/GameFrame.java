import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class GameFrame extends JFrame {

	public GameFrame() {
		
		//if (Settings.FULL_SCREEN) {
			setIgnoreRepaint(true);
	    	setUndecorated(true);
		//}
	    
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gd.getDefaultConfiguration();

	    if (Settings.FULL_SCREEN) {
	    	gd.setFullScreenWindow(this);
		    
	        if(gd.isDisplayChangeSupported()) {
	        	gd.setDisplayMode(new DisplayMode(Settings.RES_WIDTH, Settings.RES_HEIGHT, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
	        }
	    }
	    
	}
	
}
