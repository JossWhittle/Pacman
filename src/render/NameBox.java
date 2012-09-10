import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class NameBox extends DrawableImage {

	// Constants
	private static final float FONT_SIZE = 24f, OFFSET_X = 15, OFFSET_Y = 40f, DURATION = 1000;
	private static final Color TEXT_COLOUR = new Color(221,221,221);
	
	// Members
	private Font m_font;
	private String m_value = "";
	private int m_fx = 0;
	private boolean m_valid = false;
	
	protected NameBox(int img, int fnt, float x, float y, float w, float h, float alpha) {
		super(img, x, y, w, h, w/2.0f, h/2.0f, 0, alpha);
		m_font = Loader.getFont(fnt);
	}

	protected void drawContent(Graphics2D g) {
		Font std = g.getFont();
		g.setFont(m_font);
		g.setColor(TEXT_COLOUR);
		g.drawImage(getImg(), (int) (m_x - m_ox), (int) (m_y - m_oy-OFFSET_Y), (int) m_w, (int) m_h, null);
		g.drawString(m_value, (getX()-getOX()+10+OFFSET_X), (int)(getY()-getOY()));
		
		if (m_fx < (DURATION / 2.0f)) {
			g.fillRect((int)(getX()-getOX()+10+OFFSET_X + (m_value.length() * FONT_SIZE)), (int)(getY()-getOY()-24), 7, (int)30);
		}
		
		if (!m_valid) {
			g.setFont(m_font.deriveFont(20f));
			g.drawString("Well... Type something!", (getX()-getOX()+10+OFFSET_X), (int)(getY()-getOY() + 50f));
		}
		
		g.setFont(std);
	}
	
	public void update(int timePassed, String v) {
		m_fx += timePassed;
		m_fx = m_fx % (int)DURATION;
		m_value = v;
		m_valid = (m_value.length() > 0);
	}

}
