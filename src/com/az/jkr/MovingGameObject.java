package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Superclass of all game objects that move around
 *
 */

public abstract class MovingGameObject extends GameObject {

	//how long a walljump locks horizontal movement (in milliseconds)
	protected final long wallJumpTime = 400;
	
	//max horizontal speed
	protected float maxSpeedX;
	//how hard does this game object jump
	protected float jumpForce;
	//forward direction
	protected int forwardX;
	//what horizontal direction game obj is moving in
	protected boolean left, right;
	//set to true after a walljump
	protected boolean wallJumped;
	//set to true after a regular jump
	protected boolean jumped;
	//turn walljumped off after some time
	//to return horizontal movement control
	protected Coroutine wallJumpOff;


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
		left = false;
		right = false;
		wallJumpOff = new Coroutine(wallJumpTime, () -> {wallJumped = false;});
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
	

	/**
	 * (Need colliders and animations set to work)
	 * 
	 * Convenience method to be used game objects
	 * with simple singular rectangle colliders.
	 * 
	 * Sets collider to the collider that
	 * corresponds with the current animation state
	 * and updates collider.
	 */
	protected void rectUpdateCollider()
	{
		//From colliderMap, query the list corresponding to the current anim state
		//then get the rectangle at the index corresponding to the current frame
		//of animation
		Rectangle r = (Rectangle)colliderMap.get(getAnimator().getAnimState())
				.get(getAnimator().getCurrentFrameNumber());
		
		rectSetCollider(r);
	}
	
	/**
	 * Convenience method that checks whether this game object
	 * intersects another game object that's represented as a Rectangle2D
	 * 
	 * @param other the other game object
	 * @return true if collide, false otherwise
	 */
	protected boolean simpleRectIntersect(GameObject other)
	{
		return getCollider().intersects(other.getCollider().getBounds2D());	
	}
	
	/**
	 * Convenience method that renders a game object's sprite
	 * accounting for the fact that it's collider is most likely
	 * shorter than the sprite, and that the collider is slightly
	 * in the ground.
	 * 
	 * @param g2 the Graphics2D context
	 */
	protected void standingOnGroundRender(Graphics2D g2)
	{
		/*
		 * gameobject's position is at the center of the gameobject
		 * however the collider height will most often be shorter than
		 * the sprite height. therefore if we draw the sprite using this
		 * x,y position, the sprite will appear to be in the ground
		 * so we move additionally move the sprite up by the difference
		 * between spriteHeight and getHeight (delta) so that the sprite's
		 * bottom is on the ground.
		 */
		int spriteDelta = 0;
		if (getSpriteHeight() > getHeight())
			spriteDelta = (int)(getSpriteHeight()/2 - getHeight()/2);
		
		//also offset by collisionOffset since colliders are always slightly in ground to maintain collision
		Main.camera.drawSprite(g2, getAnimator().getSprite(), 
			getX()-getSpriteWidth()/2, 
			getY()-getSpriteHeight()/2 - spriteDelta - CollisionHandler.collisionOffset, 
			getForwardX() == -1,
			false);
	}
	
	/**
	 * Causes the game object to jump if possible,
	 * otherwise does nothing.
	 * May be a wall jump if game object is on wall.
	 */
	protected void jump()
	{
		//regular jump
		if (isOnGround())
		{
			setOnGround(false);
			setVelY(jumpForce);
			setJumped(true);
		}
		//wall jump
		else if (isOnWall() && !isOnGround())
		{
			setVelY(jumpForce);
			left = !left;
			right = !right;	
			setWallJumped(true);
			//regain horizontal control after some time
			wallJumpOff.start();
		}
	
	}
	
	
	//getters and setters
	
	
	@Override
	public void setOnGround(boolean onGround)
	{
		
		super.setOnGround(onGround);
		
		//if player has reached the ground and has walljumped
		if (isOnGround() && hasWallJumped())
		{
			//this is so that horizontal movement after
			//a walljump doesn't continue when you've hit the ground
			left = false;
			right = false;
			setWallJumped(false);
		}
		setJumped(false);
	}
	
	public float getMaxSpeedX() {
		return maxSpeedX;
	}
	

	public int getForwardX() {
		return forwardX;
	}

	protected void setForwardX(int forwardX) {
		this.forwardX = forwardX;
	}
	
	public boolean hasWallJumped() {
		return wallJumped;
	}

	protected void setWallJumped(boolean wallJumped) {
		this.wallJumped = wallJumped;
	}
	
	public boolean hasJumped() {
		return jumped;
	}

	protected void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	
	


}
