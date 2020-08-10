package com.az.jkr;

import java.awt.Color;
import java.util.ArrayList;


/**
 * Base class of all enemies
 *
 */

public abstract class Enemy extends MovingGameObject {

	/**
	 * The possible goals enemies can have
	 */
	public enum EnemyGoal
	{
		Patrol, //enemy moves from waypoint to waypoint
		Chase //enemy is actively chasing the player
	}
	
	//what the enemy is currently pursuing/avoiding
	protected GameObject target;
	//the enemy's current goal. this represents what the enemy is trying
	//to do and will affect how they'll act
	protected EnemyGoal goal;
	//the waypoints this enemy patrols
	//each element is the index of the waypoint stored in GameObjectHandler
	protected ArrayList<Integer> waypointCycle;
	//which waypoint are they heading towards
	protected int currWaypoint;
	//if set to true, enemy will keep moving in the horizontal
	//direction they were already moving in (this is to stop
	//enemy from essentially getting stuck if player is above/below
	//them and enemy reaches player's x-coord
	protected boolean keepGoing;
	
	public Enemy(float x, float y, float width, float height, ID id, Color c) {
		this(x, y, width, height, 100, 5,id, c);
	}
	
	public Enemy(float x, float y, float width, float height, float hp, float maxSpeedX, ID id, Color c) {
		this(x, y, width, height, hp, maxSpeedX, -5, id, c);
	}
	
	public Enemy(float x, float y, float width, float height, float hp, float maxSpeedX, float jumpForce, ID id, Color c) {
		super(x, y, width, height, hp, maxSpeedX, jumpForce, id, c);
		target = null;
		goal = null;
		waypointCycle = new ArrayList<Integer>();
		currWaypoint = 0;
	}
	
	/**
	 * Cycles through waypoints
	 */
	protected void nextWaypoint()
	{
		currWaypoint = (currWaypoint + 1) % waypointCycle.size();
	}

	protected boolean keepGoing() {
		return keepGoing;
	}

	protected void setKeepGoing(boolean keepGoing) {
		this.keepGoing = keepGoing;
	}
	

}
