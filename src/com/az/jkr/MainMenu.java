package com.az.jkr;

import java.awt.Color;

/**
 * The main menu of the game
 *
 */

public class MainMenu extends Menu{

	private GameObject pointer;
	
	public MainMenu()
	{
		super(Color.black);
		
	}
	
	@Override
	public void loadMenu() 
	{
		
		uiObjects.add(new UI_Image("title.png", Main.width/2 , Main.height/2 - 150,244,25));
		uiObjects.add(new UI_Image("play.png", Main.width/2 , Main.height/2,96,32));
		uiObjects.add(new UI_Image("quit.png", Main.width/2 , Main.height/2 + 50,96,32));
		//set up pointer to be pointing at play by default
		pointer = new UI_Image("pointer.png", Main.width/2 , Main.height/2,32,32); 
		setCurrIndex(1);
		uiObjects.add(pointer);
		updatePointer();
	}
	
	@Override
	public void tick()
	{
		processInput();
		super.tick();
	}
	
	@Override
	public void processInput() {
		
		//modulus on size-1 because we want to skip last index (the pointer)
		if (Main.inputHandler.isKeyDown("up"))
		{
			currIndex = ((currIndex + 1) % (uiObjects.size() - 1)); 
			//skip index 0 (title)
			currIndex = (currIndex == 0) ? 1 : currIndex;
			Main.inputHandler.consume("up");
		}
		else if (Main.inputHandler.isKeyDown("down"))
		{
			currIndex = ((currIndex - 1) % (uiObjects.size() - 1)); 
			//skip index 0 (title)
			currIndex = (currIndex == 0) ? uiObjects.size() - 2 : currIndex;
			Main.inputHandler.consume("down");
		}
		else if (Main.inputHandler.isKeyDown("jump"))
		{
			Main.inputHandler.consume("jump");
			select();
		}
		
			
		updatePointer();
	}
	
	
	@Override
	public void select()
	{
		//play
		if (currIndex == 1)
			Main.changeState(Main.state.Game);
		//quit
		if (currIndex == 2)
			System.exit(0);
	}
	
	/**
	 * Update the position of the pointer
	 */
	private void updatePointer()
	{
		GameObject currObj = uiObjects.get(currIndex);
		pointer.setX(currObj.getX() -  currObj.getWidth()/2 - pointer.getWidth());
		pointer.setY(currObj.getY());
	}
	
	

}
