import java.awt.Graphics2D;
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
	private static final int MINIMAP_W = 250, MINIMAP_H = 250;
	
	// Members
	private Map m_map;
	private Player m_player;
	private RayCaster m_caster;
	private Minimap m_minimap;
	
	private ArrayList<Entity> m_entities;

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

		int sndWaka = Loader.loadSound("/resource/sound/waka.wav");
		int sndSiren = Loader.loadSound("/resource/sound/siren.wav");
		int sndOpen = Loader.loadSound("/resource/sound/open_sound.wav");
		int sndPause = Loader.loadSound("/resource/sound/pause.wav");
		int sndLife = Loader.loadSound("/resource/sound/life.wav");
		int sndDie = Loader.loadSound("/resource/sound/die.wav");
		int sndGhost = Loader.loadSound("/resource/sound/ghost.wav");
		int sndCherry = Loader.loadSound("/resource/sound/cherry.wav");

		// Variable initialization code HERE

		BufferedImage[][][] texture_walls = Content.processWalls(texWalls, 5, Settings.STRIP_WIDTH);
		BufferedImage[][] texture_ghosts = Content.processGhosts(texGhosts, 8, 5);
		BufferedImage[] texture_pills = Content.processPills(texPills, 10, 128);

		m_map = new Map(mapWalls, mapSprites, mapPaths);

		m_player = new Player(m_map.getStartX(), m_map.getStartY(), 0.0);
		
		m_caster = new RayCaster(texture_walls, texture_pills);
		
		m_minimap = new Minimap(WIDTH - MINIMAP_W - 10, 10, MINIMAP_W, MINIMAP_H, 0,0);
		
		m_entities = new ArrayList<Entity>();
		//m_entities.add();
	}

	protected void update(long timePassed) {
		// Update code HERE

		m_player.update(timePassed, m_map.m_map);
		
		m_caster.update(m_player, m_map.m_map, m_map.m_sprite_map, m_entities);
		
		m_minimap.update(m_player, m_map.m_map, m_map.m_sprite_map, m_entities);

	}

	protected void draw(Graphics2D g) {
		// Draw code HERE (No update calls!)
		
		m_caster.draw(g);
		m_minimap.draw(g);
	}

}
