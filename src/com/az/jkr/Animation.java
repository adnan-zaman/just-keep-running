package com.az.jkr;

import java.awt.image.BufferedImage;

/**
 * Represents a single animation cycle
 * 
 */

public class Animation {

	private Frame[] frames; //the frames
	private int totalFrames; //total number of frames
	private int currentFrame; //the current frame of animation
	private boolean looping; //loop or not
	
	
	/**
	 * Creates an animation from a consecutive sequence of frames with
	 * constant default (1 animation) delay and looping
	 * 
	 * @param 	spriteSheet	sprite sheet containing the images
	 * @param 	startFrame	index of starting subimage (inclusive)
	 * @param 	endFrame	index of ending sub image  (inclusive)
	 * @param	tileWidth	tile width of each sprite
	 * @param	tileHeight	tile height of each sprite
	 */
	public Animation(BufferedImage spriteSheet, int startIndex, int endIndex, int tileWidth, int tileHeight)
	{
		this(spriteSheet,startIndex,endIndex,tileWidth,tileHeight,true);
	}
	
	/**
	 * Creates an animation from a consecutive sequence of frames with
	 * constant default (1 animation) delay 
	 * 
	 * @param 	spriteSheet	sprite sheet containing the images
	 * @param 	startFrame	index of starting subimage (inclusive)
	 * @param 	endFrame	index of ending sub image  (inclusive)
	 * @param	tileWidth	tile width of each sprite
	 * @param	tileHeight	tile height of each sprite
	 * @param	looping		whether or not this animation loops
	 */
	public Animation(BufferedImage spriteSheet, int startIndex, int endIndex, int tileWidth, int tileHeight, boolean looping)
	{
		totalFrames = endIndex - startIndex + 1;
		currentFrame = 0;
		frames = new Frame[totalFrames];
		this.looping = looping;
		for (int i = 0; i < totalFrames; i++)
		{
			BufferedImage img = SpriteSheet.getSubImage(spriteSheet, startIndex + i, tileWidth, tileHeight);
			frames[i] = new Frame(img,1);
		}
	}
	
	/**
	 * Returns the current frame in the animation cycle
	 * 
	 * @return	the current sprite
	 */
	public Frame getCurrentFrame()
	{
		return frames[currentFrame];
	}
	
	/**
	 * Returns the frame number of the current frame
	 * 
	 * @return	the current frame number
	 */
	public int getCurrentFrameNumber()
	{
		return currentFrame;
	}
	
	/**
	 * Get the delay of the current frame
	 * @return	the delay
	 */
	public int getDelay()
	{
		return frames[currentFrame].getDelay();
	}
	
	/**
	 * Moves the animation to the next frame.
	 * If this is a looping animation, will loop back to
	 * first frame after reaching the end. If not looping,
	 * it will stay on the last frame.
	 * 
	 * @return false if this is a non-looping animation that has reached its last frame
	 */
	public boolean nextFrame()
	{
		currentFrame++;
		if (!looping && currentFrame >= totalFrames)
		{
			currentFrame = totalFrames - 1;
			return false;
		}
		currentFrame %= totalFrames;
		return true;
	}
	
	
	/**
	 * Resets the animation to the beginning
	 */
	public void reset()
	{
		currentFrame = 0;
	}

	public int getTotalFrames() {
		return totalFrames;
	}
}
