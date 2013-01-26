import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class GameFrame extends JFrame {

	public GameFrame() {
		
		if (Stitch.FULL_SCREEN) {
			setIgnoreRepaint(true);
	    	setUndecorated(true);
		}
	    
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gd.getDefaultConfiguration();

	    if (Stitch.FULL_SCREEN) {
	    	gd.setFullScreenWindow(this);
		    
	        if(gd.isDisplayChangeSupported()) {
	        	gd.setDisplayMode(new DisplayMode(Stitch.RES_WIDTH, Stitch.RES_HEIGHT, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
	        }
	    }
	    
	}
	
}
