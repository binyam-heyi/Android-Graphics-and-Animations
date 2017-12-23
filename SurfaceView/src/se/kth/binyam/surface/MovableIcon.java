package se.kth.binyam.surface;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * A representation of a moving icon.
 * Use draw(Canvas) to draw the icon 
 * in a graphical context.
 */
public class MovableIcon {
	
	private float posX, posY; // position
	private float dX, dY; // velocity
	private Drawable sprite; // a representation of the icon image
	
	public MovableIcon(float posX, float posY, Drawable sprite) {
		this.posX = posX; this.posY = posY;
		this.sprite = sprite;
	}
	
	public void setPosition(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public float getPosX() { return posX; }
	public float getPosY() { return posY; }
	
	public void setVelocity(float dX, float dY) {
		this.dX = dX; this.dY = dY;
	}
	
	/** Move the icon one step (speedX, speedY)
	 */
	public void move() {
		posX += dX; posY += dY;
	}
	
	public Rect getIconBounds() {
		return new Rect((int)posX, (int)posY, 
				(int)posX+sprite.getIntrinsicWidth(), 
				(int)posY+sprite.getIntrinsicHeight());
	}
	
	/** Paint this icon on the specified canvas
	 */	
	public void draw(Canvas canvas) {
		// Where to draw the icon
		sprite.setBounds(this.getIconBounds());
		// Draw
		sprite.draw(canvas);
	}
}