import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Highscores extends DrawableImage {

	// Constants
	private static final int DISPLAY_SCORES = 5;
	private static final float SCORE_HEIGHT = 64f, DIVIDER_HEIGHT = 10f, FONT_SIZE = 24f;
	private static final Color TEXT_COLOUR = new Color(221,221,221);
	
	// Members
	private ArrayList<Score> m_highscores;
	private Font m_font;
	private DrawableImage m_emptyImage;
	
	protected Highscores(int img, int eimg, int fnt, float x, float y, float w, float h, float alpha) {
		super(img, x, y, w, h, w/2.0f, h/2.0f, 0, alpha);
		m_highscores = new ArrayList<Score>();
		m_font = Loader.getFont(fnt);
		m_emptyImage = new DrawableImage(eimg, x, y - (h/2.0f), w, SCORE_HEIGHT, w/2.0f, SCORE_HEIGHT/2.0f, 0, alpha);
	}

	protected void drawContent(Graphics2D g) {
		if (m_highscores.size() == 0) {
			m_emptyImage.draw(g);
		} else {
		
			Font std = g.getFont();
			g.setFont(m_font);
			for (int i = 0; i < m_highscores.size(); i++) {
				g.setColor(TEXT_COLOUR);
				if (i > 0) {
					g.drawImage(getImg(), (int)(getX()-getOX()), (int)(getY()-getOY()  + (SCORE_HEIGHT * (i-1)) + 5), (int)(getW()), (int)(DIVIDER_HEIGHT), null);
				}
				g.drawString(m_highscores.get(i).getName(), (getX()-getOX()+10), (int)(getY()-getOY()) + (SCORE_HEIGHT * i));
				g.drawString(""+m_highscores.get(i).getScore(), (getX()+getOX()-10 - (((""+m_highscores.get(i).getScore()).length()) * FONT_SIZE)), (int)(getY()-getOY()) + (SCORE_HEIGHT * i));
			}
			g.setFont(std);
		}
	}
	
	public void readDB() {
		m_highscores = DatabaseManager.PullScores("SELECT `name`,`date`,`score` FROM `Scores` ORDER BY `score` DESC, `id` DESC LIMIT "+DISPLAY_SCORES);
	}

}
