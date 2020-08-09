package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The simplest enemy of the game
 *
 */

public class BasicRunner extends Enemy {

	
	private Sensor groundSensor;
	private Sensor frontWallSensor;
	private Sensor frontWallAboveSensor;

	
	public BasicRunner(float x, float y) 
	{
		super(x, y, 64, 64, 100, 6f, -5, ID.BasicRunner, Color.yellow);
		target = Main.gameObjectHandler.player;
		groundSensor = new Sensor(getX() + getWidth()/2 + 10,getY() + getHeight()/2,20,20);
		frontWallSensor = new Sensor(getX() + getWidth()/2 + 128,getY(),20,20);
		frontWallAboveSensor = new Sensor(getX() + getWidth()/2 - 10,getY(),20,20);
		
		
		
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
		frontWallAboveSensor.clear();
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
		Main.camera.drawRect(g2, (Rectangle)frontWallAboveSensor.getCollider(), false);
	
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
		frontWallSensor.setX(getX() + getForwardX() * (getWidth()/2 + 128));
		frontWallSensor.setY(getY()); 
		groundSensor.setX(getX() + getForwardX() * (getWidth()/2 + 10));
		groundSensor.setY(getY() + getHeight()/2); 
		frontWallAboveSensor.setX(getX() + getForwardX() * (getWidth()/2 + 10));
		frontWallAboveSensor.setY(getY() - getHeight()/2 - 40);
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
	

	

	/**
	 * AI decides what to do
	 */
	private void decide()
	{
		
		float[] dirToTarget = GameObject.getDirection(this, target);
		
		if (isOnGround())
		{
			if (dirToTarget[0] < 0)
				processInput("left");
			else if (dirToTarget[0] > 0)
				processInput("right");
			else if (GameObject.floatEq(dirToTarget[0], 0))
				processInput("stop");
			
			//wall ahead
			if (frontWallSensor.isOnWall())
			{
				
				processInput("jump");
			}
			//gap ahead, target is not below
			if (!groundSensor.isOnGround() && getElevation() <= 0)
				processInput("jump");
			//touching wall, jump over it
			if (isOnWall())
				processInput("jump");
		}
		else
		{
			
			if (getElevation() <= 0 && frontWallAboveSensor.isOnWall())
			{
				if (isOnWall())
				{
					
					processInput("jump");
					System.out.println("basRun: it jumepd");
				}
					
				
				//left/right not changed allowing walljumps
			}
			else
			{
				if (dirToTarget[0] < 0)
					processInput("left");
				else if (dirToTarget[0] > 0)
					processInput("right");
				else if (GameObject.floatEq(dirToTarget[0], 0))
					processInput("stop");
			}
		}
	}
	
	/**
	 * Given an input command, will set state of BasicRunner accordingly
	 * 
	 * @param inp the input
	 */
	private void processInput(String inp)
	{
		if (!horizontalLocked)
		{
			if (inp.equals("left"))
			{
				left = true;
				right = false;
			}
			else if (inp.equals("right"))
			{
				right = true;
				left = false;
			}
			else if (inp.equals("stop"))
			{
				left = false;
				right = false;
			}
		}
		
		if (inp.equals("jump"))
			jump();
		
	}
	
	/**
	 * Returns an integer representing target's elevation
	 * relative to BasicRunner. The possible outputs are
	 * above, level and below. Since GameObjects' coordinates
	 * are at their center and are different sizes, this could
	 * lead to a "below" result even if both GameObjects are
	 * essentially level. So, level means target.y is contained
	 * within BasicRunner's height.
	 * 
	 * @return -1 = above, 0 = level, 1 = below
	 */
	private int getElevation()
	{
		if (target.getY() < getY() - getHeight()/2)
			return -1;
		if (target.getY() > getY() + getHeight()/2)
			return 1;
		return 0;
	}

}
