package com.az.jkr;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles collisions
 *
 */
public class CollisionHandler 
{
	
	/*
	 * after resolving dynamic-static collisions, we move the dynamic
	 * object slightly into the static object so it stays colliding
	 * (e.g during floor collisions, we want the dynamic object to stay on the ground)
	 * this is because if two shapes share an edge but aren't intersecting
	 * they don't count as intersecting
	 * 
	 * it's 2 to account for rounding errors when converting from ints to floats
	 * in MovingGameObject's rectSetCollider()
	 * 
	 * e.g floor surface is at y = 800, after calculations the hitbox's 
	 * bottom will be at 800.5, but due to casting to int, will become 800
	 * (and therefore be not colliding)
	 */
	public static final int collisionOffset = 2;
	
	/**
	 * 
	 * Represents a single collision between two objects
	 *
	 */
	public class CollisionInfo
	{
		//the two objects
		public GameObject obj1;
		public GameObject obj2;
		
		public CollisionInfo(GameObject go1, GameObject go2)
		{
			obj1 = go1;
			obj2 = go2;
		}
	}
	
	
	public CollisionHandler()
	{
		
	}
	
	
	/**
	 * Goes through all game objects
	 * and checks which ones collide
	 * 
	 */
	public void checkCollisions()
	{
		
		GameObject[] staticObjects = Main.gameObjectHandler.staticObjects();
		GameObject[] dynamicObjects = Main.gameObjectHandler.dynamicObjects();
		//stores all game objects who had collisions to resolve after
		//set because we don't want a game object to be moved twice
		//even if it collides twice, once should be fine
		Set<CollisionInfo> toResolve = new HashSet<CollisionInfo>();
		//game objects that are involved in a collision, used for changing onGround
		Set<GameObject> haveCollided = new HashSet<GameObject>();
		//change to false if there is a collision, used for changing onGround
		boolean noCollisions = true;
		//offset
		int offset = 0;
		
		/**
		 * only check collisions dynamic against everything else
		 * we don't care if static objects collide with anything
		 * (but we do care if dynamic objects collide with them)
		 * 
		 * offset increases since if we checked A intersect B, we don't need to
		 * check B intersect A
		 * 
		 * we start from offset + 1 because we don't need to check A intersect A
		 * 
		 * example (the checker : the game objects it checks)
		 * 
		 * given {A,B,C,D}
		 *
		 * A : B C D
		 * B :   C D
		 * C :     D
		 * D :
		 */
		for (int i = 0; i < dynamicObjects.length; i++)
		{
			//naively set onWall to false every time
			//if there's a horizontal collision, it'll set it to true afterwards
			dynamicObjects[i].setOnWall(false);
			
			for (int j = offset+1; j < Main.gameObjectHandler.size(); j++)
			{
				
				GameObject checkAgainst = null;
				//get right object from right array
				if (j < dynamicObjects.length)
					checkAgainst = dynamicObjects[j];
				else
				{
					checkAgainst = staticObjects[staticObjects.length - j];
					
				}
					
			
				if (dynamicObjects[i].intersects(checkAgainst))
				{
					
					toResolve.add(new CollisionInfo(dynamicObjects[i], checkAgainst));
					haveCollided.add(dynamicObjects[i]);
					//add second game object since collisions only "look forward"
					//A checks collisions with B, but B doesnt check with A
					haveCollided.add(checkAgainst);
					
					
					noCollisions = false;
				}
						
			}

			//game object i hasn't collided with anything
			//nor has anything collided with it
			//therefore must be in the air
			if (noCollisions && !haveCollided.contains(dynamicObjects[i]))
			{
				dynamicObjects[i].setColor(Color.red);
				dynamicObjects[i].setOnGround(false);
			}

				
			noCollisions = true;
			offset++;
		}
		
		//resolve everything that needs to be resolved
		for (CollisionInfo ci : toResolve)
			resolveCollisions(ci.obj1, ci.obj2);
	}
	
	/**
	 * Determines whether to do dynamic/static resolution or dynamic/dynamic resolution
	 * @param 	a	game object
	 * @param 	b	other game object
	 */
	private void resolveCollisions(GameObject a, GameObject b)
	{

		//dynamic with static collision
		if (a.isStatic && !b.isStatic)
			dynamicStaticResolve(b,a);
		else if (!a.isStatic && b.isStatic)
			dynamicStaticResolve(a,b);
		else if (!a.isStatic && !b.isStatic)
		{
			//System.out.println("nice");
			//dynamicDynamicResolve(a,b);
		}
			
		else
			{
			
			}
		
	}
	
	/**
	 * Collision resolution between a dynamic and static object
	 * @param 	dynObj	dynamic game object
	 * @param 	statObj	static game object
	 */
	private void dynamicStaticResolve(GameObject dynObj, GameObject statObj)
	{
		/*
		 * Solution taken from: https://stackoverflow.com/questions/1585525/how-to-find-the-intersection-point-between-a-line-and-a-rectangle
		 * by user: Joren
		 */
		
		
		//either dynObj's x or y will be changed
		//target will contain the new location
		float target = 0;
		
		//direction of static obj to dynamic obj
		float[] statToDyn = GameObject.getDirection(statObj, dynObj);
		
		float largerSide = Math.max(statObj.getWidth(), statObj.getHeight());
		
		//multiply by largerSide to guarantee the line has a magnitude that will reach outside the shape
		//add to statObj's pos to get coordinates 
		float[] endPoint = {statObj.getX() + (statToDyn[0] * largerSide), statObj.getY() + (statToDyn[1] * largerSide)};
		
		//slope of the line between statObj and endPoint
		float slope = (endPoint[1] - statObj.getY()) / (endPoint[0] - statObj.getX());

		
		
		//horizontal collision
		/*
		 * Basically this check is saying, what is the line
		 * going from statObj to endPoint's y value when x is at
		 * the side of statObj (getHeight()/2)
		 * 
		 * if y <= -statObj.getHeight()/2, it means at x = statObj.getHeight()/2
		 * it's already above (negative y values are above with java.awt), therefore
		 * the line intersects the top of the shape (and that means dynObj is intersecting
		 * at the top)
		 * 
		 * same idea goes for y >= statObj.getHeight()/2 
		 * 
		 * if y is in between (+/-)statObj.getHeight()/2,
		 * this means there is an intersection one one of the horizontal sides
		 * 
		 * which side (left or right) is determined by endPoint's x value
		 */
		if (slope * (statObj.getWidth()/2) <= statObj.getHeight()/2 && 
			slope * (statObj.getWidth()/2) >= -statObj.getHeight()/2)
		{
			
			//dynObj is touching a wall if it's a horizontal collision
			dynObj.setOnWall(true);
			//right collision
			if (endPoint[0] < statObj.getX())
			{
				target = statObj.getX() - statObj.getWidth()/2;
				dynObj.setX(target - dynObj.getWidth()/2);
			}
			//left collision
			else if (endPoint[0] > statObj.getX())
			{
				target = statObj.getX() + statObj.getWidth()/2;
				dynObj.setX(target + dynObj.getWidth()/2 + 1);
			}

		}
		
		//if it's not a horizontal collision, it's a vertical collision
		else
		{
				
				//top collision
				if (endPoint[1] < statObj.getY() && statObj.isGround())	
				{
					if (statObj.isGround)
						dynObj.setOnGround(true);
					
						
					target = statObj.getY() - statObj.getHeight()/2;
					//+1 becuase top static-dyn collisions can be interpreted as dynamic objects
					//touching feet to "ground" in some way
					//therefore, we want them to always be touching the ground
					//(this makes sure onGround will remain true during checkCollisions()
					dynObj.setY(target - dynObj.getHeight()/2 + collisionOffset);

				}
				//bottom collision
				else if (endPoint[1] > statObj.getY() && statObj.isCeiling())
				{
					target = statObj.getY() + statObj.getHeight()/2;
					dynObj.setY(target + dynObj.getHeight()/2);
					
					//stop upwards movement
					//this is to prevent dynObjs jumping, hitting a ceiling
					//but still hanging in the air
					if (dynObj.getVelY() < 0)
						dynObj.setVelY(0);
				}
		}
	}
	
	/**
	 * Collision resolution between two dynamic game objects
	 * @param 	dynObj	game object
	 * @param 	statObj	other game object
	 */
	private void dynamicDynamicResolve(GameObject a, GameObject b)
	{
		
	}
	
	
	

}
