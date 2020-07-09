package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * this is a camera
 *
 */
public class Camera {

	private float x,y;
	private Rectangle viewingArea;
	public boolean followingPlayer;
	//testing
	public int thingsDrawn;

	public Camera()
	{
		this(0,0);
	}

	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
		viewingArea = new Rectangle(0,0,Main.width,Main.height);
		followingPlayer = true;

	}

	public void tick()
	{
		if (followingPlayer)
		{
			setX(Main.gameObjectHandler.player.getX() - Main.width/2);
			setY(Main.gameObjectHandler.player.getY() - Main.height/2);

		}



	}

	public void thingsDrawn()
	{
		System.out.println("Things drawn: " + thingsDrawn);
		thingsDrawn = 0;
	}

	public void showViewArea(Graphics2D g2)
	{
		g2.setColor(Color.red);
		g2.draw(viewingArea);
	}

	public void showFPS(Graphics2D g2)
	{

		g2.setColor(Color.white);
		g2.drawString(Main.FPS+"",100,100);
	}

	public void drawRect(Graphics2D g2, Rectangle r, boolean isUI)
	{
		int camX = (isUI) ? (int)r.x : (int)worldToCam(r.x,r.y)[0];
		int camY = (isUI) ? (int)r.y : (int)worldToCam(r.x,r.y)[1];
		//assuming r is axis-aligned, r is completely out of the view area, so don't draw
		if (notInView(r, camX, camY))
			return;
		thingsDrawn++;
		g2.drawRect(camX, camY, r.width, r.height);
	}

	public void fillRect(Graphics2D g2, Rectangle r, boolean isUI)
	{
		int camX = (isUI) ? (int)r.x : (int)worldToCam(r.x,r.y)[0];
		int camY = (isUI) ? (int)r.y : (int)worldToCam(r.x,r.y)[1];
		//assuming r is axis-aligned, r is completely out of the view area, so don't draw
		if (notInView(r, camX, camY))
			return;
		thingsDrawn++;
		g2.fillRect(camX, camY, r.width, r.height);
	}

	/**
	 * Draws a sprite
	 * @param g2		graphics2d context
	 * @param sprite	sprite to ddraw
	 * @param x			top left corner x coord
	 * @param y			top left corner y coord
	 * @param flip		horizontally flip image or not
	 * @param isUI		is sprite being drawn a UI object
	 */
	public void drawSprite(Graphics2D g2, BufferedImage sprite, float x, float y, boolean flip, boolean isUI)
	{
		int camX = (isUI) ? (int)x : (int)worldToCam(x,y)[0];
		int camY = (isUI) ? (int)y : (int)worldToCam(x,y)[1];
		if (notInView(sprite,camX,camY))
			return;
		thingsDrawn++;
		AffineTransform tx = new AffineTransform();
		tx.translate(camX, camY);
		if (flip)
		{
			tx.scale(-1, 1);
			tx.translate(-sprite.getWidth(), 0);
		}
		g2.drawImage(sprite, tx, null);
	}



	public void drawString(Graphics2D g2, String s, float x, float y)
	{
		float[] camCoords = worldToCam(x,y);

		g2.drawString(s, camCoords[0], camCoords[1]);
	}

	private float[] worldToCam(float worldX, float worldY)
	{
		float[] coords = new float[2];
		coords[0] = (worldX - getX());
		coords[1] = (worldY - getY());

		return coords;
	}

	private boolean notInView(Rectangle r, float camX, float camY)
	{
		return !viewingArea.contains(camX, camY) &&
				!viewingArea.contains(camX + r.width, camY) &&
				!viewingArea.contains(camX, camY + r.height) &&
				!viewingArea.contains(camX + r.width, camY + r.height);
	}

	private boolean notInView(BufferedImage sprite, float camX, float camY)
	{
		return 	!viewingArea.contains(camX, camY) &&
				!viewingArea.contains(camX + sprite.getWidth(), camY) &&
				!viewingArea.contains(camX, camY + sprite.getHeight()) &&
				!viewingArea.contains(camX + sprite.getWidth(), camY + sprite.getHeight());
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}


}
