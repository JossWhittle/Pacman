
public class Fader extends DrawableImage {

	// Constants
	
	// Members 
	private boolean m_fading = false;
	private float[][] m_targets = {{1.0f,100f}};
	private int m_tid = 0;
	private float m_start = 1f;
	private int m_fx = 0, m_duration = 100;
	
	public Fader(int img, float x, float y, float w, float h) {
		super(img, x, y, w, h);
		
	}
	
	public void update(int timePassed) {		
		if (m_fading) {
			m_fx += timePassed;
			
			setAlpha(fadeFunction((float)m_fx,m_start,m_targets[m_tid][0]-m_start,(float)m_targets[m_tid][1]));
			
			if (m_fx >= m_duration) {
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
	
	private float fadeFunction(float t, float b, float c, float d) {
		return (float) Math.min(Math.max((-c) * Math.cos((t/d) * (Math.PI/2)) + c + b, 0),1);
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
