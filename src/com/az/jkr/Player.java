package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;


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

	
	public Player(float x, float y)
	{
		super(x, y, 64, 64, 100, 7, ID.Player, Color.green);
		state = PlayerState.Idle;	
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
		standingOnGroundRender(g2);
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
		Main.camera.drawRect(g2, (Rectangle)getCollider(),false);
		g2.setColor(Color.white);
		Main.camera.drawRect(g2, new Rectangle((int)getX()-1,(int)getY(),3,3),false);
//		Main.camera.drawString(g2,"(" + left + "," + right +")", (int)(getX() + getWidth()/2 + 10), (int)getY());
//		Main.camera.drawString(g2,"onWall: " + isOnWall(), (int)(getX() + getWidth()/2 + 10), (int)getY() + 10);
//		Main.camera.drawString(g2,"onGround: " + isOnGround(), (int)(getX() + getWidth()/2 + 10), (int)getY() + 20);
	
		g2.setColor(Color.white);
	
	
	}

	@Override
	public void updateCollider() 
	{
		//rectUpdateCollider();	
		Rectangle r = new Rectangle((int)getX(),(int)getY(),64,64);
		rectSetCollider(r);
	}
	
	

	@Override
	public boolean intersects(GameObject other)
	{
		return simpleRectIntersect(other);
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


	private void processInputs()
	{
		
		//while wallJumped is true, player moves in opposite
		//direction of wall regardless of input
		if (!hasWallJumped())
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

	
	

	
	

	
	

}
