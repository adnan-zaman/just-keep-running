package com.az.jkr;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls the animation for a single game object
 *
 */
public class Animator {
	
	private Map<String,Animation> animMap; //map of animation state to animations
	private String currAnimState; //the current animation state
	private Animation currAnimation; //the current animation
	private int framesPassed; //the number of animation frames that have passed;
	
	public Animator()
	{
		animMap = new HashMap<String,Animation>();
		currAnimState = "";
		currAnimation = null;
		framesPassed = 0;
	}
	
	/**
	 * Adds an animation to the animator
	 * @param state	name of animation state
	 * @param anim	the animation associated with this state
	 * 
	 */
	public void addAnimation(String state, Animation anim)
	{
		animMap.put(state, anim);
	}
	
	public void tick()
	{
		if (animMap.size() > 0)
		{
			framesPassed++;
			//move onto the next frame
			if (framesPassed > currAnimation.getDelay())
			{
				framesPassed = 0;
				currAnimation.nextFrame();
			}
			
		}
	}
	
	/**
	 * Returns the sprite from the current frame in the
	 * current animation
	 * @return	the sprite to be drawn
	 */
	public BufferedImage getSprite()
	{
		return animMap.get(currAnimState).getCurrentFrame().getImage();
	}
	
	/**
	 * Return the frame number of the current frame
	 * of the current animation
	 * 
	 * @return frame number of current frame of current animation
	 */
	public int getCurrentFrameNumber()
	{
		return animMap.get(currAnimState).getCurrentFrameNumber();
	}

		
	public String getAnimState()
	{
		return currAnimState;
	}
	
	
	public void setAnimState(String state)
	{
		if (currAnimState.equals(state))
			return;
		currAnimState = state;
		currAnimation = animMap.get(state);
		currAnimation.reset();
		framesPassed = 0;
	}
	
	
	

}
