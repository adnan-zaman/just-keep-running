package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.az.jkr.Player.PlayerState;

/**
 * The simplest enemy of the game
 *
 */

public class BasicRunner extends Enemy {

	
	public BasicRunner(float x, float y) 
	{
		super(x, y, 64, 64, 100, 6, -5, ID.BasicRunner, Color.red);
		target = Main.gameObjectHandler.player;
	}
	
	
	@Override
	public void tick()
	{
		decide();

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
			setVelX(0);
		}
		
		super.tick();
	}
	
	
	public void decide()
	{
		if (GameObject.getDirection(this, target)[0] < 0)
		{
			left = true;
			right = false;
		}
		else if (GameObject.getDirection(this, target)[0] > 0)
		{
			right = true;
			left = false;
		}
		else
		{
			left = false;
			right = false;
		}
		
	}
	
	@Override
	public void render(Graphics2D g2) {
		standingOnGroundRender(g2);
	}

	@Override
	public void updateCollider() {
		rectUpdateCollider();
	}


	@Override
	protected boolean intersects(GameObject other) {
		return simpleRectIntersect(other);
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

}
