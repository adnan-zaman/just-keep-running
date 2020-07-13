package com.az.jkr;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;




/**
 * The main class of the game. Contains the game loop
 * and holds references to all other aspects of the game
 *
 */

public class Main extends Canvas{

	//fps
	public static int gameFPS = 60;
	//animation fps
	public static  int animFPS = 12;
	
	//dimensions
	public static int height, width;
	//holds all GameObjects
	public static GameObjectHandler gameObjectHandler;
	//input handler
	public static InputHandler inputHandler;
	//collision handler
	public static CollisionHandler collisionHandler;
	//physics system
	public static Physics physics;
	//camera
	public static Camera camera;
	//loader of levels
	public static LevelLoader levelLoader;
	//current state of the game
	public static GameState state;
	//current level
	public static int level;
	//for debugging, used with camera
	public static int FPS;
	
	
	//is game running
	private boolean playing;
	
	//current transition, if any
	private static Transition currTransition;
	

	
	public Main()
	{
		
		//setting up the frame
		width = 800; height = 600; 
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(width,height));
		frame.setResizable(false);
		frame.setTitle("Just Keep Running!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		
		//setting up game related things
		gameObjectHandler = new GameObjectHandler();
		inputHandler = new InputHandler();
		collisionHandler = new CollisionHandler();
		physics = new Physics();
		camera = new Camera(width/2,height/2);
		levelLoader = new LevelLoader();
		addKeyListener(inputHandler);
		state = GameState.MainMenu;
		currTransition = null;
		levelLoader.loadLevel(GameState.MainMenu);
		gameObjectHandler.finalize();
		
		
		//good to go
		camera.followingPlayer = false;
		playing = true;
		frame.setVisible(true);
	
		
		
		run(); 
		
	}
	
	public static void main(String[] args) 
	{
		new Main();

	}
	
	
	public static void changeLevel(int lvl)
	{
		level = lvl;
	
		if (lvl == 1)
			changeState(GameState.LevelOneTitle);

	}

	
	
	public void run()
	{
		//given max fps, how long is one frame (in nanoseconds)
		float oneFrame = 1000000000.0f / gameFPS;
		//same but for animation frames
		float oneAnimFrame = 1000000000.0f / animFPS;
		//current time in nanoseconds
		long now = 0;
		//holds time of last iteration of game loop
		long last = System.nanoTime();
		//how many frames have passed (can be a fraction)
		float delta = 0;
		//same but for anim frames
		float animDelta = 0;
		
		//for counting and displaying only 
		int countFrames = 0;
		long lastMilli = System.currentTimeMillis();
		
		requestFocus();
		//game loop
		while (playing)
		{
			
			now = System.nanoTime();
			//(now - last) time elapsed, divided by oneFrame gives
			//what fraction of a frame has passed
			delta += (now - last) / oneFrame;
			animDelta += (now - last) / oneAnimFrame;
			last = now;
			//keep calling tick for as many frames have passed
			//if it's at least 1
			if (delta >= 1.0f)
			{
				countFrames++;
				while (delta >= 1.0f)
				{
					
					tick();
					delta--;
				}
			}
			if (animDelta >= 1.0f)
			{
				while (animDelta >= 1.0f)
				{
					
					animTick();
					animDelta--;
				}
			}
			render();
			countFrames++;
			if (Math.abs(System.currentTimeMillis() - lastMilli) >= 1000)
			{
				FPS = countFrames;
				countFrames = 0;
				lastMilli = System.currentTimeMillis();
			}
		}
	}
	
	/**
	 * The main update method of the game. Depending on the current
	 * state, will call the tick methods of other appropriate objects
	 * e.g handler
	 */
	public void tick()
	{
		gameObjectHandler.tick();
		//System.out.println("-all ticks done");
		physics.applyGravity();
		//System.out.println("-Grav done");
		collisionHandler.checkCollisions();
		//System.out.println("-collisions checked");
		camera.tick();
		//System.out.println("-camera ticked");
		
		//game state management
		if (currTransition == null)
		{
			//transition to level one title done, waiting to enter level one
			//start loading level one
			if (state == GameState.LevelOneTitle)
			{
//				levelLoader.loadLevel(GameState.LevelOne);
			}
				
		}
		else
		{
			if (currTransition.isDone())
			{
				gameObjectHandler.finalize();
				System.out.println(gameObjectHandler.size());
				currTransition = null;
				if (state == GameState.LevelOne)
					camera.followingPlayer = true;
			}
			else
			{
				currTransition.tick();
			}			
		}
		
		
			
	
		
	}
	
	public void animTick()
	{
		gameObjectHandler.animTick();
	}
	
	/**
	 * The main render method of the game. Depending on the current
	 * state, will call the render methods of other appropriate objects
	 * e.g handler
	 */
	public void render()
	{
		//double buffering is cool
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(2);
			bs = getBufferStrategy();
		}
			
		Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();
	
		
		
		
		if (state == GameState.LevelOne)
		{
			g2.setColor(Color.black);
			g2.fillRect(0, 0, width, height);			
		}
		
		gameObjectHandler.render(g2,false);
		
		if(currTransition != null)
			currTransition.render(g2);
		
		
		
		bs.show();
		g2.dispose();
	
	}
	
	public static void changeState(GameState newState)
	{
		if (!validStateTransition(newState))
			return;
		
		if (newState == GameState.LevelOneTitle)
		{
			levelLoader.loadLevel(GameState.LevelOneTitle);
			currTransition = new FadeTransition(Color.black,1500,"in");
		}
		else if (newState == GameState.LevelOne)
		{
			levelLoader.loadLevel(GameState.LevelOne);
			currTransition = new FadeTransition(Color.black,1,"in");
		}
		
		
		state = newState;
	}
	
	private static boolean validStateTransition(GameState newstate)
	{
		return true;
	}

}
