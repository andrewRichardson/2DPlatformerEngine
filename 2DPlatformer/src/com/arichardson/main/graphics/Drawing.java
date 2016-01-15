package com.arichardson.main.graphics;

import java.awt.AlphaComposite;
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
	private double blockDistance = 0;
	public double maxDistance = 70;
	public int playerX = 0;
	public int playerY = 0;
	public int playerwidth = 0;
	public int playerheight = 0;
	
	private int brushRadius = 1;
	private double scrollNumber = 2;
	private double scrollUnits = 2;
	public boolean brushShape = false;
	
	private boolean breakOrPlace = false;
	public Color canChangeBlock_Color;
	
	public Drawing(int width, int height, int tileSize, Color color, InputHandler inputHandler){
		level = new Level(width, height, tileSize, color);
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
		
		if(breakOrPlace){
			canChangeBlock_Color = Color.GREEN;
		} else {
			canChangeBlock_Color = Color.ORANGE;
		}
		
		if(input.mouseLeft && !input.escape){
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
		
		//if(mouseIsHovering){
			int x = (int)(mouseX/level.size);
			int y = (int)(mouseY/level.size);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			if(brushShape){
				for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
					for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
						if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
							updateBlockDistance(xx*level.size, yy*level.size);
							if(blockDistance <= maxDistance || input.shift)
								g.setColor(canChangeBlock_Color);
							else
								g.setColor(Color.RED);
							//if(xx < -(brushRadius-2) || xx > brushRadius-2 || yy < -(brushRadius-2) || yy > brushRadius-2)
								g.fillRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size);
						}
					}
				}
			}
			else{
				for(int xx = -(brushRadius-1); xx < brushRadius; xx++){
					for(int yy = -(brushRadius-1); yy < brushRadius; yy++){
						if(x+xx < level.tileMap.tiles.length && x+xx > -1 && y+yy < level.tileMap.tiles[1].length && y+yy > -1){
							updateBlockDistance(xx*level.size, yy*level.size);
							if(blockDistance <= maxDistance || input.shift)
								g.setColor(canChangeBlock_Color);
							else
								g.setColor(Color.RED);
							if(xx*xx + yy*yy <= brushRadius*brushRadius)
								g.fillRect((x+xx)*level.size, (y+yy)*level.size, level.size, level.size);
						}
					}
				}
			}
		//}
		g.setColor(Color.WHITE);
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
					level.tileMap.tiles[x][y] = 0;
					level.getColliders();
					level.fillColliders();
					System.out.println("Breaking block: "+x+", "+y);
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
					level.tileMap.tiles[x][y] = 1;
					level.getColliders();
					level.fillColliders();
					System.out.println("Placing block: "+x+", "+y);
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
		else{
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
