import java.awt.Graphics2D;
import java.util.ArrayList;

public class RenderQueue {

	// Constants

	// Members
	private ArrayList<RenderJob> m_queue;

	/**
	 * Constructor
	 */
	public RenderQueue() {
		m_queue = new ArrayList<RenderJob>();
	}

	/**
	 * Draws out all render jobs in the queue
	 * 
	 * @param g
	 *            The graphics object passed by the engine
	 */
	public void draw(Graphics2D g) {
		for (int i = 0; i < m_queue.size(); i++) {
			m_queue.get(i).draw(g);
		}
	}

	/**
	 * Draws out all render jobs in the queue
	 * 
	 * @param g
	 *            The graphics object passed by the engine
	 * @param stride
	 *            This is a really hacky way of adding stride bounce. Sorry
	 */
	public void draw(Graphics2D g, double stride) {
		for (int i = 0; i < m_queue.size(); i++) {
			SimpleDrawable j = m_queue.get(i).getJob();
			j.setOY(stride);
			j.draw(g);
		}
	}

	/**
	 * Adds a render job to the queue
	 * 
	 * @param job
	 *            The drawale object
	 * @param dist
	 *            It's distance from the camera
	 */
	public void addJob(SimpleDrawable job, double dist) {
		RenderJob rj = new RenderJob(job, dist);

		if (m_queue.size() == 0) {
			m_queue.add(rj);
		} else {
			addJob(rj, 0, m_queue.size());
		}
	}

	/**
	 * The worker function for addJob
	 * 
	 * @param rj
	 *            The render job
	 * @param left
	 *            The left search index
	 * @param right
	 *            The right search index
	 */
	private void addJob(RenderJob rj, int left, int right) {
		int meta = right - left, centre = left + (meta / 2);
		if (meta == 1) {
			if (rj.getDistance() >= m_queue.get(left).getDistance()) {
				m_queue.add(left, rj);
			} else {
				m_queue.add(right, rj);
			}
		} else {
			if (rj.getDistance() >= m_queue.get(centre).getDistance()) {
				addJob(rj, left, centre);
			} else {
				addJob(rj, centre, right);
			}
		}
	}

	/**
	 * Clears the render queue
	 */
	public void clear() {
		m_queue.clear();
	}

	/**
	 * Handles modeling the data for a render operation in the render queue. A
	 * distance is stored along with the job so that we can sort render jobs by
	 * their z-depth from the camera
	 * 
	 * @author Joss
	 * 
	 */
	private class RenderJob {

		// Constants

		// Members
		private SimpleDrawable m_job;
		private double m_dist;

		/**
		 * Constructor
		 * 
		 * @param job
		 *            The drawable object to draw
		 * @param dist
		 *            The distance from the camera (z-depth)
		 */
		public RenderJob(SimpleDrawable job, double dist) {
			m_job = job;
			m_dist = dist;
		}

		/**
		 * Calls the render job to draw
		 * 
		 * @param g
		 *            The graphics object passed by the engine
		 */
		public void draw(Graphics2D g) {
			m_job.draw(g);
		}

		/**
		 * Gets the render jobs distance from the camera
		 * 
		 * @return The distance
		 */
		public double getDistance() {
			return m_dist;
		}
		
		/**
		 * Gets the render job
		 * 
		 * @return The object
		 */
		public SimpleDrawable getJob() {
			return m_job;
		}
	}

}
