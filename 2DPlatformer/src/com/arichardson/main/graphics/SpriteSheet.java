package com.arichardson.main.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private int width;
	private int height;
	private int size;
	private BufferedImage image;
	
	public SpriteSheet(String filePath, int size) {
		image = retrieveImage(filePath);
		width = image.getWidth();
		height = image.getHeight();
		this.size = size;
	}
	
	public BufferedImage retrieveImage(String filePath) {
		BufferedImage image = null;
		try {
			File file = new File(filePath);
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}

	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getSize(){
		return size;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
}
