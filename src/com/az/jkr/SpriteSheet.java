package com.az.jkr;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Contains methods for getting sprite sheets
 * and subimages from spritesheets
 *
 */

public class SpriteSheet {

	public static BufferedImage getSpriteSheet(String sheetName) throws IOException
	{
		return ImageIO.read(new File(Main.gameObjectHandler.getClass().
					getResource("../../../spriteSheets/" + sheetName).getFile()));
	}
	
	public static BufferedImage getSubImage(BufferedImage sheet, int imgNum, int tileWidth, int tileHeight)
	{
		//Calculating target coords taken from:
		//https://gamedevelopment.tutsplus.com/tutorials/an-introduction-to-spritesheet-animation--gamedev-13099
		
		int x = imgNum % (sheet.getWidth() / tileWidth);  //mod will cause x value to loop around
		int y = imgNum / (sheet.getHeight() / tileHeight); //y only increases on multiples (every row)
		
		return sheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
	}
}
