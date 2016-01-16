package com.arichardson.main.entities;

import java.awt.Polygon;
import java.awt.Rectangle;

import com.arichardson.main.Level;
import com.arichardson.main.input.InputHandler;

public class Player {
	
	InputHandler input;
	public Level level;

	public Polygon playerRect;
	public Rectangle playerTopCollider;
	public Rectangle playerBottomCollider;
	public int px = 0;
	public int py = 0;
	private int velX = 0;
	private int velY = 0;
	private int speedX = 1;
	private int speedY = 6;
	private int gravity = 1;
	private int width, height;
	private boolean jumped = false;
	private boolean falling = false;
	
	public Player(Level level, InputHandler inputHandler, int gravity, int boundX, int boundY){
		this.level = level;
		input = inputHandler;
		width = boundX;
		height = boundY;
		
		speedX *= (level.size/8);
		speedY *= (level.size/8);
		gravity *= (level.size/8);
		
		px = level.spawnPoint[0];
		py = level.spawnPoint[1];
		
		int[] xPoints = {px, px, px+level.size/2, px+level.size, px+level.size};
		int[] yPoints = {py, py+level.size*3-level.size, py+level.size*3, py+level.size*3-level.size, py};
		
		playerRect = new Polygon(xPoints, yPoints, 5);
		
		playerTopCollider = new Rectangle(px-2, py, level.size+4, level.size);
		playerBottomCollider = new Rectangle(px, py+level.size*2, level.size, level.size-level.size/8);
	}
	
	public void update(){
		int oldx = px;
		int oldy = py;

		if(Math.abs(velY) < speedY/2 && !jumped){
			if (input.up) {
				velY = -speedY;
				jumped = true;
			}
		}
		
		if(velY == 0 && jumped && falling && !input.up){
			jumped = false;
			falling = false;
		}
		
		if(velY == 0 && jumped)
			falling = true;
		/*
		if (input.down) {
			velY += playerSpeed;
		}*/
		
		if(Math.abs(velX) < speedX*4){
			if (input.left) {
				velX -= speedX;
			}
			if (input.right) {
				velX += speedX;
			}	
		}
		else{
			velX += (velX < 0) ? speedX : -speedX;
		}
		
		px += velX;
		py += velY;
		
		if(velY < gravity*15)
			velY += gravity;

		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
		playerTopCollider.x = px-2;
		playerTopCollider.y = py;
		playerBottomCollider.x = px;
		playerBottomCollider.y = py+level.size*2;

		if (px > width - level.size || px < 0)
			px = oldx;

		if (py > height - level.size || py < 0)
			py = oldy;
		
		boolean colliderFlag = false;
		boolean bColliderFlag = false;
		
		for (int i = 0; i < level.colliders.length; i++) {
			if(playerTopCollider.intersects(level.colliders[i]))
				colliderFlag = true;
			if(playerBottomCollider.intersects(level.colliders[i]))
				bColliderFlag = true;
		}
		
		for (int i = 0; i < level.colliders.length; i++) {
			if (playerRect.intersects(level.colliders[i]) && velY == speedX && Math.abs(velX) > 0 && !colliderFlag && bColliderFlag) {
				py = oldy-speedX*3;
				velY = 0;
				i = level.colliders.length;
				if(velX != 0)
					velX -= speedX * (velX/Math.abs(velX));
			}
		}
		
		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
		playerTopCollider.x = px-2;
		playerTopCollider.y = py;
		playerBottomCollider.x = px;
		playerBottomCollider.y = py+level.size*2;

		for (int i = 0; i < level.colliders.length; i++) {
			if (playerRect.intersects(level.colliders[i])) {
				px = oldx;
				i = level.colliders.length;
				if(velX != 0)
					velX -= speedX * (velX/Math.abs(velX));
			}
		}
		
		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
		playerTopCollider.x = px-2;
		playerTopCollider.y = py;
		playerBottomCollider.x = px;
		playerBottomCollider.y = py+level.size*2;
		
		for (int i = 0; i < level.colliders.length; i++) {
			if (playerRect.intersects(level.colliders[i])) {
				py = oldy;
				velY = 0;
				i = level.colliders.length;
			}
		}
		
		playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
		playerRect.translate(px, py);
		playerTopCollider.x = px-2;
		playerTopCollider.y = py;
		playerBottomCollider.x = px;
		playerBottomCollider.y = py+level.size*2;
	}
	
	public void resetPlayer() {
		px = level.spawnPoint[0];
		py = level.spawnPoint[1];
	}
	
}
