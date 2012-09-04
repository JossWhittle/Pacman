import java.awt.Color;
import java.awt.Graphics2D;


public abstract class MenuItem extends Drawable {

	// Constants
	protected static float WIDTH = 600, HEIGHT = 50, PASSIVE_ALPHA = 0.1f, ACTIVE_ALPHA = 0.3f;
	protected static Color TEXT_COLOUR = new Color(250,250,250);
	
	// Members
	private float m_labelX = 0, m_labelY = 0;
	private String m_label = "";
	private boolean m_active = false;
	
	public MenuItem(String s) {
		super(0, 0, Settings.RES_WIDTH, Settings.RES_HEIGHT, 0, 0, DEFAULT_ROTATION, DEFAULT_ALPHA);
		m_label = s;
	}
	
	protected void drawContent(Graphics2D g) {}
	
	public abstract void execute();
	
	public void drawLabel(Graphics2D g) {
		g.setFont(g.getFont().deriveFont(30));
		g.setColor(TEXT_COLOUR);
		g.drawString(m_label, m_labelX, m_labelY);
	}

	public void setActive(boolean v) { 
		m_active = v;
		setAlpha((m_active ? ACTIVE_ALPHA : PASSIVE_ALPHA)); 
	}
	
	public void setLabelPosition(float x, float y) {
		m_labelX = x;
		m_labelY = y;
	}

}
