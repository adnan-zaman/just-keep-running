package com.az.jkr;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Superclass of all game objects that move around
 *
 */

public abstract class MovingGameObject extends GameObject {

	//how long a walljump locks horizontal movement
	protected final long wallJumpTime = 250;
	
	//max horizontal speed
	protected float maxSpeedX;
	//how hard does this game object jump
	protected float jumpForce;
	//forward direction
	protected int forwardX;


	public MovingGameObject(float x, float y, float width, float height, ID id, Color c) {
		this(x, y, width, height, 100, 5,id, c);
	}
	
	public MovingGameObject(float x, float y, float width, float height, float hp, float maxSpeedX, ID id, Color c) {
		this(x, y, width, height, hp, maxSpeedX, -5, id, c);
	}
	
	public MovingGameObject(float x, float y, float width, float height, float hp, float maxSpeedX, float jumpForce, ID id, Color c) {
		super(x, y, width, height, id, c);
		this.maxSpeedX = maxSpeedX;
		this.jumpForce = jumpForce;
	}
	
	/**
	 * Assuming a moving game objects moves.
	 * Movement done by updating x and y according to velX and velY.
	 * Subclasses can use this.
	 */
	public void tick()
	{
		x += velX;
		y += velY;
	}
	
	/**
	 * Utility method for subclasses.
	 * Takes a rectangle, and updates width,height and positions
	 * rectangle so that it's centered on the game object
	 * 
	 * @param 	r	the current rectangle to set collider as
	 */
	protected void rectSetCollider(Rectangle r)
	{

		setWidth((float)r.getWidth());
		setHeight((float)r.getHeight());
		r.setLocation((int)(getX() - getWidth()/2), (int)(getY() - getHeight()/2));
		Rectangle oldCollider = (Rectangle)collider;
		/*
		 * different colliders may have different heights
		 * if gameobject was on the ground,
		 * ensure that this new collider, regardless of height
		 * stays on the ground
		 */
		if (isOnGround())
		{
			
			//in CollisionHandler, y pos is set to be height/2 above
			//the collision point. subtract y pos to get y coord of the ground
			float trueGround = (float)(getY() + oldCollider.getHeight()/2);
			//y pos of the bottom of the current collider with the current y pos
			float currGround = (float)(getY() + r.getHeight()/2);
			//translate r so that it's bottom is at the old ground
			int diff = (int)(trueGround - currGround);
			r.translate(0, diff);
			//y must also change so that it remains in the center of the collider
			setY(getY() + diff); 
		}
		
		//same thing that went for on ground goes for on wall
		if (isOnWall())
		{
			//+/- getWidth depends on if we're on left or right wall
			//which can be determined by forwardX
			float trueWall = (float)(getX() + (oldCollider.getWidth()/2) * forwardX);
			float currWall = (float)(getX() + (r.getWidth()/2) * forwardX);
			int diff = (int)(trueWall - currWall);
			r.translate(diff, 0);
			setX(getX() + diff); 
		}
		
		collider = r;
	}
	
	
	//getters and setters
	
	
	public float getMaxSpeedX() {
		return maxSpeedX;
	}

	protected int getForwardX() {
		return forwardX;
	}

	protected void setForwardX(int forwardX) {
		this.forwardX = forwardX;
	}

}
