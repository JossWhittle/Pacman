import java.awt.Color;
import java.awt.Graphics2D;


public class Fader extends DrawableImage {

	// Constants
	
	// Members 
	private boolean m_fading = false;
	private float[][] m_targets = {{1.0f,100f}};
	private int m_tid = 0;
	private float m_start = 1f;
	private int m_fx = 0, m_duration = 100;
	
	private Color m_colour = null;
	
	public Fader(int img, float x, float y, float w, float h) {
		super(img, x, y, w, h);
		
	}
	
	public Fader(Color c, float x, float y, float w, float h) {
		super(-1, x, y, w, h);
		m_colour = c;
	}
	
	public void update(int timePassed) {		
		if (m_fading) {
			m_fx += timePassed;
			
			setAlpha(fadeFunction((float)m_fx,m_start,m_targets[m_tid][0]-m_start,(float)m_targets[m_tid][1]));
			
			if (m_fx >= (float)m_targets[m_tid][1]) {
				m_fading = false;
				
				if (m_tid < m_targets.length - 1) {
					m_tid++;
					m_fading = true;
					m_start = getAlpha();
				}
				m_fx = 0;
			}
		}
	}
	
	protected void drawContent(Graphics2D g) {
		if (m_colour != null) {
			g.setColor(m_colour);
			g.fillRect((int) (m_x - m_ox), (int) (m_y - m_oy), (int) m_w,
					(int) m_h);
		}
		g.drawImage(getImg(), (int) (m_x - m_ox), (int) (m_y - m_oy), (int) m_w,
				(int) m_h, null);
	}
	
	private float fadeFunction(float t, float b, float c, float d) {
		
		t /= d / 2f;
		if (t < 1f) return Math.min(Math.max((c/2f)*t*t + b, 0),1);
		t--;
		return Math.min(Math.max((-c/2f) * (t*(t-2f) - 1f) + b, 0),1);
		
		// return (float) Math.min(Math.max((-c) * Math.cos((t/d) * (Math.PI/2)) + c + b, 0),1);
	}
	
	public boolean isFading() {
		return m_fading;
	}
	
	public void setFadeTarget(float t) {
		setFadeTarget(new float[][]{{t,m_duration}});
	}
	
	public void setFadeTarget(float[][] t) {
		m_fx = 0;
		m_tid = 0;
		m_targets = t;
		m_fading = true;
		m_start = getAlpha();
	}
}
