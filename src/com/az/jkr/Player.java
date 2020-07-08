package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * The player
 * 
 */

public class Player extends MovingGameObject {

	/**
	 * All possible states the player can be in
	 *
	 */
	public enum PlayerState 
	{
		Idle,
		RunLeft,
		RunRight;
	}
	
	
	//current state;
	protected PlayerState state;
	
	//what direction player is moving in
	protected boolean left, right;
	protected boolean up, down;
	//set to true after a walljump
	protected boolean wallJumped;
	
	//turn walljumped off after some time
	//to return horizontal movement control
	protected Coroutine wallJumpOff;
	
	//queue of input events
	private Queue<String> inputQueue;
	
	public Player(float x, float y)
	{
		super(x, y, 64, 64, 100, 7, ID.Player, Color.green);
		state = PlayerState.Idle;
		left = false;
		right = false;
		up = down = false;
		wallJumpOff = new Coroutine(wallJumpTime, () -> {wallJumped = false;});
		inputQueue = new LinkedList<String>();
	}
	
	@Override
	public void tick() 
	{
		processInputs();

		if (left)
		{
			setVelX(-maxSpeedX);
			setForwardX(-1);
		}
		else if (right)
		{
			setVelX(maxSpeedX);
			setForwardX(1);
		}
		else //neither left nor right
		{
			state = PlayerState.Idle;
			setVelX(0);
		}
		
			
		//move
		super.tick();
		
	}

	@Override
	public void render(Graphics2D g2) 
	{
		
		
		/*
		 * player's position is at the center of the gameobject
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
			getX()-getSpriteWidth()/2, getY()-getSpriteHeight()/2 - spriteDelta - CollisionHandler.collisionOffset, getForwardX() == -1 );
		
		
	}
	
	@Override
	public void animTick()
	{
		if (!isOnGround())
		{
			if (getVelY() < 0)
				getAnimator().setAnimState("jump");
			else
				getAnimator().setAnimState("fall");
		}
			
		else
		{
			if (!floatEq(getVelX(),0))
				getAnimator().setAnimState("run");
			else
				getAnimator().setAnimState("idle");
		}
			
		getAnimator().tick();
	}
	
	public void debugRender(Graphics2D g2)
	{

		g2.setColor(Color.white);
		g2.setColor(getColor());
		g2.setColor(Color.green);
		Main.camera.drawRect(g2, (Rectangle)getCollider());
		g2.setColor(Color.white);
		Main.camera.drawRect(g2, new Rectangle((int)getX()-1,(int)getY(),3,3));
//		Main.camera.drawString(g2,"(" + left + "," + right +")", (int)(getX() + getWidth()/2 + 10), (int)getY());
		Main.camera.drawString(g2,"onWall: " + isOnWall(), (int)(getX() + getWidth()/2 + 10), (int)getY() + 10);
		Main.camera.drawString(g2,"onGround: " + isOnGround(), (int)(getX() + getWidth()/2 + 10), (int)getY() + 20);
	
		g2.setColor(Color.white);
	
	
	}

	@Override
	public void updateCollider() 
	{
		//From colliderMap, query the list corresponding to the current anim state
		//then get the rectangle at the index corresponding to the current frame
		//of animation
		Rectangle r = (Rectangle)colliderMap.get(getAnimator().getAnimState())
				.get(getAnimator().getCurrentFrameNumber());
		
		rectSetCollider(r);
		
	}
	
	
	/**
	 * A formatted string containing the input button pressed
	 * and any other info such as whether it was pressed or released
	 * 
	 * @param 	input	the formated input string
	 */
	public void sendInput(String input)
	{
		inputQueue.add(input);
	}
	
	@Override
	public boolean intersects(GameObject other)
	{
//		System.out.println("player is intersecting " + other.getId() + " " 
//				+ collider.intersects(other.getCollider().getBounds2D()));
		return getCollider().intersects(other.getCollider().getBounds2D());
	}
	
	
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
		
	}

	@Override
	protected void setCollider() 
	{
		try
		{
			readColliders("player.hbx");
			rectSetCollider((Rectangle)colliderMap.get("idle").get(0));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	@Override
	protected void setAnimations()
	{
		try {
			spriteWidth = spriteHeight = 64;
			BufferedImage playerSheet = SpriteSheet.getSpriteSheet("player.png");
			getAnimator().addAnimation("run", new Animation(playerSheet, 0, 9, spriteWidth, spriteHeight));
			getAnimator().addAnimation("idle", new Animation(playerSheet, 10, 10, spriteWidth, spriteHeight));
			getAnimator().addAnimation("fall", new Animation(playerSheet, 14, 16, spriteWidth, spriteHeight,false));
			getAnimator().addAnimation("jump", new Animation(playerSheet, 11, 13, spriteWidth, spriteHeight,false));
			getAnimator().setAnimState("idle");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected boolean hasWallJumped() {
		return wallJumped;
	}

	protected void setWallJumped(boolean wallJumped) {
		this.wallJumped = wallJumped;
	}

	private void processInputs()
	{
		
		//while wallJumped is true, player moves in opposite
		//direction of wall regardless of input
		if (!wallJumped)
		{
			
			//!right/!left is there so you can't go in the other direction
			//until you let the key go
			
			if (Main.inputHandler.isKeyDown("left") && !right)
				left = true;	
			else
				left = false;
			
			if (Main.inputHandler.isKeyDown("right") && !left)
				right = true;
			
			else
				right = false;
		}
		
		if (Main.inputHandler.isKeyDown("jump"))
		{
			Main.inputHandler.consume("jump");
			jump();
	
		}

	}

	/**
	 * The player jumps
	 */
	private void jump()
	{
		//regular jump
		if (isOnGround())
		{
			setOnGround(false);
			setVelY(jumpForce);
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
	

	
	

	
	

}
