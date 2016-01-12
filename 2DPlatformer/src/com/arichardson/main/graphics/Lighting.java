package com.arichardson.main.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import com.arichardson.main.Level;
import com.arichardson.main.entities.Player;

public class Lighting {

	Level level;
	Player player;
	
	private int width, height;
	
	public int lightX, lightY;
	private int lightBallSize = 8;
	private int lightFalloff = 75;
	private int lightPasses = 100;
	private int lightStep = 2;
	private int lightStartA = 0;
	private int lightA = 360;
	
	private int lightRed = 255;
	private int lightBlue = 255;
	private int lightGreen = 255;
	
	public Lighting(Level level, Player player, int boundX, int boundY){
		this.level = level;
		this.player = player;
		width = boundX;
		height = boundY;
		lightX = width/2;
		lightY = height/2;
	}
	
	public void lightScene(Graphics2D g) {
		for(int l = 0; l < lightPasses; l++){
			for(int r = 1; r < lightFalloff+1; r += lightStep){
				int x = (int)(lightX*(r*Math.cos(Math.toRadians(((double)l/lightPasses)*lightA + lightStartA))));
				int y = (int)(lightY*(r*Math.sin(Math.toRadians(((double)l/lightPasses)*lightA + lightStartA))));
				
				boolean collide = false;
				
				if(level.coords[x][y] || player.playerRect.intersects(x-lightBallSize/2, y-lightBallSize/2, lightBallSize, lightBallSize)){
					r = lightFalloff+1;
					collide = true;
				}
				
				if(!collide){
					g.setColor(new Color(lightRed-(int)(((double)r/lightFalloff)*lightRed),lightBlue-(int)(((double)r/lightFalloff)*lightBlue),lightGreen-(int)(((double)r/lightFalloff)*lightGreen)));
					g.fillOval(x, y, lightBallSize+(r/(lightFalloff/5)), lightBallSize+(r/(lightFalloff/5)));
				}
			}
		}
	}
	
	public void renderLight(Graphics2D g, int xx, int yy, int size, float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		for(int l = 0; l < lightPasses; l++){
			for(int r = 1; r < size+1; r += lightStep){
				int x = (int)(xx*(r*Math.cos(Math.toRadians(((double)l/lightPasses)*360))));
				int y = (int)(yy*(r*Math.sin(Math.toRadians(((double)l/lightPasses)*360))));
				
				g.setColor(new Color(lightRed-(int)(((double)r/size)*lightRed),lightBlue-(int)(((double)r/size)*lightBlue),lightGreen-(int)(((double)r/size)*lightGreen)));
				g.fillOval(x, y, size, size);
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
	}
	
}
