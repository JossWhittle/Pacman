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
	private static final Color FLOOR = new Color(7,7,7), CEILING = new Color(7,7,7); // new Color(13,10,10)
	private static final int MINIMAP_SIZE = 175, UBER = 10000;
	
	// Members
	private Map m_map;
	private Player m_player;
	private RayCaster m_caster;
	private Minimap m_minimap;
	
	private ArrayList<Entity> m_entities;
	
	private boolean LEFT = false, RIGHT = false, FORWARD = false, BACK = false;
	
	private SoundChannel sndWaka;
	private Sound sndSiren, sndOpen, sndPause, sndLife, sndDie, sndGhost, sndCherry;
	
	private Fader m_vignette;

	private int m_uber = 0, m_pillCount = 0, m_pillStart, SCORE_X, SCORE_Y;
	
	public Game() {
		init();

		// Map loading code HERE

		int mapWalls = Loader.loadImage("/resource/map/pacmanmap_walls.gif");
		int mapSprites = Loader.loadImage("/resource/map/pacmanmap_sprites.gif");
		int mapPaths = Loader.loadImage("/resource/map/pacmanmap_paths.gif");

		// Texture loading code HERE
		
		int vignette = Loader.loadImage("/resource/texture/vignette.png");

		int[][] texWalls = {{
			Loader.loadImage("/resource/texture/dev/gif/small/wall_marine_1.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_marine_2.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_marine_3.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_marine_4.gif")
		},
		{
			Loader.loadImage("/resource/texture/dev/gif/small/wall_alien_rust_1.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_alien_rust_2.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_alien_rust_4.gif"),
			Loader.loadImage("/resource/texture/dev/gif/small/wall_alien_rust_3.gif")
		}};
		
		int[] texXeno = {
			Loader.loadImage("/resource/texture/xeno/xeno_front.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_right.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_left.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_back.gif")
		};
		
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

		BufferedImage[][][][] texture_walls = Content.processWalls(texWalls, Settings.STRIP_WIDTH);
		BufferedImage[][] texture_ghosts = Content.processGhosts(texGhosts, 8, 5);
		BufferedImage[] texture_pills = Content.processPills(texPills, 10, 128);
		
		BufferedImage[][] texture_xeno = {
			Loader.splitImage(texXeno[0], 50),
			Loader.splitImage(texXeno[1], 100),
			Loader.splitImage(texXeno[2], 100),
			Loader.splitImage(texXeno[3], 50)
		};

		m_map = new Map();

		m_player = new Player(m_map.getStartX()+0.5f, m_map.getStartY()+0.5f, 180f, m_map.m_map);
		
		m_caster = new RayCaster(texture_walls, texture_pills, m_map.m_map, m_map.m_sprite_map);
		
		m_minimap = new Minimap(WIDTH - MINIMAP_SIZE - 30, 10, MINIMAP_SIZE, MINIMAP_SIZE, 0,0, m_map.m_map, m_map.m_sprite_map, texPacman);
		
		m_entities = new ArrayList<Entity>();
		m_entities.add(new AI_Blinky(texture_xeno, m_map.getEnemyX(), m_map.getEnemyY(), m_map.m_path_map));
		
		//m_entities.add();
		
		SCORE_X = WIDTH - 110;
		SCORE_Y = MINIMAP_SIZE + 30;
		m_pillStart = m_map.getSpriteCount();
		
		m_vignette = new Fader(vignette, 0,0,WIDTH,HEIGHT);
	}

	protected void update(int timePassed) {
		// Update code HERE

		if (m_uber > 0) {
			m_uber -= timePassed;
			if (m_uber <= 0) {
				m_uber = 0;
				sndSiren.stop();
				m_vignette.setFadeTarget(new float[][]{{1f,2000f}});
			}
		}

		float turn = 0.0f;
		if (LEFT) {
			turn += Player.LEFT;
		}
		if (RIGHT) {
			turn += Player.RIGHT;
		}
		m_player.setTurn(turn);

		float speed = 0.0f;
		if (FORWARD) {
			speed += Player.FORWARD;
		}
		if (BACK) {
			speed += Player.BACK;
		}
		m_player.setSpeed(speed);

		m_player.update(timePassed);

		int px = (int) Math.floor(m_player.getX());
		int py = (int) Math.floor(m_player.getY());
		if (m_map.m_sprite_map[px][py] >= Map.SPRITE_PILL) {
			m_map.m_sprite_map[px][py] = 0;
			m_pillCount++;
			
			if (m_uber == 0) {
				//sndWaka.play();
				m_vignette.setFadeTarget(new float[][]{{0.8f,50f},{1f,100f}});
			}
		} else if (m_map.m_sprite_map[px][py] == Map.SPRITE_MEGA) {
			m_map.m_sprite_map[px][py] = 0;
			
			m_vignette.setFadeTarget(new float[][]{{0.75f,50f}});
			//sndSiren.loop();
			m_uber += UBER;
		}
		
		for (int i = 0; i < m_entities.size(); i++) {
			m_entities.get(i).update(timePassed, m_player);
		}
		
		m_caster.update(m_player, m_entities);
		
		m_minimap.update(m_player, m_entities);
		
		m_vignette.update(timePassed);
	}

	protected void draw(Graphics2D g) {
		// Draw code HERE (No update calls!)
		g.setColor(CEILING);
		g.fillRect(0, 0, WIDTH, (int) (HEIGHT / 2.0));
		g.setColor(FLOOR);
		g.fillRect(0, (int) (HEIGHT / 2.0), WIDTH, (int) (HEIGHT / 2.0));
		
		m_caster.draw(g);
		m_vignette.draw(g);
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
