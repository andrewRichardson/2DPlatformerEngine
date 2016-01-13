package com.arichardson.main.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import com.arichardson.main.Level;
import com.arichardson.main.input.InputHandler;

public class Drawing {
	
	public Level level;
	private InputHandler input;
	public int mouseX, mouseY;
	public boolean mouseIsHovering = false;
	public int tileHovered = 0;
	public double blockDistance = 0;
	public double maxDistance = 70;
	
	private int brushRadius = 1;
	private double scrollNumber = 2;
	private double scrollUnits = 2;
	public boolean brushShape = false;
	
	private boolean breakOrPlace = false;
	
	public Drawing(int width, int height, int tileSize, Color color, InputHandler inputHandler){
		level = new Level(width, height, tileSize, color);
		this.input = inputHandler;
	}
	
	public void update(){
		if(input.mouseScrollUp){
			scrollNumber++;
			System.out.println("Scrolling... " + scrollNumber);
		}
		if(input.mouseScrollDown){
			scrollNumber--;
			System.out.println("Scrolling... " + scrollNumber);
		}
		if(scrollNumber < 0)
			scrollNumber = 0;
		brushRadius = (int)(scrollNumber/scrollUnits);
		
		input.mouseScrollUp = false;
		input.mouseScrollDown = false;
		
		boolean previousValue = brushShape;
		brushShape = input.ctrl;
		if(previousValue != brushShape)
			System.out.println("Brush shape is set to " + ((brushShape)?"square":"circle")+".");
		
		if(input.mouseLeft){
			if(!breakOrPlace){
				tryBreakBlock();
			}
			if(breakOrPlace){
				tryPlaceBlock();
			}
		}
		
		checkMouseHover();
	}
	
	public void changeBrushType(boolean i){
		breakOrPlace = i;
	}
	
	public void render(Graphics2D g){
		level.drawTileMap(g);
		
		if(mouseIsHovering){
			if(blockDistance <= maxDistance)
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.RED);
			g.drawRect(level.colliders[tileHovered].x-1, level.colliders[tileHovered].y-1, level.colliders[tileHovered].width+2, level.colliders[tileHovered].height+2);
		}
	}
	
	public void tryBreakBlock(){
		int[] tileCoords = level.findTileFromCoordinate(mouseX, mouseY);
		int x = tileCoords[0];
		int y = tileCoords[1];
		
		if(mouseIsHovering && blockDistance <= maxDistance || input.shift){
			if(brushRadius == 1){
					level.tileMap.tiles[x][y] = 0;
					level.getColliders();
					level.fillColliders();
					System.out.println("Breaking block: "+x+", "+y);
			} else {
				changeTiles(x, y, 0);
				System.out.println("Breaking block: "+x+", "+y+" w/ radius of "+brushRadius+" tiles.");
			}
		}
	}
	
	public void tryPlaceBlock(){
		int[] tileCoords = level.findTileFromCoordinate(mouseX, mouseY);
		int x = tileCoords[0];
		int y = tileCoords[1];
		
		if(blockDistance <= maxDistance|| input.shift){
			if(brushRadius == 1){
					level.tileMap.tiles[x][y] = 1;
					level.getColliders();
					level.fillColliders();
					System.out.println("Placing block: "+x+", "+y);
			} else {
				changeTiles(x, y, 1);
				System.out.println("Placing block: "+x+", "+y+" w/ radius of "+brushRadius+" tiles.");
			}
		}
	}
	
	public void changeTiles(int x, int y, int data){
		if(brushShape){
			for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
				for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						level.tileMap.tiles[x+xx][y+yy] = data;
					}
				}
			}
			level.getColliders();
			level.fillColliders();
		}
		else{
			int xx, yy, r;
			for(r = brushRadius-1; r > -1; r--){
				for(int i = 0; i < (int)(r*2*180); i += 1) {
					xx = (int)(r * Math.cos(i * Math.PI / 180));
					yy = (int)(r * Math.sin(i * Math.PI / 180));
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						level.tileMap.tiles[x+xx][y+yy] = data;
					}
			    }
			}
			level.getColliders();
			level.fillColliders();
		}
	}
	
	public void checkMouseHover(){
		for(int i = 0; i < level.colliders.length; i++){
			if(level.colliders[i].contains(mouseX, mouseY)){
				mouseIsHovering = true;
				tileHovered = i;
				i = level.colliders.length;
			}
			else
				mouseIsHovering = false;
		}
	}
}
