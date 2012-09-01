
public class Fader extends DrawableImage {

	// Constants
	
	// Members 
	private boolean m_fading = false;
	private double[][] m_targets = {{1.0,100}};
	private int m_tid = 0;
	private double m_start = 1;
	private long m_fx = 0, m_duration = 100;
	
	public Fader(int img, double x, double y, double w, double h) {
		super(img, x, y, w, h);
		
	}
	
	public void update(long timePassed) {		
		if (m_fading) {
			m_fx += timePassed;
			
			setAlpha(fadeFunction((double)m_fx,m_start,m_targets[m_tid][0]-m_start,(double)m_targets[m_tid][1]));
			
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
	
	private double fadeFunction(double t, double b, double c, double d) {
		return Math.min(Math.max((-c) * Math.cos((t/d) * (Math.PI/2)) + c + b, 0),1);
	}
	
	public boolean isFading() {
		return m_fading;
	}
	
	public void setFadeTarget(double t) {
		setFadeTarget(new double[][]{{t,m_duration}});
	}
	
	public void setFadeTarget(double[][] t) {
		m_fx = 0;
		m_tid = 0;
		m_targets = t;
		m_fading = true;
		m_start = getAlpha();
	}
}
