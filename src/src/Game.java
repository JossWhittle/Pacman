import java.awt.Color;
import java.awt.Font;
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
	private static final int MINIMAP_SIZE = 175, UBER = 10000, AMMO_DROP = 25, SCORE_PILL = 10, SCORE_AMMO = 50, SCORE_GHOST = 200;
	
	private static final int GAME_MODE_PLAY = 0, GAME_MODE_PAUSE = 1, GAME_MODE_DEAD = 2, GAME_MODE_DYING = 3, GAME_MODE_MENU = 4, GAME_MODE_HIGHSCORES = 5, GAME_MODE_SETTINGS = 6, GAME_MODE_PLAY_INTRO = 7, GAME_MODE_CONFIRM_QUIT = 8, GAME_MODE_CONFIRM_MENU = 9, GAME_MODE_NAME = 10;
	private static final int MENU_ITEM_WIDTH = 512, MENU_ITEM_HEIGHT = 64, MENU_ITEM_OFFSET = 64, MENU_ITEM_1 = -2, MENU_ITEM_2 = -1, MENU_ITEM_3 = 0, MENU_ITEM_4 = 1, MENU_ITEM_5 = 2;
	
	private static final float PLAYER_DEF_ROT = 180f, PLAYER_INTRO_ROT = 90f, GAME_MODE_PLAY_INTRO_DURATION = 1000f;
	
	// Members
	private int m_gameMode = GAME_MODE_MENU;
	
	private Map m_map;
	private Player m_player;
	private RayCaster m_caster;
	private Minimap m_minimap;
	private HUD m_hud;
	
	private DrawableImage m_gun, m_killSprite, m_menuPaused, m_menuConfirmQuit, m_menuConfirmMenu, m_highscoreBanner, m_pointBanner, m_nameBanner;
	
	private Highscores m_highscores;
	private PointScreen m_points;
	private NameBox m_nameBox;
	
	private ClickableImage m_menuPlay, m_menuResume, m_menuSettings, m_menuHighscores, m_menuExit, m_menuMenu, m_menuConfirmYes, m_menuConfirmNo, m_highscoreBack, m_menuContinue;
	private Cursor m_cursor;
	
	private ArrayList<Entity> m_entities;
	
	private boolean LEFT = false, RIGHT = false, FORWARD = false, BACK = false, SPRINT = false;
	
	private SoundChannel sndWaka;
	private Sound sndSiren, sndOpen, sndPause, sndLife, sndDie, sndGhost, sndCherry;
	
	private Fader m_vignette, m_blackFade;
	
	private Mouse m_fpsMouse;
	private NameKeyboard m_nameKeyboard;

	private int m_uber = 0, m_ammo = 0, m_pillCount = 0, m_pillStart, SCORE_X, SCORE_Y, m_score = 0, m_ghostsKilled = 0, m_ammoCrates = 0;
	
	private float m_mouseX = 0.0f, m_gameIntroRot = PLAYER_DEF_ROT, m_gameIntroTime = 0;
	
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
		
		int texCursor = Loader.loadImage("/resource/texture/menu/cursor_rad.png");
		int texMenuPaused = Loader.loadImage("/resource/texture/menu/banner_paused.png");
		int texMenuPlay = Loader.loadImage("/resource/texture/menu/item_play.png");
		int texMenuConfirmQuit = Loader.loadImage("/resource/texture/menu/item_confirm_quit.png");
		int texMenuConfirmMenu = Loader.loadImage("/resource/texture/menu/item_confirm_menu.png");
		int texMenuConfirmYes = Loader.loadImage("/resource/texture/menu/item_yes.png");
		int texMenuConfirmNo = Loader.loadImage("/resource/texture/menu/item_no.png");
		int texMenuResume = Loader.loadImage("/resource/texture/menu/item_resume.png");
		int texMenuExit = Loader.loadImage("/resource/texture/menu/item_exit.png");
		int texMenuMenu = Loader.loadImage("/resource/texture/menu/item_menu.png");
		int texMenuHighscore = Loader.loadImage("/resource/texture/menu/item_highscores.png");
		int texMenuSettings = Loader.loadImage("/resource/texture/menu/item_settings.png");
		
		int texDivider = Loader.loadImage("/resource/texture/highscores/divider.png");
		int texEmpty = Loader.loadImage("/resource/texture/highscores/no_scores.png");
		int texMenuBack = Loader.loadImage("/resource/texture/menu/item_back.png");
		
		int texMenuPoints = Loader.loadImage("/resource/texture/menu/banner_points.png");
		int texMenuContinue = Loader.loadImage("/resource/texture/menu/item_continue.png");
		
		int texMenuName = Loader.loadImage("/resource/texture/menu/banner_name.png");
		int texNameBox = Loader.loadImage("/resource/texture/menu/name_box.png");
		
		BufferedImage[][][][] texture_walls = Content.processWalls(texWalls, Stitch.STRIP_WIDTH);
		
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
		
		// Font loading code HERE
		
		int fntMain = Loader.loadFont("/resource/font/prstartk.ttf", 24f);
		
		// Variable initialization code HERE
		
		m_cursor = new Cursor(texCursor,WIDTH/2.0f,HEIGHT/2.0f,20,20, 0.5f);
		m_menuPaused = new DrawableImage(texMenuPaused,WIDTH/2.0f,50,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,0);
		m_menuPlay = new ClickableImage(texMenuPlay,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_1 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_menuResume = new ClickableImage(texMenuResume,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_1 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_menuHighscores = new ClickableImage(texMenuHighscore,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_2 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_menuSettings = new ClickableImage(texMenuSettings,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_3 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		
		m_menuExit = new ClickableImage(texMenuExit,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_4 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_menuMenu = new ClickableImage(texMenuMenu,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_2 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);

		m_menuConfirmQuit = new DrawableImage(texMenuConfirmQuit,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_1 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,64,0,0.6f);
		m_menuConfirmMenu = new DrawableImage(texMenuConfirmMenu,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_1 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,64,0,0.6f);
		m_menuConfirmYes = new ClickableImage(texMenuConfirmYes,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_2 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_menuConfirmNo = new ClickableImage(texMenuConfirmNo,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_3 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		
		m_highscoreBanner = new DrawableImage(texMenuHighscore,WIDTH/2.0f,50,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,0);
		m_highscores = new Highscores(texDivider, texEmpty, fntMain, WIDTH/2.0f, HEIGHT/2.0f, MENU_ITEM_WIDTH, (MENU_ITEM_HEIGHT * 5), 0.8f);
		m_highscoreBack = new ClickableImage(texMenuBack,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_5 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		
		m_pointBanner = new DrawableImage(texMenuPoints,WIDTH/2.0f,50,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,0);
		m_menuContinue = new ClickableImage(texMenuContinue,WIDTH/2.0f,(HEIGHT/2.0f)+(MENU_ITEM_5 * MENU_ITEM_HEIGHT)+MENU_ITEM_OFFSET,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT);
		m_points = new PointScreen(texDivider, texEmpty, fntMain, WIDTH/2.0f, HEIGHT/2.0f, MENU_ITEM_WIDTH, (MENU_ITEM_HEIGHT * 5), 0.8f);
		
		m_nameBanner = new DrawableImage(texMenuName,WIDTH/2.0f,50,MENU_ITEM_WIDTH,MENU_ITEM_HEIGHT,256,0);
		m_nameBox = new NameBox(texNameBox, fntMain, WIDTH/2.0f, HEIGHT/2.0f, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, 0.8f);
		
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
		
		m_pillStart = m_map.getSpriteCount();
		
		try {
			m_fpsMouse = new Mouse();
			addMouseMotionListener(m_fpsMouse);
			
			m_nameKeyboard = new NameKeyboard();
			addKeyListener(m_nameKeyboard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Map size " + m_map.m_map.length + ", " + m_map.m_map[0].length);
	}

	protected void update(int timePassed) {
		// Update code HERE
		if (m_gameMode == GAME_MODE_MENU || m_gameMode == GAME_MODE_PAUSE) {
			
			m_cursor.update(timePassed,MOUSE_MENU_X, MOUSE_MENU_Y);
			
			if (m_gameMode == GAME_MODE_MENU) {
				m_menuPlay.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				m_menuExit.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				m_menuHighscores.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				m_menuSettings.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				
				if (m_menuPlay.stateChanged() && m_menuPlay.isActive()) {
					LEFT_MOUSE = false;
					gotoNewGame();
				} else if (m_menuHighscores.stateChanged() && m_menuHighscores.isActive()) {
					LEFT_MOUSE = false;
					gotoHighscores();
				} else if (m_menuSettings.stateChanged() && m_menuSettings.isActive()) {
					LEFT_MOUSE = false;
					//gotoSettings();
				} else if (m_menuExit.stateChanged() && m_menuExit.isActive()) {
					LEFT_MOUSE = false;
					gotoConfirmQuit();
				}
			} else {
				m_menuResume.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				m_menuMenu.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
				
				if (m_menuResume.stateChanged() && m_menuResume.isActive()) {
					LEFT_MOUSE = false;
					gotoPlay();
				} else if (m_menuMenu.stateChanged() && m_menuMenu.isActive()) {
					LEFT_MOUSE = false;
					gotoConfirmMenu();
				}
			}
			
			
		} else if (m_gameMode == GAME_MODE_CONFIRM_QUIT || m_gameMode == GAME_MODE_CONFIRM_MENU) {
			
			m_cursor.update(timePassed,MOUSE_MENU_X, MOUSE_MENU_Y);
			
			m_menuConfirmYes.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
			m_menuConfirmNo.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
			
			if (m_gameMode == GAME_MODE_CONFIRM_QUIT) {
				
				if (m_menuConfirmYes.stateChanged() && m_menuConfirmYes.isActive()) {
					LEFT_MOUSE = false;
					System.exit(0);
				} else if (m_menuConfirmNo.stateChanged() && m_menuConfirmNo.isActive()) {
					LEFT_MOUSE = false;
					gotoMenu();
				}
				
			} else {
				
				if (m_menuConfirmYes.stateChanged() && m_menuConfirmYes.isActive()) {
					LEFT_MOUSE = false;
					gotoMenu();
				} else if (m_menuConfirmNo.stateChanged() && m_menuConfirmNo.isActive()) {
					LEFT_MOUSE = false;
					gotoPause();
				}

			}
			
		} else if (m_gameMode == GAME_MODE_HIGHSCORES) {
		
			m_cursor.update(timePassed,MOUSE_MENU_X, MOUSE_MENU_Y);
			m_highscoreBack.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
			
			if (m_highscoreBack.stateChanged() && m_highscoreBack.isActive()) {
				LEFT_MOUSE = false;
				gotoMenu();
			}
			
		} else if (m_gameMode == GAME_MODE_PLAY_INTRO) {
			
			m_gameIntroTime += timePassed;
			m_player.setRot(Ease.sineInOut(m_gameIntroTime, PLAYER_DEF_ROT, PLAYER_INTRO_ROT, GAME_MODE_PLAY_INTRO_DURATION));
			
			if (m_gameIntroTime >= GAME_MODE_PLAY_INTRO_DURATION) {
				m_player.setRot(PLAYER_DEF_ROT + PLAYER_INTRO_ROT);
				
				gotoPlay();
			}
			
		} else if (m_gameMode == GAME_MODE_PLAY) {
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
				
				m_score += SCORE_PILL;
				
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
				
				m_score += SCORE_AMMO;
				m_ammoCrates++;
				
				m_vignette.setFadeTarget(new float[][]{{0.75f,50f}});
				//sndSiren.loop();
				m_uber += UBER;
			}
			
			for (int i = 0; i < m_entities.size(); i++) {
				m_entities.get(i).update(timePassed, m_player);
				
				if (m_entities.get(i) instanceof AI_Inky) {
					((AI_Inky)m_entities.get(i)).setBlinky(m_entities.get(0).getX(), m_entities.get(0).getY());
				}
				
				if (m_entities.get(i).isAlive() && ((int)m_entities.get(i).getX() == (int)m_player.getX() && (int)m_entities.get(i).getY() == (int)m_player.getY())) {
					// You dead
					
					gotoDying();
					sndDie.play();
				}
			}
			
			m_minimap.update(m_player, m_entities);
			m_vignette.update(timePassed);
			
		} else if (m_gameMode == GAME_MODE_DYING) {
			
			m_blackFade.update(timePassed);
			if (!m_blackFade.isFading()) {
				gotoName();
			}
			
		} else if (m_gameMode == GAME_MODE_NAME) {
			
			m_blackFade.update(timePassed);
			m_nameBox.update(timePassed, m_nameKeyboard.getValue());
			m_cursor.update(timePassed,MOUSE_MENU_X, MOUSE_MENU_Y);
			m_menuContinue.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
			
			if (m_menuContinue.stateChanged() && m_menuContinue.isActive()) {
				LEFT_MOUSE = false;
				if (sendScore()) {
					gotoDead();
				}
			}
			
		} else if (m_gameMode == GAME_MODE_DEAD) {
			
			m_points.update(timePassed);
			m_cursor.update(timePassed,MOUSE_MENU_X, MOUSE_MENU_Y);
			m_menuContinue.update(timePassed, MOUSE_MENU_X, MOUSE_MENU_Y, LEFT_MOUSE);
			
			if (m_menuContinue.stateChanged() && m_menuContinue.isActive()) {
				LEFT_MOUSE = false;		
				gotoMenu();
			}
			
		}
		
		m_caster.update(m_player, m_entities);
	}

	protected void draw(Graphics2D g) {
		// Draw code HERE (No update calls!)
		if (m_gameMode == GAME_MODE_PLAY || m_gameMode == GAME_MODE_PLAY_INTRO || m_gameMode == GAME_MODE_DYING || m_gameMode == GAME_MODE_DEAD || m_gameMode == GAME_MODE_MENU || m_gameMode == GAME_MODE_PAUSE || m_gameMode == GAME_MODE_CONFIRM_QUIT || m_gameMode == GAME_MODE_CONFIRM_MENU || m_gameMode == GAME_MODE_CONFIRM_QUIT || m_gameMode == GAME_MODE_HIGHSCORES || m_gameMode == GAME_MODE_NAME) {
			g.setColor(CEILING);
			g.fillRect(0, 0, WIDTH, (int) (HEIGHT / 2.0f));
			g.setColor(FLOOR);
			g.fillRect(0, (int) (HEIGHT / 2.0f), WIDTH, (int) (HEIGHT / 2.0f));
			
			m_caster.draw(g);
			
			if (m_gameMode == GAME_MODE_DYING) {
				m_killSprite.draw(g);
			}
			
			if (m_gameMode == GAME_MODE_MENU) {
				
				m_menuPlay.draw(g);
				m_menuExit.draw(g);
				m_menuHighscores.draw(g);
				m_menuSettings.draw(g);
			
			} else if (m_gameMode == GAME_MODE_PAUSE) {
				
				m_menuPaused.draw(g);
				m_menuResume.draw(g);
				m_menuMenu.draw(g);
				
			} else if (m_gameMode == GAME_MODE_CONFIRM_QUIT || m_gameMode == GAME_MODE_CONFIRM_MENU) {
				
				if (m_gameMode == GAME_MODE_CONFIRM_QUIT) {
					m_menuConfirmQuit.draw(g);
				} else {
					m_menuConfirmMenu.draw(g);
				}
				
				m_menuConfirmYes.draw(g);
				m_menuConfirmNo.draw(g);
				
			} else if (m_gameMode == GAME_MODE_HIGHSCORES) {
				
				m_highscoreBanner.draw(g);
				m_highscores.draw(g);
				m_highscoreBack.draw(g);
				
			} else if (m_gameMode == GAME_MODE_PLAY || m_gameMode == GAME_MODE_PLAY_INTRO || m_gameMode == GAME_MODE_DYING) {
			
				m_gun.draw(g);
			} else if (m_gameMode == GAME_MODE_DEAD) {
				
				m_pointBanner.draw(g);
				m_points.draw(g);
				m_menuContinue.draw(g);
				
			} else if (m_gameMode == GAME_MODE_NAME) {
				
				m_nameBanner.draw(g);
				m_nameBox.draw(g);
				m_menuContinue.draw(g);
				
			} 
			
			m_vignette.draw(g);
			
			if (m_gameMode == GAME_MODE_MENU || m_gameMode == GAME_MODE_PAUSE || m_gameMode == GAME_MODE_CONFIRM_QUIT || m_gameMode == GAME_MODE_CONFIRM_MENU || m_gameMode == GAME_MODE_HIGHSCORES || m_gameMode == GAME_MODE_DEAD || m_gameMode == GAME_MODE_NAME) {
				m_cursor.draw(g);
			}
			
			if (m_gameMode == GAME_MODE_PLAY || m_gameMode == GAME_MODE_PLAY_INTRO) {
				m_minimap.draw(g);
				m_hud.draw(g);
			}
			
			if (m_gameMode == GAME_MODE_DYING) {
				
				m_blackFade.draw(g);
			} else if (m_gameMode == GAME_MODE_DEAD) {
				
				m_blackFade.draw(g);
				
			}
		}
		
	}
	
	private void normalizeMouse(int time_d, float realx) {
	    float d = (float)(1.0f - Math.exp(Math.log(0.5f) * 100.0f * (float)(time_d / 1000.0f)));
	    m_mouseX += ((realx * Stitch.MOUSE_X) - m_mouseX) * d;
	}
	
	private void nextLevel() {
		m_level++;
		
		if (m_level >= 1 && m_level < 4) {
			m_levelIndex = 1;
		} else if (m_level >= 4) {
			m_levelIndex = 2;
		}
		
		//resetLevel();
	}
	
	private void resetLevel() {
		m_ghostsKilled = 0;
		
		m_player.reset();
		
		m_map.resetSprites();
		
		for (int i = 0; i < m_entities.size(); i++) {
			m_entities.get(i).reset();
		}
		
	}
	
	private void gotoMenu() {
		m_player.reset();
		m_fpsMouse.unhook();
		m_gameMode = GAME_MODE_MENU;
	}
	
	private void gotoPause() {
		m_fpsMouse.unhook();
		m_gameMode = GAME_MODE_PAUSE;
	}
	
	private void gotoPlay() {
		m_fpsMouse.hook();
		m_gameMode = GAME_MODE_PLAY;
	}
	
	private void gotoResetPlay() {
		resetLevel();
		m_fpsMouse.hook();
		m_gameMode = GAME_MODE_PLAY_INTRO;
		m_gameIntroRot = PLAYER_DEF_ROT;
		m_gameIntroTime = 0;
		m_ammoCrates = 0;
	}
	
	private void gotoNewGame() {
		m_score = 0;
		m_level = 0;
		m_levelIndex = 0;
		gotoResetPlay();
	}
	
	private void gotoConfirmQuit() {
		m_gameMode = GAME_MODE_CONFIRM_QUIT;
	}
	
	private void gotoConfirmMenu() {
		m_gameMode = GAME_MODE_CONFIRM_MENU;
	}
	
	private void gotoDying() {
		m_fpsMouse.unhook();
		m_gameMode = GAME_MODE_DYING;
		m_blackFade.setAlpha(0f);
		m_blackFade.setFadeTarget(new float[][]{{0.25f, 2000f},{1f, 1000f}});
	}
	
	private void gotoDead() {
		m_player.reset();
		m_nameKeyboard.unhook();
		m_fpsMouse.unhook();
		m_gameMode = GAME_MODE_DEAD;
		//m_blackFade.setFadeTarget(new float[][]{{0f, 500f}});
		
		m_points.readDB();
	}
	
	private void gotoName() {
		m_player.reset();
		m_nameKeyboard.hook();
		m_fpsMouse.unhook();
		m_gameMode = GAME_MODE_NAME;
		m_blackFade.setFadeTarget(new float[][]{{0f, 500f}});
	}
	
	private void gotoHighscores() {
		m_highscores.readDB();
		m_gameMode = GAME_MODE_HIGHSCORES;
	}
	
	private void gotoSettings() {
		m_gameMode = GAME_MODE_SETTINGS;
	}
	
	private boolean sendScore() {
		if (m_nameKeyboard.isValid()) {
			String date = ""+System.currentTimeMillis();
			DatabaseManager.SendQuery("INSERT INTO `Scores` (`name`,`date`,`score`) VALUES ('"+m_nameKeyboard.getValue().trim()+"','"+date+"','"+m_score+"');");
			m_points.setData(m_score, m_ghostsKilled, m_ammoCrates, date);
			return true;
		}
		return false;
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
			if (m_gameMode == GAME_MODE_MENU) {
				// System.exit(0);
			} else if (m_gameMode == GAME_MODE_PLAY) {
				gotoPause();
			} else if (m_gameMode == GAME_MODE_PAUSE) {
				gotoPlay();
			} else if (m_gameMode == GAME_MODE_DYING) {
				gotoName();
			} else if (m_gameMode == GAME_MODE_DEAD) {
				gotoMenu();
			}
		}
	}
	
	private boolean LEFT_MOUSE = false;
	public void mousePressed(MouseEvent e) {
		LEFT_MOUSE = true;
	}

	public void mouseReleased(MouseEvent e) {
		LEFT_MOUSE = false;
	}

	private float MOUSE_MENU_X = 0, MOUSE_MENU_Y = 0;
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public void mouseMoved(MouseEvent e) {
		MOUSE_MENU_X = e.getX();
		MOUSE_MENU_Y = e.getY();
	}

}
