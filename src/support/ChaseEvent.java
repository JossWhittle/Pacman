import java.util.ArrayList;


public class ChaseEvent extends GameEvent {
	
	public ChaseEvent(float time) {
		setTime(time);
	}
	
	public void execute(ArrayList<Entity> entities) {
		System.out.println("[CHASE MODE]");
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).chaseMode();
		}
	}

}
