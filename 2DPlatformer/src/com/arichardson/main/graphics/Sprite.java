package com.arichardson.main.graphics;

import java.awt.Image;

public class Sprite {

	private Image image;
	private int width;
	private int height;
	
	public Sprite(SpriteSheet spriteSheet, int index) {
		image = retrieveImage(spriteSheet, index);
	}
	
	private Image retrieveImage(SpriteSheet spriteSheet, int index){
		int x = index%(spriteSheet.getImage().getWidth()/spriteSheet.getSize());
		int y = (int)Math.floor(index/(spriteSheet.getWidth()/spriteSheet.getSize()));
		return spriteSheet.getImage().getSubimage(x, y, spriteSheet.getSize(), spriteSheet.getSize());
	}
	
	public Image getImage(){
		return image;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
