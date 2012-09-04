import java.util.ArrayList;


public class ScatterEvent extends GameEvent {
	
	public ScatterEvent(float time) {
		setTime(time);
	}
	
	public void execute(ArrayList<Entity> entities) {
		System.out.println("[SCATTER MODE]");
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).scatterMode();
		}
	}

}
