package com.az.jkr;

/**
 * Contains methods for physics interactions
 *
 */
public class Physics {

	//currently interpreted as how much velY decreases
	public static final float  gravity = 0.2f;
	public static final float terminalVel = 10f;
	public static final float dragX = 0.5f;
	public static final float dragY = 0.5f;
	
	/**
	 * Applies gravity on all dynamic game objects
	 */
	public void applyGravity()
	{
		GameObject[] gameObjects = Main.gameObjectHandler.asArray();
		for (GameObject go : gameObjects)
		{
			if (!go.isStatic)
			{
				if (go.isOnGround())
					go.setVelY(0);	
				else if (go.getVelY() < terminalVel)
					go.setVelY(Math.min(go.getVelY() + gravity,terminalVel));
			}
				
		}
	}
	
	public void tick()
	{
		resolvePhysics();
	}
	
	/**
	 * Linear interpolation from a to b by amt percent
	 * @param 	a	starting number
	 * @param 	b	ending number
	 * @param 	amt	how much to interpolate
	 * @return	the interpolated value
	 */
	public static float lerp(float a, float b, float amt)
	{
		float dist = b - a;
		return a + (dist * amt);
	}
	
	private void resolvePhysics()
	{
		GameObject[] gameObjects = Main.gameObjectHandler.asArray();
	}
	
}
