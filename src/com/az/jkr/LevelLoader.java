package com.az.jkr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

public class LevelLoader {

	
	
	public void loadLevel(int level)
	{	
		Main.gameObjectHandler.clearAll();
		
		//main menu
		if (level == 0)
		{
			Main.gameObjectHandler.addMenu(new MainMenu());
		}
		
		if (level == 1)
		{
			try
			{
				
				BufferedReader br = new BufferedReader(new FileReader(getClass().getResource("../../../levels/levelOne.txt").getPath()));
				String line = "";
				char curr = '/';
				float x = 0;
				float y = 0;
				
				Queue<Enemy> enemyQueue = new LinkedList<Enemy>();
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
						
						else if (curr == 'e')
						{
							//enemies need player/waypoint already in level to set their goal
							//so, will be added in after everything else has been loaded
							Enemy e = new BasicRunner(x,y);
							enemyQueue.add(e);
							Main.gameObjectHandler.addGameObject(e);
						}
							
						x += 64;
					}
					x = 0;
					y += 64;
				}
				while (!enemyQueue.isEmpty())
					enemyQueue.remove().setInitialTarget();
				br.close();
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}


