package com.arichardson.main.graphics;

import java.awt.Color;

import com.arichardson.main.Level;
import com.arichardson.main.input.InputHandler;
import com.arichardson.main.api.Graphics;

public class Drawing {
	
	public Level level;
	private InputHandler input;
	public int mouseX, mouseY;
	public boolean mouseIsHovering = false;
	public int tileHovered = 0;
	private double blockDistance = 0;
	public double maxDistance = 10000;//level.size*10;
	public int playerX = 0;
	public int playerY = 0;
	public int playerwidth = 0;
	public int playerheight = 0;
	
	public int brushRadius = 1;
	public double scrollNumber = 2;
	private double scrollUnits = 2;
	public boolean brushShape = false;
	
	private int breakOrPlace = 0;
	public Color canChangeBlock_Color;
	
	public Drawing(int width, int height, int tileSize, Color color, InputHandler inputHandler){
		level = new Level(width, height, tileSize, color, playerheight);
		this.input = inputHandler;
		canChangeBlock_Color = Color.ORANGE;
	}
	
	public Drawing(int width, int height, int tileSize, Color color, InputHandler inputHandler, String tileSet){
		level = new Level(width, height, tileSize, color, tileSet, playerheight);
		this.input = inputHandler;
		canChangeBlock_Color = Color.ORANGE;
	}
	
	public void update(){
		if(input.mouseScrollUp){
			scrollNumber++;
			System.out.println("Scrolling... " + brushRadius);
		}
		if(input.mouseScrollDown){
			scrollNumber--;
			System.out.println("Scrolling... " + brushRadius);
		}
		if(scrollNumber < 0)
			scrollNumber = 0;
		brushRadius = (int)(scrollNumber/scrollUnits);
		
		input.mouseScrollUp = false;
		input.mouseScrollDown = false;
		
		updateBlockDistance(0, 0);
		
		boolean previousValue = brushShape;
		brushShape = input.ctrl;
		if(previousValue != brushShape)
			System.out.println("Brush shape is set to " + ((brushShape)?"square":"circle")+".");
		
		if(breakOrPlace == 1){
			canChangeBlock_Color = Color.GREEN;
		} else if(breakOrPlace == 0){
			canChangeBlock_Color = Color.ORANGE;
		}
		
		int x = (int)(mouseX/level.size);
		int y = (int)(mouseY/level.size);
		
		if(input.mouseLeft && !input.escape){
			if(breakOrPlace == 0){
				tryBreakBlock();
			}
			if(breakOrPlace == 1){
				tryPlaceBlock();
			}
			if(breakOrPlace == 2){
				System.out.println("Placing Spawn Point at "+ x + ", "+ y);
				level.tileMap.tiles[(level.spawnPoint[0])/level.size][(level.spawnPoint[1]+playerheight-1)/level.size] = 0;
				level.tileMap.tiles[x][y] = 2;
				level.spawnPoint[0] = x*level.size;
				level.spawnPoint[1] = y*level.size-level.size;
			}
		}
		
		checkMouseHover();
	}
	
	public void changeBrushType(int i){
		breakOrPlace = i;
	}
	
	public void render(Graphics graphics){
		level.drawTileMap(graphics);
		
		int x = (int)(mouseX/level.size);
		int y = (int)(mouseY/level.size);
		if(brushShape && breakOrPlace != 2){
			for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
				for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						updateBlockDistance(xx*level.size, yy*level.size);
						if(blockDistance <= maxDistance || input.shift)
							graphics.drawRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size, true, canChangeBlock_Color, 0.5f);
						else
							graphics.drawRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size, true, Color.RED, 0.5f);
					}
				}
			}
		} else if(!brushShape && breakOrPlace != 2){
			for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
				for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						updateBlockDistance(xx*level.size, yy*level.size);
						if(xx*xx + yy*yy <= brushRadius*brushRadius){
							if(blockDistance <= maxDistance || input.shift)
								graphics.drawRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size, true, canChangeBlock_Color, 0.5f);
							else
								graphics.drawRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size, true, Color.RED, 0.5f);
						}
					}
				}
			}
		} else if(breakOrPlace == 2){
			graphics.drawRect(x*level.size, y*level.size, level.size, level.size, true, Color.WHITE);
		}
	}
	
	public void updateBlockDistance(int xOffset, int yOffset){
		blockDistance = Math.sqrt(((mouseX+xOffset)-(playerX+playerwidth/2))*((mouseX+xOffset)-(playerX+playerwidth/2)) + 
				((mouseY+yOffset)-(playerY+playerheight/2))*((mouseY+yOffset)-(playerY+playerheight/2)));
		
	}
	
	public void tryBreakBlock(){
		int[] tileCoords = level.findTileFromCoordinate(mouseX, mouseY);
		int x = tileCoords[0];
		int y = tileCoords[1];
		//if(mouseIsHovering && blockDistance <= maxDistance || input.shift){
			if(brushRadius == 1){
				if(x < level.tileMap.tiles.length && x > -1 && y < level.tileMap.tiles[1].length && y > -1 && blockDistance <= maxDistance){
					level.tileMap.tiles[x][y] = 0;
					level.getColliders();
					level.fillColliders();
					System.out.println("Breaking block: "+x+", "+y);
				}
			} else {
				changeTiles(x, y, 0);
				System.out.println("Breaking block: "+x+", "+y+" w/ radius of "+brushRadius+" tiles.");
			}
		//}
	}
	
	public void tryPlaceBlock(){
		int[] tileCoords = level.findTileFromCoordinate(mouseX, mouseY);
		int x = tileCoords[0];
		int y = tileCoords[1];
		
		//if(blockDistance <= maxDistance|| input.shift){
			if(brushRadius == 1){
				if(x < level.tileMap.tiles.length && x > -1 && y < level.tileMap.tiles[1].length && y > -1 && blockDistance <= maxDistance){
					level.tileMap.tiles[x][y] = 1;
					level.getColliders();
					level.fillColliders();
					System.out.println("Placing block: "+x+", "+y);
				}
			} else {
				changeTiles(x, y, 1);
				System.out.println("Placing block: "+x+", "+y+" w/ radius of "+brushRadius+" tiles.");
			}
		//}
	}
	
	public void changeTiles(int x, int y, int data){
		if(brushShape){
			for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
				for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						updateBlockDistance(xx*level.size, yy*level.size);
						if(blockDistance <= maxDistance || input.shift)
							level.tileMap.tiles[x+xx][y+yy] = data;
					}
				}
			}
			level.getColliders();
			level.fillColliders();
		}
		else if (!brushShape){
			for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
				for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
					if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
						updateBlockDistance(xx*level.size, yy*level.size);
						if(blockDistance <= maxDistance || input.shift)
							if(xx*xx + yy*yy <= brushRadius*brushRadius)
								level.tileMap.tiles[x+xx][y+yy] = data;
					}
			    }
			}
			level.getColliders();
			level.fillColliders();
		}
		level.tileMap.checkTileBorders();
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
