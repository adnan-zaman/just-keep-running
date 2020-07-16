package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * It's a wall
 *
 */


public class Wall extends GameObject{

	

	
	public Wall(float x, float y) {
		this(x,y,64,64,Color.blue);
	}
	
	public Wall(float x, float y, float width, float height) {
		this(x, y, width, height,Color.blue);
	}
	
	public Wall(float x, float y, float width, float height, Color c) {
		super(x, y, width, height, ID.Wall, c);
		isStatic = true;
	}

	@Override
	public void tick() {
	
		//WALLS DO NOTHING
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setColor(getColor());
		//g2.draw(getCollider());
		Main.camera.drawRect(g2, (Rectangle)getCollider(),false);
	}
	
	public void debugRender(Graphics2D g2)
	{
		
			g2.setColor(color.white);
			Main.camera.drawString(g2, "" + GameObject.getDistance(this,Main.gameObjectHandler.player), getX()-20, getY()-15);
			
	
			
			//g2.drawLine((int)(getX() - getWidth()/2), (int)(getY() - getHeight()/2), 
			//			(int)(getX() + getWidth()/2), (int)(getY() + getHeight()/2));
	}

	@Override
	public void updateCollider() {
		//statics don't update
		
	}
	
	@Override
	public boolean intersects(GameObject other)
	{
		return collider.intersects((other.getCollider().getBounds2D()));
	}

	@Override
	protected void setCollider() {
		rectSetCollider();
		
	}

}
