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

	
	private Sensor groundSensor;
	private Sensor frontWallSensor;
	
	public BasicRunner(float x, float y) 
	{
		super(x, y, 64, 64, 100, 5, -5, ID.BasicRunner, Color.yellow);
		target = Main.gameObjectHandler.player;
		groundSensor = new Sensor(getX() + getWidth()/2 + 10,getY() + getHeight()/2,20,20);
		frontWallSensor = new Sensor(getX() + getWidth()/2 + 10 + 48,getY(),160,20);
	}
	
	
	@Override
	public void tick()
	{
		updateSensors();
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
		frontWallSensor.clear();
		groundSensor.clear();
	}
	
	
	public void decide()
	{
		
		float[] dirToTarget = GameObject.getDirection(this, target);
		if (hasWallJumped())
		{
			
		}
		else
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
			
			
			if (!groundSensor.isOnGround() && !hasJumped() && dirToTarget[1] <= 0)
				jump();
			if (frontWallSensor.isOnWall() && !hasJumped())
				jump();	
		}
		
		
	}
	
	@Override
	public void render(Graphics2D g2) {
		standingOnGroundRender(g2);
	}
	
	@Override
	public void debugRender(Graphics2D g2)
	{
//		g2.setColor(getColor());
//		Main.camera.drawRect(g2,(Rectangle)getCollider(), false);
		g2.setColor(Color.white);
		Main.camera.drawRect(g2, (Rectangle)groundSensor.getCollider(), false);
		Main.camera.drawRect(g2, (Rectangle)frontWallSensor.getCollider(), false);
	
	}

	@Override
	public void updateCollider() {
		rectUpdateCollider();
	}


	@Override
	protected boolean intersects(GameObject other) {
		frontWallSensor.intersects(other);
		groundSensor.intersects(other);
		return simpleRectIntersect(other);
	}
	
	
	public void updateSensors()
	{
		frontWallSensor.setX(getX() + getForwardX() * (getWidth()/2 + 10 + 128));
		frontWallSensor.setY(getY()); 
		groundSensor.setX(getX() + getForwardX() * (getWidth()/2 + 10));
		groundSensor.setY(getY() + getHeight()/2); 
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
			BufferedImage playerSheet = SpriteSheet.getSpriteSheet("enemy.png");
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
