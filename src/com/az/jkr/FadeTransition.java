package com.az.jkr;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class FadeTransition extends Transition {

	private float currAlpha;
	private float startAlpha;
	private float endAlpha;
	
	public FadeTransition(Color c, long t, String type) {
		super(c, t);
		
		if (type.equals("in"))
		{
			currAlpha = startAlpha = 0;
			endAlpha = 1;
		}
		else if (type.equals("out"))
		{
			currAlpha = startAlpha = 1;
			endAlpha = 0;
		}
		
		lastTime = System.currentTimeMillis();

	}

	@Override
	public void tick() 
	{
		long now = System.currentTimeMillis();
		long deltaTime = now - lastTime;
		deltaTime = 10;
		float fraction = ((float)deltaTime)/totalTime;
//		System.out.println("totalTime: " + totalTime);
//		System.out.println("delta/total: " + fraction);
//		System.out.println("----------");
		currAlpha = GameObject.lerp(currAlpha, endAlpha, fraction  , false);
		
		if (GameObject.floatEq(currAlpha, endAlpha, 0.05f))
			done = true;
		if (currAlpha > endAlpha)
		{
			currAlpha = endAlpha;
			done = true;
		}
		
		lastTime = now;
	
	}

	@Override
	public void render(Graphics2D g2) 
	{
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currAlpha));
		g2.setColor(bgColor);
		Main.camera.fillRect(g2, new Rectangle(0,0,Main.width,Main.height), true);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

	}

}
