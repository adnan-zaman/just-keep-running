package com.az.jkr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LevelLoader {

	
	public enum Level
	{
		one;
	}
	
	public void loadLevel(Level l)
	{	
		if (l == Level.one)
		{
			try
			{
				
				BufferedReader br = new BufferedReader(new FileReader(getClass().getResource("../../../levels/levelOne.txt").getPath()));
				String line = "";
				char curr = '/';
				float x = 0;
				float y = 0;
				while ((line = br.readLine()) != null)
				{
					for (int i = 0; i < line.length(); i++)
					{
						curr = line.charAt(i);
						//wall
						if (curr == 'x')
						{
							Wall w = new Wall(x,y);
							Main.gameObjectHandler.addGameObject(w);
						}
						if (curr == 'c')
						{
							Wall w = new Wall(x,y);
							w.isCeiling = true;
							Main.gameObjectHandler.addGameObject(w);
						}
						if (curr == 'y')
						{
							Wall w = new Wall(x,y);
							w.isCeiling = true;
							w.isGround = true;
							Main.gameObjectHandler.addGameObject(w);
						}	
						if (curr == 'g')
						{
							Wall w = new Wall(x,y);
							w.isGround = true;
							Main.gameObjectHandler.addGameObject(w);
						}
						//player
						else if (curr == 'p')
							Main.gameObjectHandler.addGameObject(new Player(x,y));					
						x += 64;
					}
					x = 0;
					y += 64;
				}
					br.close();
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}


