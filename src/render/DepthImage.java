import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class DepthImage extends DrawableImage {

	// Constants
	private static final Color BG = new Color(7,7,7);
	private static final double MAX_DEPTH = 3, FALLOFF_LIMIT = 0.25;
	
	// Members
	private double m_depth, m_df;
	private boolean m_back;
	
	public DepthImage(BufferedImage img, double x, double y, double w, double h, double depth) {
		this(img,x,y,w,h,depth,true);
	}
	
	public DepthImage(BufferedImage img, double x, double y, double w, double h, double depth, boolean back) {
		super(img, x, y, w, h);
		m_depth = depth;
		m_back = back;
		
		double t = (m_depth - FALLOFF_LIMIT) / MAX_DEPTH;
		m_df = Math.max(Math.min(((-1)*t*t) + (1), 1),0.02);
	}
	
	protected void drawContent(Graphics2D g) {
		if (m_back) {
			g.setColor(BG);
			g.fillRect((int) (m_x - m_ox), (int) (m_y - m_oy), (int) m_w, (int) m_h);
		}
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				(float) m_df));
		g.drawImage(getImg(), (int) (m_x - m_ox), (int) (m_y - m_oy), (int) m_w,
				(int) m_h, null);
	}
	
	

}
