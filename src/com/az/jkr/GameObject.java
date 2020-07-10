package com.az.jkr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
 * The base class of all objects that are actually
 * part of the game (i.e, not including UI elements)
 *
 */

public abstract class GameObject {
	
	
	//if true, will not respond to physics collisions
	//game objects are NOT static by default
	public boolean isStatic;
	
	

	//position refers to the CENTER of game objects
	//Java AWT draws shapes from the top left
	protected float x, y;
	//velocity
	protected float velX, velY;
	//forces acting on this object
	protected float forceX, forceY;
	//mass
	protected float mass;
	//width and height (corresponds to current collider)
	protected float width, height;
	//how far game object is from surface of the ground (only meaningful if !isStatic)
	protected float distFromGround;
	//width and height of sprite tiles
	protected int spriteWidth, spriteHeight;
	
	
	//this game object's hp
	protected float hp;
	//max hp
	protected float maxHP;
	//whether or not this gameobject is on the ground (only meaningful if isStatic = false)
	protected boolean onGround;
	//whether or not this gameobject is touching a wall (only meaningful if isStatic = false)
	protected boolean onWall;
	
	/* used to determine how vertical collisions with this object will be handled (only meaningful if isStatic = false)
	 * if isGround -> will check for things colliding with it's top (acts like ground)
	 * if isCeiling -> will check for things colliding with it's bottom (acts like a ceiling)
	 * isGround & isCeiling -> will check for collisions on top AND bottom (a single block in the air is a ground from above but a ceiling from below)
	 * neither -> no vertical collision check (for walls, this is so dynamic objecs don't get stuck in them when moving up/down against a wall)
	 */
	protected boolean isGround, isCeiling;
	//this object's current collider
	protected Shape collider;
	//mapping from state -> list of colliders for that state
	//index of colliders in list correspond with frame number of animation
	//of the same name
	protected Map<String,ArrayList<Shape>> colliderMap;
	//this object's color
	protected Color color;
	//this object's id
	protected ID id;
	//this object's animator
	protected Animator animator;
	
	//how many points on the bottom of a game object there are
	//for the purpose of checking if it's on the ground
	private final int  groundPoints = 10;
	//what percentage of groundPoints need to be touching to be
	//considered on the ground
	private float groundTolerance = 0.55f;


	public GameObject(float x, float y, float width, float height, ID id, Color c)
	{
		this(x,y,width,height,100,id,c);
	}
	
	public GameObject(float x, float y, float width, float height, float hp, ID id, Color c)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hp = this.maxHP = hp;
		this.id = id;
		this.color = c;
		distFromGround = 0;
		colliderMap = new HashMap<String,ArrayList<Shape>>();
		animator = new Animator();
		velX = velY = 0;
		forceX = forceY = 0;
		isStatic = false;
		isGround = false;
		isCeiling = false;
		onGround = false;
		onWall = false;
		setAnimations();
		setCollider();
		

	}
	
	/**
	 * Equality check for floats with
	 * default precision of 0.001
	 * 
	 * @param 	x	first float
	 * @param 	y	second float
	 * @return	true if equal
	 */
	public static boolean floatEq(float x, float y)
	{
		return floatEq(x,y,0.001f);
	}
	
	/**
	 * Equality check for floats!!
	 * 
	 * @param 	x		first float
	 * @param 	y		second float
	 * @param 	prec	precision
	 * @return	true if equal
	 */
	public static boolean floatEq(float x, float y, float prec)
	{
		return ((x <= y + prec) && (x >= y - prec));
	}
	
	/**
	 * Distance between two game objects
	 * @param 	a	one game object
	 * @param 	b	another game object
	 * @return	distance
	 */
	public static float getDistance(GameObject a, GameObject b)
	{
		float[] dir = new float[2];
		dir[0] = (b.getX() - a.getX());
		dir[1] = (b.getY() - a.getY());
		float mag = (float)Math.sqrt( (dir[0] * dir[0]) + (dir[1] * dir[1])   );
		
		return mag;
	}
	
	/**
	 * Returns the direction (unit vector) from a (source) to b (target)
	 * @param 	a	the source game object
	 * @param 	b	the target game object
	 * @return	float[] of size 2. index 0 = x-component, index 1 = y-component
	 */
	public static float[] getDirection(GameObject a, GameObject b)
	{
		float[] dir = new float[2];
		dir[0] = (b.getX() - a.getX());
		dir[1] = (b.getY() - a.getY());
		float mag = (float)Math.sqrt( (dir[0] * dir[0]) + (dir[1] * dir[1])   );
		
		dir[0] /= mag;
		dir[1] /= mag;
		
		
		
		return dir;
	}
	
	public static float lerp (float a, float b, float step, boolean percent)
	{
		if (percent)
			return a + ((b-a) * step);
		else
			return a + ((b-a)/Math.abs(b-a) * step);
	}

	/**
	 * The game object's update method. Will be
	 * called every frame. The game object's logic
	 * should be contained here
	 * 
	 */
	public abstract void tick();
	
	/**
	 * The game object's draw method. Logic
	 * pertaining to render the game object should
	 * be contained here.
	 * 
	 * @param 	g2	the Graphics2D object
	 */
	public abstract void render(Graphics2D g2);
	
	/**
	 * Updates the collider if the game object has moved
	 */
	public abstract void updateCollider();
	
	/**
	 * Called during initialization to set up
	 * game object's collider
	 * 
	 */
	protected abstract void setCollider();
	
	/**
	 * Determines whether another game object
	 * intersects this one
	 * 
	 * @param 	other	the other game object
	 * 
	 * @return	true if other intersects this game object
	 * 
	 */
	protected abstract boolean intersects(GameObject other);
	
	/**
	 * Returns this game object's collider.
	 * 
	 * @return the collider
	 */
	public Shape getCollider()
	{
		if (!isStatic)
		{
			updateCollider();
		}
		
		return collider;		
	}

	/**
	 * Can be overridden
	 */
	public void animTick()
	{
		
	}
	
	/**
	 * Maps sequences of sprites as animations in this
	 * game object's animator.
	 * Also responsible for setting spriteWidth/spriteHeight
	 */
	protected void setAnimations()
	{
		
	}
	
	
	/**
	 * Reads colliders from an HBX file into colliderMap
	 * 
	 * format of of an HBX file:
	 * 
	 * each line contains all rectangles representing all colliders for a particular state
	 * rectangles are seperate by spaces, each rectangle is represented by two numbers
	 * seperated by a comma. First number is width, second is length.
	 * 
	 * e.g
	 * 
	 * run 20,30 25,30 22,30 
	 * jump 20,35 25,35
	 * 
	 * @param colliderFile	file name
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	protected void readColliders(String colliderFile) throws NumberFormatException, IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(Main.gameObjectHandler.getClass().
				getResource("../../../colliders/" + colliderFile).getPath()));
		String line = "";
		while ((line = br.readLine()) != null)
		{
			String[] arr = line.split(" ");
			ArrayList<Shape> list = new ArrayList<Shape>();
			for (int i = 1; i < arr.length; i++)
			{
				String[] pos = arr[i].split(",");
				list.add(new Rectangle((int)getX(),(int)getY(), Integer.parseInt(pos[0]),Integer.parseInt(pos[1])));
			}
			
			colliderMap.put(arr[0],list);
		}
		br.close();
	}
	

	/**
	 * Determines whether the game object has moved since last frame
	 * 
	 * @return true if game object has moved
	 */
	protected boolean hasMoved()
	{
		//there will be situations when velX/velY will != 0
		//but doesn't necessarily mean object is moving
		//(e.g velX/velY set at end of tick, will move next frame)
		//but generally, most of the time it'll be because the object
		//is and has been moving
		//if this is really a cost to efficiency, will change later
		return (!floatEq(velX,0) || !floatEq(velY,0));
	}
	
	
	/**
	 * TODO
	 * gotta delete this, kept for now so that walls dont break
	 */
	protected void rectSetCollider()
	{
		collider = new Rectangle((int)(x - width/2), (int)(y - height/2), (int)width, (int)height);	
	}
	
	

	/*debug stuff */
	
	public void debugRender(Graphics2D g2)
	{
		
	}
	

	
	/**
	 * Checks whether this game object is on the ground
	 * @param 	other	other game object that is meant to be a floor
	 * @return	true if on ground
	 */
	public boolean checkTouchingGround(GameObject other)
	{
		/*
		 * Checks whether multiple (groundPoints) points along the
		 * bottom of the game object are within the other game object
		 * 
		 * if a certain percentage (greater than groundTolerance) are
		 * colliding, we consider this game object to be on the ground
		 */
	
		
		//how many points are touching the ground
		float touching = 0;
		Shape oColl = other.getCollider();
		//think of the n points as dividers
		//if there are n dividers, there are n-1 slices
		//each section is 1/(n-1) of the width
		float dist = getWidth() / (groundPoints-1);
		System.out.println("go.checkGr: y: " + getY() + " floor: " + (getY() + getHeight()/2));
		for (int i = 0; i < groundPoints; i++)
		{
			float px = (getX() - getWidth()/2) + (i * dist); 
			Point p = new Point((int)px,(int)(getY() + getHeight()/2));
			
			Rectangle r = new Rectangle(p.x - 1, p.y - 1, 2,2);
			if (oColl.contains(p))
				touching++;
				
		}

		return (touching/groundPoints) > groundTolerance;
	}
	/* getters and setters */
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;	
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public ID getId() {
		return id;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getMaxHP() {
		return maxHP;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;

	}

	public boolean isOnWall() {
		return onWall;
	}

	public void setOnWall(boolean onWall) {
		this.onWall = onWall;
	}

	public float getForceX() {
		return forceX;
	}

	public void setForceX(float forceX) {
		this.forceX = forceX;
	}

	public float getForceY() {
		return forceY;
	}

	public void setForceY(float forceY) {
		this.forceY = forceY;
	}
	
	public void addForceX(float fx)
	{
		forceX += fx;
	}
	
	public void addForceY(float fy)
	{
		forceY += fy;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public Animator getAnimator() {
		return animator;
	}

	protected int getSpriteWidth() {
		return spriteWidth;
	}
	
	protected int getSpriteHeight() {
		return spriteHeight;
	}

	public float getDistFromGround() {
		return distFromGround;
	}

	public void setDistFromGround(float distFromGround) {
		this.distFromGround = distFromGround;
	}

	public boolean isGround() {
		return isGround;
	}

	public void setGround(boolean isGround) {
		this.isGround = isGround;
	}

	public boolean isCeiling() {
		return isCeiling;
	}

	public void setCeiling(boolean isCeiling) {
		this.isCeiling = isCeiling;
	}
	
	
}
