import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class PointScreen extends DrawableImage {

	// Constants
	private static final int DISPLAY_SCORES = 3;
	private static final float SCORE_HEIGHT = 64f, DIVIDER_HEIGHT = 10f, FONT_SIZE = 24f, HS_OFFSET = 150f, DATA_HEIGHT = 30f, MIN_FLASH = 0.3f, FLASH_LEN = 100;
	private static final Color TEXT_COLOUR = new Color(221,221,221);
	
	// Members
	private ArrayList<Score> m_highscores;
	private Font m_font;
	private DrawableImage m_emptyImage;
	
	private int m_score, m_ghosts, m_ammo, m_fx = 0;
	private String m_date = "";
	private float m_flash = MIN_FLASH;
	
	protected PointScreen(int img, int eimg, int fnt, float x, float y, float w, float h, float alpha) {
		super(img, x, y, w, h, w/2.0f, h/2.0f, 0, alpha);
		m_highscores = new ArrayList<Score>();
		m_font = Loader.getFont(fnt);
		m_emptyImage = new DrawableImage(eimg, x, y - (h/2.0f) + HS_OFFSET, w, SCORE_HEIGHT, w/2.0f, SCORE_HEIGHT/2.0f, 0, alpha);
	}
	
	public void setData(int score, int ghosts, int ammo, String date) {
		m_score = score;
		m_ghosts = ghosts;
		m_ammo = ammo;
		m_date = date;
	}
	
	public void update(int timePassed) {
		m_fx += timePassed;
		m_flash = (float) Math.max(Math.min((((getAlpha() - MIN_FLASH) / 2.0f) * Math.sin(m_fx / FLASH_LEN)) + MIN_FLASH + ((getAlpha() - MIN_FLASH) / 2.0f), 1), 0);
	}

	protected void drawContent(Graphics2D g) {
		Font std = g.getFont();
		g.setFont(m_font);
		g.setColor(TEXT_COLOUR);
		
		g.drawString("Score", (getX()-getOX()+10), (int)(getY()-getOY()));
		drawStringRight(g, ""+m_score, (getX()+getOX()-10), (getY()-getOY()));
				
		g.drawString("Xeno's Killed", (getX()-getOX()+10), (int)(getY()-getOY()) + (DATA_HEIGHT));
		drawStringRight(g, ""+m_ghosts, (getX()+getOX()-10), (getY()-getOY()) + (DATA_HEIGHT));
		
		g.drawString("Ammo Crates", (getX()-getOX()+10), (int)(getY()-getOY()) + (DATA_HEIGHT * 2));
		drawStringRight(g, ""+m_ammo+"/4", (getX()+getOX()-10), (getY()-getOY()) + (DATA_HEIGHT * 2));
		
		if (m_highscores.size() == 0) {
			m_emptyImage.draw(g);
		} else {
		
			for (int i = 0; i < m_highscores.size(); i++) {
				
				if (i > 0) {
					g.drawImage(getImg(), (int)(getX()-getOX()), (int)(getY()-getOY() + HS_OFFSET + (SCORE_HEIGHT * (i-1)) + 5), (int)(getW()), (int)(DIVIDER_HEIGHT), null);
				}
				
				AlphaComposite stdA = (AlphaComposite)g.getComposite();
				
				if (m_date.equals(m_highscores.get(i).getDate())) {
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)m_flash));
				}
				
				g.drawString(m_highscores.get(i).getName(), (getX()-getOX()+10), (int)(getY()-getOY()) + HS_OFFSET + (SCORE_HEIGHT * i));
				drawStringRight(g, ""+m_highscores.get(i).getScore(), (getX()+getOX()-10), (int)(getY()-getOY()) + HS_OFFSET + (SCORE_HEIGHT * i));
				
				g.setComposite(stdA);
			}
			
		}
		g.setFont(std);
	}
	
	private void drawStringRight(Graphics2D g, String str, float x, float y) {
		g.drawString(str, x - (str.length() * FONT_SIZE), y);
	}
	
	public void readDB() {
		m_highscores = DatabaseManager.PullScores("SELECT `name`,`date`,`score` FROM `Scores` ORDER BY `score` DESC, `id` DESC LIMIT "+DISPLAY_SCORES);
	}

}
