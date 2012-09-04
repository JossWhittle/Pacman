import java.awt.Graphics2D;
import java.util.ArrayList;


public class GameMenu extends MenuItem {

	// Constants
	protected static final float OFFSET_TOP = 50;
	
	// Members
	private GameMenu m_parent = null;
	private ArrayList<MenuItem> m_options;
	
	public GameMenu(String s) {
		super(s);
	}
	
	public GameMenu(String s, GameMenu p) {
		super(s);
		setParent(p);
	}
	
	public void add(MenuItem i) {
		i.setLabelPosition((Settings.RES_WIDTH / 2.0f) - (WIDTH / 2.0f), OFFSET_TOP + (m_options.size() * HEIGHT));
		m_options.add(i);
	}
	
	public void setParent(GameMenu p) {
		m_parent = p;
	}
	
	public void update(int timePassed, int mouseX, int mouseY, boolean clicked) {
		
	}

	protected void drawContent(Graphics2D g) {
		if (m_parent != null) {
			// Draw back button
		}
		
		float mx = 0, my = 0;
		for (int i = 0; i < m_options.size(); i++) {
			m_options.get(i).drawLabel(g);
		}
	}

	public void execute() {
		
	}
}
