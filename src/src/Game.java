import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This class represent the implementation of the game
 * 
 * @author Joss
 * 
 */
public class Game extends GPanel {

	// Constants
	private static final Color FLOOR = Color.darkGray, CEILING = Color.gray;
	private static final int MINIMAP_SIZE = 200, UBER = 10000;
	
	// Members
	private Map m_map;
	private Player m_player;
	private RayCaster m_caster;
	private Minimap m_minimap;
	
	private ArrayList<Entity> m_entities;
	
	private boolean LEFT = false, RIGHT = false, FORWARD = false, BACK = false;
	
	private SoundChannel sndWaka;
	private Sound sndSiren, sndOpen, sndPause, sndLife, sndDie, sndGhost, sndCherry;

	private int m_uber = 0, m_pillCount = 0, m_pillStart, SCORE_X, SCORE_Y;
	
	public Game() {
		init();

		// Map loading code HERE

		int mapWalls = Loader.loadImage("/resource/map/pacmanmap_walls.gif");
		int mapSprites = Loader.loadImage("/resource/map/pacmanmap_sprites.gif");
		int mapPaths = Loader.loadImage("/resource/map/pacmanmap_paths.gif");

		// Texture loading code HERE

		int texWalls = Loader.loadImage("/resource/texture/pacmanwalls.png");
		int texGhosts = Loader.loadImage("/resource/texture/ghost.png");
		int texPacman = Loader.loadImage("/resource/texture/pacman.png");
		int texPills = Loader.loadImage("/resource/texture/pills_small.png");

		// Sound loading code HERE

		sndWaka = new SoundChannel("/resource/sound/waka.wav",5);
		sndSiren = new Sound("/resource/sound/siren.wav");
		sndOpen = new Sound("/resource/sound/open_sound.wav");
		sndPause = new Sound("/resource/sound/pause.wav");
		sndLife = new Sound("/resource/sound/life.wav");
		sndDie = new Sound("/resource/sound/die.wav");
		sndGhost = new Sound("/resource/sound/ghost.wav");
		sndCherry = new Sound("/resource/sound/cherry.wav");

		// Variable initialization code HERE

		BufferedImage[][][] texture_walls = Content.processWalls(texWalls, 5, Settings.STRIP_WIDTH);
		BufferedImage[][] texture_ghosts = Content.processGhosts(texGhosts, 8, 5);
		BufferedImage[] texture_pills = Content.processPills(texPills, 10, 128);

		m_map = new Map(mapWalls, mapSprites, mapPaths);

		m_player = new Player(m_map.getStartX(), m_map.getStartY(), 0.0);
		
		m_caster = new RayCaster(texture_walls, texture_pills, m_map.m_map.length, m_map.m_map[0].length);
		
		m_minimap = new Minimap(WIDTH - MINIMAP_SIZE - 10, 10, MINIMAP_SIZE, MINIMAP_SIZE, 0,0, m_map.m_map, m_map.m_sprite_map, texPacman);
		
		m_entities = new ArrayList<Entity>();
		//m_entities.add();
		
		SCORE_X = WIDTH - 110;
		SCORE_Y = MINIMAP_SIZE + 30;
	}

	protected void update(long timePassed) {
		// Update code HERE

		if (m_uber > 0) {
			m_uber -= timePassed;
			if (m_uber <= 0) {
				m_uber = 0;
				sndSiren.stop();
			}
		}

		double turn = 0.0;
		if (LEFT) {
			turn += Player.LEFT;
		}
		if (RIGHT) {
			turn += Player.RIGHT;
		}
		m_player.setTurn(turn);

		double speed = 0.0;
		if (FORWARD) {
			speed += Player.FORWARD;
		}
		if (BACK) {
			speed += Player.BACK;
		}
		m_player.setSpeed(speed);

		m_player.update(timePassed, m_map.m_map);

		int px = (int) Math.floor(m_player.getX());
		int py = (int) Math.floor(m_player.getY());
		if (m_map.m_sprite_map[px][py] >= Map.SPRITE_PILL) {
			m_map.m_sprite_map[px][py] = 0;
			m_pillCount++;
			//m_pillFlash.fadeIn();
			if (m_uber == 0) {
				sndWaka.play();
			}
		} else if (m_map.m_sprite_map[px][py] == Map.SPRITE_MEGA) {
			m_map.m_sprite_map[px][py] = 0;
			//m_pillFlash.fadeIn();
			sndSiren.loop();
			m_uber += UBER;
		}
		
		m_caster.update(m_player, m_map.m_map, m_map.m_sprite_map, m_entities);
		
		m_minimap.update(m_player, m_entities);

	}

	protected void draw(Graphics2D g) {
		// Draw code HERE (No update calls!)
		g.setColor(CEILING);
		g.fillRect(0, 0, WIDTH, (int) (HEIGHT / 2.0));
		g.setColor(FLOOR);
		g.fillRect(0, (int) (HEIGHT / 2.0), WIDTH, (int) (HEIGHT / 2.0));
		
		m_caster.draw(g);
		m_minimap.draw(g);
		
		g.setColor(Color.yellow);
		g.drawString("" + m_pillCount, SCORE_X, SCORE_Y);
		g.drawString("/", SCORE_X + 40, SCORE_Y);
		g.drawString("" + m_pillStart, SCORE_X + 50, SCORE_Y);
	}
	
	/*
	 * Keyboard Handlers
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			FORWARD = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			BACK = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			LEFT = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			RIGHT = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			FORWARD = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			BACK = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			LEFT = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			RIGHT = true;
		}
	}

}
