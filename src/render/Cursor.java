import java.awt.image.BufferedImage;


public class Cursor extends DrawableImage {

	// Constants
	private static final float ROT_SPEED = 0.5f;
	
	// Members
	
	public Cursor(int img, float x, float y, float w, float h, float a) {
		super(img, x, y, w, h, w/2.0f, h/2.0f, 0,a);
	}
	
	public void update(int timePassed, float x, float y) {
		rotCW(ROT_SPEED * timePassed);
		setXY(x,y);
	}
}
