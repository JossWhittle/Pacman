import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	private static final int MINIMAP_SIZE = 175, UBER = 10000, AMMO_DROP = 25;
	
	private static final int GAME_MODE_PLAY = 0, GAME_MODE_PAUSE = 1, GAME_MODE_DEAD = 2, GAME_MODE_DYING = 3, GAME_MODE_MENU = 4;
	
	// Members
	private int m_gameMode = GAME_MODE_PLAY;
	
	private Map m_map;
	private Player m_player;
	private RayCaster m_caster;
	private Minimap m_minimap;
	private HUD m_hud;
	
	private DrawableImage m_gun, m_killSprite;
	
	private ArrayList<Entity> m_entities;
	
	private boolean LEFT = false, RIGHT = false, FORWARD = false, BACK = false, SPRINT = false;
	
	private SoundChannel sndWaka;
	private Sound sndSiren, sndOpen, sndPause, sndLife, sndDie, sndGhost, sndCherry;
	
	private Fader m_vignette, m_blackFade;
	
	private Mouse m_fpsMouse;

	private int m_uber = 0, m_ammo = 0, m_pillCount = 0, m_pillStart, SCORE_X, SCORE_Y;
	
	private float m_mouseX = 0.0f;
	
	private int m_level = 0, m_levelIndex = 0;
	private GameLevel[] m_levels = {
		new GameLevel(new GameEvent[]{
			new ScatterEvent(0),
			new ChaseEvent(7),
			new ScatterEvent(27),
			new ChaseEvent(34),
			new ScatterEvent(54),
			new ChaseEvent(59),
			new ScatterEvent(79),
			new ChaseEvent(64)
		}),
		new GameLevel(new GameEvent[]{
			new ScatterEvent(0),
			new ChaseEvent(7),
			new ScatterEvent(27),
			new ChaseEvent(34),
			new ScatterEvent(54),
			new ChaseEvent(59),
			new ScatterEvent(1092),
			new ChaseEvent(1092+(1.0f/60.0f))
		}),
		new GameLevel(new GameEvent[]{
			new ScatterEvent(0),
			new ChaseEvent(7),
			new ScatterEvent(27),
			new ChaseEvent(34),
			new ScatterEvent(54),
			new ChaseEvent(59),
			new ScatterEvent(1096),
			new ChaseEvent(1096+(1.0f/60.0f))
		})
	};
	
	public Game() {
		init();

		// Texture loading code HERE
		
		int vignette = Loader.loadImage("/resource/texture/effects/vignette.png");

		int[][] texWalls = {{
			Loader.loadImage("/resource/texture/walls/wall_marine_1.gif"),
			Loader.loadImage("/resource/texture/walls/wall_marine_2.gif"),
			Loader.loadImage("/resource/texture/walls/wall_marine_3.gif"),
			Loader.loadImage("/resource/texture/walls/wall_marine_4.gif")
		},
		{
			Loader.loadImage("/resource/texture/walls/wall_alien_rust_1.gif"),
			Loader.loadImage("/resource/texture/walls/wall_alien_rust_2.gif"),
			Loader.loadImage("/resource/texture/walls/wall_alien_rust_4.gif"),
			Loader.loadImage("/resource/texture/walls/wall_alien_rust_3.gif")
		}};
		
		int[] texXeno = {
			Loader.loadImage("/resource/texture/xeno/xeno_front.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_right.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_left.gif"),
			Loader.loadImage("/resource/texture/xeno/xeno_back.gif")
		};
		
		int texPill = Loader.loadImage("/resource/texture/items/pill.png");
		int texAmmo = Loader.loadImage("/resource/texture/items/ammo.png");
		
		int texGun = Loader.loadImage("/resource/texture/weapons/m41a_small.png");
		
		int texPacman = Loader.loadImage("/resource/texture/hud/pacman.png");
		int texMap = Loader.loadImage("/resource/texture/hud/minimap.png");
		int texHUD = Loader.loadImage("/resource/texture/hud/hud.png");
		int texXenoKill = Loader.loadImage("/resource/texture/hud/xeno_small.png");
		int texProgress = Loader.loadImage("/resource/texture/hud/progress.png");
		int texSprintSymbol = Loader.loadImage("/resource/texture/hud/sprint.png");
		int texAmmoSymbol = Loader.loadImage("/resource/texture/hud/ammo.png");
		
		BufferedImage[][][][] texture_walls = Content.processWalls(texWalls, Settings.STRIP_WIDTH);
		
		BufferedImage[][] texture_xeno = {
			Loader.splitImage(texXeno[0], 50),
			Loader.splitImage(texXeno[1], 100),
			Loader.splitImage(texXeno[2], 100),
			Loader.splitImage(texXeno[3], 50)
		};
		
		// Unload raw walls
		for (int u = 0; u < texWalls.length; u++) {
			for (int v = 0; v < texWalls[u].length; v++) {
				Loader.unloadImage(texWalls[u][v]);
			}
		}
		// Unload raw xeno
		for (int v = 0; v < texXeno.length; v++) {
			Loader.unloadImage(texXeno[v]);
		}

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

		m_map = new Map();

		m_player = new Player(m_map.getStartX()+0.5f, m_map.getStartY()+0.5f, 180f, m_map.m_map);
		
		m_caster = new RayCaster(texture_walls, texPill, texAmmo, m_map.m_map, m_map.m_sprite_map);
		
		m_gun = new DrawableImage(texGun, WIDTH / 2.0f, HEIGHT, 620, 280, 100, 300);
		
		m_minimap = new Minimap(WIDTH - MINIMAP_SIZE - 30, 10, MINIMAP_SIZE, MINIMAP_SIZE, 0,0, m_map.m_map, m_map.m_sprite_map, texMap, texPacman);
		m_hud  = new HUD(10, HEIGHT-35, 250, 100, 0, 100, texHUD, texProgress, texSprintSymbol, texAmmoSymbol);
		
		m_vignette = new Fader(vignette, 0,0,WIDTH,HEIGHT);
		m_blackFade = new Fader(FLOOR, 0,0,WIDTH,HEIGHT);	
		m_blackFade.setAlpha(0f);
		m_blackFade.setFadeTarget(new float[][]{{0.25f, 2000f},{1f, 1000f}});
		
		m_killSprite = new DrawableImage(texXenoKill,-150,100,WIDTH,HEIGHT,0 ,0, 350, 1);
		
		
		m_entities = new ArrayList<Entity>();
		m_entities.add(new AI_Blinky(texture_xeno, m_map.getEnemyX(), m_map.getEnemyY(), m_map.m_path_map));
		m_entities.add(new AI_Pinky(texture_xeno, m_map.getEnemyX(), m_map.getEnemyY(), m_map.m_path_map));
		m_entities.add(new AI_Inky(texture_xeno, m_map.getEnemyX(), m_map.getEnemyY(), m_map.m_path_map));
		m_entities.add(new AI_Clyde(texture_xeno, m_map.getEnemyX(), m_map.getEnemyY(), m_map.m_path_map));
		
		SCORE_X = WIDTH - 110;
		SCORE_Y = MINIMAP_SIZE + 30;
		m_pillStart = m_map.getSpriteCount();
		
		try {
			m_fpsMouse = new Mouse();
			addMouseMotionListener(m_fpsMouse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Map size " + m_map.m_map.length + ", " + m_map.m_map[0].length);
	}

	protected void update(int timePassed) {
		// Update code HERE
		
		if (m_gameMode == GAME_MODE_PLAY) {
			m_levels[m_levelIndex].update(timePassed, m_entities);

			if (m_uber > 0) {
				m_uber -= timePassed;
				if (m_uber <= 0) {
					m_uber = 0;
					sndSiren.stop();
					m_vignette.setFadeTarget(new float[][]{{1f,2000f}});
				}
			}

			normalizeMouse(timePassed, m_fpsMouse.getDX());
			m_fpsMouse.clear();
			m_player.setTurn(Math.max(Math.min(m_mouseX, 5),-5));
			
			m_player.setSprint(SPRINT);
			
			float speed = 0.0f;
			if (FORWARD) {
				speed += Player.FORWARD;
			}
			if (BACK) {
				speed += Player.BACK;
			}
			m_player.setSpeed(speed);

			float strafe = 0.0f;
			if (LEFT) {
				strafe += Player.STRAFE_LEFT;
			}
			if (RIGHT) {
				strafe += Player.STRAFE_RIGHT;
			}
			m_player.setStrafe(strafe);
			
			m_player.update(timePassed);
			
			m_hud.update(m_player);

			int px = (int) Math.floor(m_player.getX());
			int py = (int) Math.floor(m_player.getY());
			if (m_map.m_sprite_map[px][py] >= Map.SPRITE_PILL) {
				m_map.m_sprite_map[px][py] = 0;
				m_pillCount++;
				
				if (m_pillCount >= m_pillStart) {
					nextLevel();
				}
				
				if (m_uber == 0) {
					//sndWaka.play();
					m_vignette.setFadeTarget(new float[][]{{0.8f,50f},{1f,100f}});
				}
			} else if (m_map.m_sprite_map[px][py] == Map.SPRITE_MEGA) {
				m_map.m_sprite_map[px][py] = 0;
				
				m_player.giveAmmo(AMMO_DROP);
				
				m_vignette.setFadeTarget(new float[][]{{0.75f,50f}});
				//sndSiren.loop();
				m_uber += UBER;
			}
			
			for (int i = 0; i < m_entities.size(); i++) {
				m_entities.get(i).update(timePassed, m_player);
				
				if (m_entities.get(i) instanceof AI_Inky) {
					((AI_Inky)m_entities.get(i)).setBlinky(m_entities.get(0).getX(), m_entities.get(0).getY());
				}
				
				if ((int)m_entities.get(i).getX() == (int)m_player.getX() && (int)m_entities.get(i).getY() == (int)m_player.getY()) {
					// You dead
					
					m_gameMode = GAME_MODE_DYING;
					sndDie.play();
				}
			}
			
			m_caster.update(m_player, m_entities);
			m_minimap.update(m_player, m_entities);
			m_vignette.update(timePassed);
			
		} else if (m_gameMode == GAME_MODE_DYING) {
			
			m_blackFade.update(timePassed);
		} else if (m_gameMode == GAME_MODE_DEAD) {
			
			
		} 
	}

	protected void draw(Graphics2D g) {
		// Draw code HERE (No update calls!)
		
		if (m_gameMode == GAME_MODE_PLAY || m_gameMode == GAME_MODE_DYING) {
			g.setColor(CEILING);
			g.fillRect(0, 0, WIDTH, (int) (HEIGHT / 2.0));
			g.setColor(FLOOR);
			g.fillRect(0, (int) (HEIGHT / 2.0), WIDTH, (int) (HEIGHT / 2.0));
			
			m_caster.draw(g);
			
			if (m_gameMode == GAME_MODE_DYING) {
				m_killSprite.draw(g);
			}
			
			m_gun.draw(g);
			
			m_vignette.draw(g);
			
			if (m_gameMode == GAME_MODE_PLAY) {
				m_minimap.draw(g);
				m_hud.draw(g);
			}
			
			if (m_gameMode == GAME_MODE_DYING) {
				m_blackFade.draw(g);
			}
		} else if (m_gameMode == GAME_MODE_DEAD) {
			
		}
		
		/*g.setColor(Color.yellow);
		g.drawString("" + m_pillCount, SCORE_X, SCORE_Y);
		g.drawString("/", SCORE_X + 40, SCORE_Y);
		g.drawString("" + m_pillStart, SCORE_X + 50, SCORE_Y);*/
		
	}
	
	private void normalizeMouse(int time_d, float realx) {
	    float d = (float)(1.0f - Math.exp(Math.log(0.5f) * 100.0f * (float)(time_d / 1000.0f)));
	    m_mouseX += ((realx * Settings.MOUSE_X) - m_mouseX) * d;
	}
	
	private void nextLevel() {
		m_level++;
		
		if (m_level >= 2 && m_level < 5) {
			m_levelIndex = 1;
		} else if (m_level >= 5) {
			m_levelIndex = 2;
		}
	}
	
	/*
	 * Keyboard Handlers
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			SPRINT = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			FORWARD = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			BACK = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			LEFT = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			RIGHT = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			SPRINT = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			FORWARD = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			BACK = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			LEFT = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			RIGHT = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

}
