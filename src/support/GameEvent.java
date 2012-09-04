import java.util.ArrayList;


public abstract class GameEvent {

	// Constants
	
	// Members
	private int m_time;
	
	public void setTime(float t) {
		m_time = (int) (t * 1000);
	}
	
	public int getTime() {
		return m_time;
	}
	
	public abstract void execute(ArrayList<Entity> entities);
}
