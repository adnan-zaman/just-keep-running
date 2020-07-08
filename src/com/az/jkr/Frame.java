package com.az.jkr;

import java.awt.image.BufferedImage;

/**
 * Represents a single frame of animation
 *
 */

public class Frame {

	private BufferedImage image; //the image
	private int delay; //how many anim frames will this frame last
	
	
	public Frame(BufferedImage i, int d)
	{
		image = i;
		d = delay;
	}


	public BufferedImage getImage() {
		return image;
	}


	public int getDelay() {
		return delay;
	}
}
