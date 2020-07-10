package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Transition {

	protected Color bgColor;
	protected long totalTime;
	protected long lastTime;
	protected boolean done;
	
	
	public Transition(long t)
	{
		this(Color.black, t);
	}
	
	public Transition(Color c, long t)
	{
		bgColor = c;
		totalTime = t;
	}
	
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g2);
	
	public boolean isDone() {
		return done;
	}

}
