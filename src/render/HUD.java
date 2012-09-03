import java.awt.Graphics2D;


public class HUD extends Drawable {

	// Constants
	private static final float HUD_ALPHA = 0.18f;
	
	// Members
	DrawableImage m_hudSprite, m_iconSprint, m_iconAmmo;
	ProgressBar m_progressSprint, m_progressAmmo;
	
	protected HUD(float x, float y, float w, float h, float ox, float oy, int himg, int pimg, int simg, int aimg) {
		super(x, y, w, h, ox, oy, 0, HUD_ALPHA);
		
		m_hudSprite = new DrawableImage(himg, x, y, w, h, ox, oy, 0, HUD_ALPHA);
		m_iconSprint = new DrawableImage(simg, x+232, y-7, 20, 20, 20, 20, 0, HUD_ALPHA);
		m_iconAmmo = new DrawableImage(aimg, x+232, y-30, 20, 20, 20, 20, 0, HUD_ALPHA);
		m_progressSprint = new ProgressBar(pimg, x+10, y-7, 193, 20, ox, 20, 0, HUD_ALPHA);
		m_progressAmmo = new ProgressBar(pimg, x+10, y-30, 193, 20, ox, 20, 0, HUD_ALPHA);
	}
	
	public void update(Player p) {
		m_progressSprint.setProgress(p.getSprintRatio());
		m_iconSprint.setAlpha((p.isSprinting() ? HUD_ALPHA : 0.1f));
	}

	protected void drawContent(Graphics2D g) {
		m_iconSprint.draw(g);
		m_iconAmmo.draw(g);
		m_hudSprite.draw(g);
		m_progressSprint.draw(g);
		m_progressAmmo.draw(g);
	}

}
