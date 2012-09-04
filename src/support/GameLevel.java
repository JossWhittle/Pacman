import java.util.ArrayList;


public class GameLevel {

	// Constants
	
	// Members
	private GameEvent[] m_events;
	private int m_index = 0;
	private long m_accTime;
	
	public GameLevel(GameEvent[] events) {
		m_events = events;
	}
	
	public void update(int timePassed, ArrayList<Entity> entities) {
		m_accTime += timePassed;
		
		if (m_index < m_events.length && m_events[m_index].getTime() <= m_accTime) {
			m_events[m_index].execute(entities);
			
			m_index++;
			update(0, entities);
		}
	}
	
}
