package com.az.jkr;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;


/**
 * Handles all input
 *
 */


public class InputHandler implements KeyListener{
	
	//controls
	private int up, down, left, right, jump;
	private boolean rightPressed;
	//map from key -> pressed or not
	private Map<String,Boolean> inputMap; 
	
	private final String[] validInputs = {"left","right","jump","down","up"};
	
	public InputHandler()
	{
		//default controls
		up = KeyEvent.VK_W;
		down = KeyEvent.VK_S;
		left = KeyEvent.VK_A;
		right = KeyEvent.VK_D;
		jump = KeyEvent.VK_J;
		
		inputMap = new HashMap<String,Boolean>();
		for (String s : validInputs)
			inputMap.put(s,false);
		
		inputMap.put("pause", false);
		inputMap.put("stop",false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == right)
		{
			inputMap.put("right", true);
		}
		if (e.getKeyCode() == left)
		{
			inputMap.put("left", true);
		}
		if (e.getKeyCode() == up)
		{
			inputMap.put("up", true);
		}
		if (e.getKeyCode() == down)
		{
			inputMap.put("down", true);
		}
		
		if (e.getKeyCode() == jump)
		{
			inputMap.put("jump", true);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_P)
		{
			inputMap.put("pause", true);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_O)
		{
			inputMap.put("stop", true);
		}
		
		
		
		

		
	}

	public void keyReleased(KeyEvent e) 	
	{
		if (e.getKeyCode() == right)
		{
			inputMap.put("right", false);

		}
		if (e.getKeyCode() == left)
		{
			inputMap.put("left", false);
		}
		if (e.getKeyCode() == up)
		{
			inputMap.put("up", false);
		}
		if (e.getKeyCode() == down)
		{
			inputMap.put("down", false);

		}
		
		if (e.getKeyCode() == jump)
		{
			inputMap.put("jump", false);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_P)
		{
			inputMap.put("pause", false);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_O)
		{
			inputMap.put("stop", false);
		}

	}
	
	public boolean isKeyDown(String key)
	{
		
		return inputMap.get(key);
	}
	
	/**
	 * There are a few frames between when a key is
	 * pressed and a key is released, and it stays true in this time.
	 * If consumed, key will be set to false. Useful for events
	 * where we don't want key to stay on for
	 * longer than the first frame it was read (like jump)
	 * 
	 * @param key
	 */
	public void consume(String key)
	{
		inputMap.put(key, false);
	}
	
	public void allKeys()
	{
		System.out.println("----");
		for (String s : validInputs)
			System.out.println(s + " " + inputMap.get(s));
		
		System.out.println("----");
	}

}
