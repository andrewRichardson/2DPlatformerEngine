package com.arichardson.main.entities;

import java.awt.Polygon;

import com.arichardson.main.Level;
import com.arichardson.main.input.InputHandler;

public class Player {
	
	InputHandler input;
	public Level level;

	public Polygon playerRect;
	public int px = 0;
	public int py = 0;
	private int velX = 0;
	private int velY = 0;
	private int speedX = 1;
	private int speedY = 2;
	private int gravity = 1;
	private int width, height;
	
	public Player(Level level, InputHandler inputHandler, int gravity, int boundX, int boundY){
		this.level = level;
		input = inputHandler;
		width = boundX;
		height = boundY;
		
		px = width / 2 - level.size / 2;
		py = height / 2 - level.size / 2;
		
		int[] xPoints = {px, px, px+level.size, px+level.size*2, px+level.size*2};
		int[] yPoints = {py, py+level.size*4-level.size, py+level.size*4, py+level.size*4-level.size, py};
		
		playerRect = new Polygon(xPoints, yPoints, 5);
	}
	
	public void update(){
		int oldx = px;
		int oldy = py;

		if(Math.abs(velY) < 5){
			if (input.up) {
				velY -= speedY;
			}
		}
		/*
		if (input.down) {
			velY += playerSpeed;
		}*/
		
		if(Math.abs(velX) < 4){
			if (input.left) {
				velX -= speedX;
			}
			if (input.right) {
				velX += speedX;
			}	
		}
		else{
			velX += (velX < 0) ? 1 : -1;
		}
		
		px += velX;
		py += velY;
		
		if(velY < 10)
			velY += gravity;

		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);

		if (px > width - level.size || px < 0)
			px = oldx;

		if (py > height - level.size || py < 0)
			py = oldy;

		for (int i = 0; i < level.colliders.length; i++) {
			if (playerRect.intersects(level.colliders[i])) {
				px = oldx;
				i = level.colliders.length;
				if(velX != 0)
					velX -= speedX/2 * (velX/Math.abs(velX));
			}
		}
		
		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
		
		for (int i = 0; i < level.colliders.length; i++) {
			if (playerRect.intersects(level.colliders[i])) {
				py = oldy;
				velY = 0;
				i = level.colliders.length;
			}
		}
		
		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
	}
	
	@SuppressWarnings("unused")
	private void resetPlayer() {
		px = width / 2 - level.size / 2;
		py = height / 2 - level.size / 2;

		int[] xPoints = {px, px, px+level.size, px+level.size*2, px+level.size*2};
		int[] yPoints = {py, py+level.size*4-level.size, py+level.size*4, py+level.size*4-level.size, py};
		playerRect.xpoints = xPoints;
		playerRect.ypoints = yPoints;
	}
	
}
