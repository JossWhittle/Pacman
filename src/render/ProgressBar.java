import java.awt.AlphaComposite;
import java.awt.Graphics2D;


public class ProgressBar extends DrawableImage {

	// Constants
	private static final float BG_ALPHA = 0.1f;
	
	// Members
	private float m_progress = 1.0f;
	
	public ProgressBar(int img, float x, float y, float w, float h, float ox,
			float oy, float rot, float alpha) {
		super(img, x, y, w, h, ox, oy, rot, alpha);
	}

	public void setProgress(float v) {
		m_progress = v;
		if (m_progress > 1.0f) {
			m_progress = 1.0f;
		} else if (m_progress < 0) {
			m_progress = 0;
		}
	}
	
	protected void drawContent(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				(float) BG_ALPHA));
		g.drawImage(getImg(), (int) (m_x - m_ox), (int) (m_y - m_oy), (int) (m_w),
				(int) m_h, null);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				(float) getAlpha()));
		g.drawImage(getImg().getSubimage(0, 0, (int)Math.max((getImg().getWidth() * m_progress), 1), getImg().getHeight()), (int) (m_x - m_ox), (int) (m_y - m_oy), (int) (m_w * m_progress),
				(int) m_h, null);
	}

}
