package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * An invisible game object 
 * to be used by enemies to
 * sense their surroundings (e.g walls, gaps)
 * 
 * They arent added to GameObjectHandler, but instead
 * when an Enemy's intersect() is called, the Enemy
 * will pass the other GameObject to its sensors
 *
 */

public class Sensor extends GameObject {
	
	//colliding with a ceiling tile
	protected boolean onCeiling;
	public boolean one = false;

	public Sensor(float x, float y, float width, float height)
	{
		super(x,y,width,height,ID.Sensor,Color.black);
		isStatic = true;
		onCeiling = false;
	}
	
	@Override
	public void tick()
	{
	
	}
	
	@Override
	public void render(Graphics2D g2)
	{
		//nothing is drawn
	}
	
	@Override
	public void setCollider()
	{
		collider = new Rectangle((int)(getX() - getWidth()/2), (int)(getY() - getHeight()/2),
								(int)getWidth(), (int)getHeight());
	}
	
	@Override
	public void updateCollider()
	{
		collider = new Rectangle((int)(getX() - getWidth()/2), (int)(getY() - getHeight()/2),
				(int)getWidth(), (int)getHeight());
	}

	
	/**
	 * Clears this sensor's state
	 */
	public void clear()
	{
		setOnGround(false);
		setOnWall(false);
		setOnCeiling(false);
	}
	
	@Override
	protected boolean intersects(GameObject other) {
		boolean collision = getCollider().intersects(other.getCollider().getBounds2D());
		if (collision)
		{
			if (other.getId() == ID.Wall)
			{
				if (other.isGround())
					setOnGround(true);
				else if (other.isCeiling())
					setOnCeiling(true);
				String dir = CollisionHandler.getCollisionDirection(this, other);
				if (dir.equals("left") || dir.equals("right"))
				{
					
					setOnWall(true);
					setColor(Color.red);
					other.setColor(Color.red);
				}
					
			}
		}
		return collision;
	}
	
	
	@Override
	public Shape getCollider()
	{
		updateCollider();
		return collider;
	}
	
	public boolean isOnCeiling() {
		return onCeiling;
	}

	public void setOnCeiling(boolean onCeiling) {
		this.onCeiling = onCeiling;
	}

}
