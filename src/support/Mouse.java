import java.awt.Canvas;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener {

	// Constants

	// Members
	private int m_mx = 0, m_my = 0, m_screenXDiv2, m_screenYDiv2;
	private int dmx = 0, dmy = 0, dx = 0, dy = 0;
	private boolean m_hook = false;
	
	private Robot m_robot;

	public Mouse() throws Exception {
		m_robot = new Robot();
		Toolkit t = Toolkit.getDefaultToolkit();
		m_screenXDiv2 = t.getScreenSize().width / 2;
		m_screenYDiv2 = t.getScreenSize().height / 2;
	}

	public void mouseDragged(MouseEvent e) {
		this.mouseMoved(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (m_hook) {
			m_mx = e.getXOnScreen();
			m_my = e.getYOnScreen();
			if (m_mx != m_screenXDiv2 || m_my != m_screenYDiv2) {
				if (m_mx != m_screenXDiv2) {
					dx = m_mx - m_screenXDiv2;
					dmx += dx;
				}
				if (m_my != m_screenYDiv2) {
					dy = m_my - m_screenYDiv2;
					dmy += dy;
				}
				m_robot.mouseMove(m_screenXDiv2, m_screenYDiv2);
			}
		}
	}
	
	public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
	
	public void hook() {
		m_hook = true;
	}
	
	public void unhook() {
		m_hook = false;
	}
	
	public void clear() {
		dx = 0;
		dy = 0;
	}

}
