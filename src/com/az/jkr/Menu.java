package com.az.jkr;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * A menu acts as a container for a bunch of UI objects.
 * The menu is responsible for querying InputHandler
 * and the logic associated with which UI object
 * is currently selected, which to move to and
 * what to do if a selection occurs given the current
 * UI object
 *
 */

public abstract class Menu {

	//background color
	protected Color bgColor; 
	//transparency
	protected float alpha;
	//all ui objects this menu is responsible for
	protected ArrayList<GameObject> uiObjects;
	//index of the current object being selected
	protected int currIndex;
	
	public int getCurrIndex() {
		return currIndex;
	}

	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}

	public Menu(Color c)
	{
		this(c,1);
	}
	
	public Menu(Color c, float a)
	{
		bgColor = c;
		alpha = a;
		uiObjects = new ArrayList<GameObject>();
		currIndex = 0;
		loadMenu();
	}
	
	/**
	 * Each menu is responsible for filling 
	 * itself with whatever ui objects it needs
	 * as well as setting their positions.
	 */
	public abstract void loadMenu();
	
	/** 
	 * Each menu is responsible for 
	 * querying InputHandler and figuring
	 * out what needs to be done
	 * (change selection, carry out selection
	 * action depending on which ui object is selected etc)
	 */
	public abstract void processInput();
	
	
	/**
	 * Updates all UI objects in this menu
	 */
	public void tick()
	{
		for (GameObject go : uiObjects)
			go.tick();
	}
	
	/**
	 * Renders all UI objects in this menu
	 * 
	 * @param  g2	the Graphics2D context
	 */
	public void render(Graphics2D g2)
	{
		//set alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.setColor(bgColor);
		Main.camera.fillRect(g2, new Rectangle(0,0,Main.width,Main.height), true);
		//unset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		for (GameObject go : uiObjects)
			go.render(g2); 
	}
	
	/**
	 * Updates animation
	 * of all UI objects in this menu.
	 */
	public void animTick()
	{
		for (GameObject go : uiObjects)
			go.animTick();	
	}
	
	/**
	 * Many menus will offer user selection.
	 * They can override this.
	 */
	public void select()
	{
		
	}
}
