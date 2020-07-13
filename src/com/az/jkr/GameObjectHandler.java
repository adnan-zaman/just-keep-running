package com.az.jkr;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * GameObjectHandler represents the data structure that holds references
 * to all GameObjects that are currently present in the game. 
 *
 */

public class GameObjectHandler {

	//reference to the player
	public Player player;
	
	private LinkedList<GameObject> staticObjects;
	private LinkedList<GameObject> dynamicObjects;
	private LinkedList<Menu> menus;
	
	private LinkedList<GameObject> tempStaticObjects;
	private LinkedList<GameObject> tempDynamicObjects;
	private LinkedList<Menu> tempMenus;
	
	public GameObjectHandler()
	{
		staticObjects = new LinkedList<GameObject>();
		dynamicObjects = new LinkedList<GameObject>();
		menus = new LinkedList<Menu>();
		tempStaticObjects = new LinkedList<GameObject>();
		tempDynamicObjects = new LinkedList<GameObject>();
		tempMenus = new LinkedList<Menu>();
		player = null;
	}
	
	/**
	 * Adds a GameObject to the handler
	 * 
	 * @param 	go 		the game object to add
	 * @return 	true 	if add succeeds, false otherwise
	 */
	public boolean addGameObject(GameObject go)
	{
		if (go.isStatic)
			return tempStaticObjects.add(go);
		
		if (player == null && go.getId() == ID.Player)
			player = (Player)go;
		return tempDynamicObjects.add(go);
	}
	
	/**
	 * Adds a menu to the handler
	 * 
	 * @param 	m 		the menu to add
	 * @return 	true 	if add succeeds, false otherwise
	 */
	public boolean addMenu(Menu m)
	{
		return tempMenus.add(m);
	}
	
	/**
	 * Removes a GameObject from the handler
	 * 
	 * @param 	go 		the game object to remove
	 * @return 	true if remove succeeds, false otherwise
	 */
	public boolean removeGameObject(GameObject go)
	{
		if (go.isStatic)
			return staticObjects.remove(go);
		if (player != null && go.getId() == ID.Player)
			player = null;
		return dynamicObjects.remove(go);
	}
	
	/**
	 * How many GameObjects are present
	 * @return the number of game objects in the handler
	 */
	public int size()
	{
		return staticObjects.size() + dynamicObjects.size();
	}
	
	/** 
	 * Calls the tick method for all GameObjects in the handler
	 */
	public void tick()
	{
		for (GameObject go : staticObjects)
			go.tick();
		for (int i = 0; i < staticObjects.size()/500; i++)
		{
			staticObjects.get(i).tick();
		}
		for (GameObject go : dynamicObjects)
			go.tick();
		for (Menu m : menus)
			m.tick();
		
	}
	
	public void animTick()
	{
		for (GameObject go : staticObjects)
			go.animTick();
		for (GameObject go : dynamicObjects)
			go.animTick();
		for (Menu m : menus)
			m.animTick();
	}
	
	/**
	 * Calls the render method of each game object in the handler
	 * 
	 * @param 	g2	the Graphics2D object
	 */
	public void render(Graphics2D g2, boolean debug)
	{
		for (GameObject go : staticObjects)
		{
			go.render(g2);
			if (debug)
				go.debugRender(g2);
		}
			
		for (GameObject go : dynamicObjects)
		{
			go.render(g2);
			if (debug)
				go.debugRender(g2);
		}
		
		for (Menu m : menus)
			m.render(g2);
	}
	
	
	
	public GameObject[] asArray()
	{
		GameObject[] gameObjects = new GameObject[size()];
		int i = 0;
		for (GameObject go : staticObjects)
		{
			gameObjects[i] = go;
			i++;
		}
		for (GameObject go : dynamicObjects)
		{
			gameObjects[i] = go;
			i++;
		}
		
		return gameObjects;
	}
	
	public GameObject[] dynamicObjects()
	{
		GameObject[] dynObjs = new GameObject[dynamicObjects.size()];
		int i = 0;
		for (GameObject go : dynamicObjects)
		{
			dynObjs[i] = go;
			i++;
		}
		
		return dynObjs;
	}
	
	public GameObject[] staticObjects()
	{
		GameObject[] statObjs = new GameObject[staticObjects.size()];
		int i = 0;
		for (GameObject go : staticObjects)
		{
			statObjs[i] = go;
			i++;
		}
		
		return statObjs;
	}
	
	/**
	 * Remove all game objects 
	 */
	public void clear()
	{
		staticObjects = new LinkedList<GameObject>();
		dynamicObjects = new LinkedList<GameObject>();
	}
	
	/**
	 * Remove all game objects and menus
	 */
	public void clearAll()
	{
		staticObjects = new LinkedList<GameObject>();
		dynamicObjects = new LinkedList<GameObject>();
		menus = new LinkedList<Menu>();
	}
	
	public void finalize()
	{
		staticObjects = tempStaticObjects;
		dynamicObjects = tempDynamicObjects;
		menus = tempMenus;
		
		tempStaticObjects = new LinkedList<GameObject>();
		tempDynamicObjects = new LinkedList<GameObject>();
		tempMenus = new LinkedList<Menu>();
	}
	
	

}
