package com.az.jkr;

import java.awt.Color;

public class TestMenu extends Menu {

	public TestMenu() {
		super(Color.black, 1);
	}

	public void tick()
	{
		processInput();
	}
	
	@Override
	public void loadMenu() {
		uiObjects.add(new UI_Image("title.png", Main.width/2 , Main.height/2 + 150,244,25));
		uiObjects.add(new UI_Image("play.png", Main.width/2 , Main.height/2,96,32));
		
	}

	@Override
	public void processInput() {
		if (Main.inputHandler.isKeyDown("jump"))
		{
			Main.inputHandler.consume("jump");
			Main.changeState(GameState.LevelOne);
		}

	}

}
