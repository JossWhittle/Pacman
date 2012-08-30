/**
 * Handles playing a sound file on multiple channels. Currently it does this by
 * simply loading the file multiple times, giving the game multiple references
 * to play it by. This could possibly be improved by looking into the JMF
 * framework but that seems overly complicated given how small the sound clips
 * are and how much RAM modern systems have
 * 
 * @author Joss
 * 
 */
public class SoundChannel {

	// Constants

	// Members
	private int m_channels;
	private int m_index = 0;
	private Sound[] m_sound;

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            The path to the file
	 * @param channels
	 *            The number of channels to give the sound
	 */
	public SoundChannel(String dir, int channels) {
		m_channels = channels;
		m_sound = new Sound[m_channels];
		for (int i = 0; i < m_channels; i++) {
			m_sound[i] = new Sound(Loader.loadSound(dir));
		}
	}

	/**
	 * Plays the sound on the next free channel
	 */
	public void play() {
		m_sound[m_index].play();
		m_index = (m_index + 1) % m_channels;
	}

}
