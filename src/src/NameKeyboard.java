import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class NameKeyboard implements KeyListener {

	// Constants
	private static final int MIN_ACCEPTED = 1, MAX_ACCEPTED = 15;
	private static final String DEFAULT = "Player";
	
	// Members
	private boolean m_hooked = false;
	private String m_value = DEFAULT;
	
	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
		if (m_hooked) {
			if (m_value.length() > 0 && e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				m_value = m_value.substring(0, m_value.length()-1);
			} else if (m_value.length() < MAX_ACCEPTED) {
				m_value += (""+e.getKeyChar()).replaceAll("(?uix)[^a-zA-Z\\s]", "");
			}
		}
	}
	
	public String getValue() {
		return m_value;
	}
	
	public boolean isValid() {
		return m_value.length() >= MIN_ACCEPTED;
	}
	
	public void clear() {
		m_value = "";
	}
	
	public void setDefault() {
		m_value = DEFAULT;
	}
	
	public void hook() {
		m_hooked = true;
	}
	
	public void unhook() {
		m_hooked = false;
	}

}
