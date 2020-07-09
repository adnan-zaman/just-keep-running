package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A convenience method for creating
 * UI objects that are just a single image.
 
 * @author adnan
 *
 */
public class UI_Image extends GameObject {

	//name of image file
	private String imgFile;
	
	public UI_Image(String imgFile, float x, float y, int spriteWidth, int spriteHeight)
	{
		super(x, y, spriteWidth, spriteHeight, ID.UI, Color.blue);
		this.imgFile = imgFile;
		setAnimation();
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g2) {
		
		Main.camera.drawSprite(g2, getAnimator().getSprite(), 
				getX() - getWidth()/2, 
				getY() - getHeight()/2, 
				false,
				true);

	}
	
	/**
	 * This is deliberately called differently so that
	 * we can call this after GameObject's constructor finishes
	 * (GameObject's constructor calls setAnimations, but imgFile
	 * isn't initialized till after the constructor finishes)
	 */
	public void setAnimation()
	{
		try {
			spriteWidth = (int)getWidth();
			spriteHeight = (int)getHeight();
			BufferedImage img = SpriteSheet.getSpriteSheet(imgFile);
			getAnimator().addAnimation("idle", new Animation(img, 0, 0, spriteWidth, spriteHeight,false));
			getAnimator().setAnimState("idle");
//			spriteWidth = spriteHeight = 64;
//			BufferedImage playerSheet = SpriteSheet.getSpriteSheet("player.png");
//			getAnimator().addAnimation("idle", new Animation(playerSheet, 10, 10, spriteWidth, spriteHeight,false));
//			getAnimator().setAnimState("idle");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateCollider() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setCollider() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean intersects(GameObject other) {
		// TODO Auto-generated method stub
		return false;
	}

}
