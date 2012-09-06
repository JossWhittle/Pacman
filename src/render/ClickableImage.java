import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class ClickableImage extends DrawableImage {

	// Constants
	private static final int PASSIVE = 0, HOVER = 1, ACTIVE = 2;
	private static final float PASSIVE_ALPHA = 0.1f, HOVER_ALPHA = 0.5f, ACTIVE_ALPHA = 0.8f, DURATION = 150f;
	
	// Members
	private int m_state = PASSIVE;
	private float m_start, m_delta, m_fx = 0;
	private boolean m_fading = false;
	
	public ClickableImage(int img, float x, float y, float w, float h) {
		super(img, x, y, w, h, w/2.0f, h/2.0f);
		
	}
	
	public void update(int timePassed, float x, float y, boolean click) {
		Rectangle rect = new Rectangle((int)(m_x-m_ox),(int)(m_y-m_oy),(int)(m_w),(int)(m_h));
		if (rect.contains(new Point((int)x,(int)y))) {
			if (click) {
				m_state = ACTIVE;
				setTarget(ACTIVE_ALPHA);
			} else {
				m_state = HOVER;
				setTarget(HOVER_ALPHA);
			}
		} else {
			m_state = PASSIVE;
			setTarget(PASSIVE_ALPHA);
		}
		
		if (m_fading) {
			m_fx += timePassed;
			setAlpha(fadeFunction(m_fx,m_start,m_delta,DURATION));
			
			if (m_fx >= DURATION) {
				m_fading = false;
			}
		}
	}
	
	private void setTarget(float target) {
		m_start = getAlpha();
		m_delta = target - m_start;
		m_fx = 0;
		m_fading = true;
	}
	
	private float fadeFunction(float t, float b, float c, float d) {
		t /= d / 2f;
		if (t < 1f) return Math.min(Math.max((c/2f)*t*t + b, 0),1);
		t--;
		return Math.min(Math.max((-c/2f) * (t*(t-2f) - 1f) + b, 0),1);
	}
	
	public boolean isActive() {
		return (m_state == ACTIVE);
	}
	
	public int getState() {
		return m_state;
	}

}
