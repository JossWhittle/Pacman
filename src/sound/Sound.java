import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Handles playing a sound file
 * 
 * @author Joss
 * 
 */
public class Sound {

	// Constant

	// Member
	private Clip m_clip;
	private boolean m_loaded = false;

	/**
	 * Constructor
	 * 
	 * @param sid
	 *            The id of the sound file
	 */
	public Sound(int sid) {
		try {
			m_clip = AudioSystem.getClip();
			m_clip.open(Loader.getSound(sid));
			m_loaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            The path to the sound file
	 */
	public Sound(String dir) {
		this(Loader.loadSound(dir));
	}

	/**
	 * Sets the clip playing
	 */
	public void play() {
		if (m_loaded) {
			if (m_clip.isRunning()) {
				m_clip.stop();
			}
			m_clip.setFramePosition(0);
			m_clip.start();
		}
	}

	/**
	 * Pauses the clip
	 */
	public void pause() {
		m_clip.stop();
	}

	/**
	 * Stops and rewinds the clip
	 */
	public void stop() {
		m_clip.stop();
		m_clip.setFramePosition(0);
	}

	/**
	 * Sets the sound looping
	 */
	public void loop() {
		if (m_loaded) {
			if (m_clip.isRunning()) {
				m_clip.stop();
			}
			m_clip.setFramePosition(0);
			m_clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

}
