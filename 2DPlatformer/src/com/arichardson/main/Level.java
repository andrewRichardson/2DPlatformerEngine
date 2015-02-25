package com.arichardson.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.arichardson.main.graphics.TileMap;

public class Level {

	public TileMap tiles;
	private int width;
	private int height;
	private int size;
	private Color color;
	public Rectangle[] ret;
	public boolean[][] coords;

	public Level(int w, int h, int s, Color col) {
		width = w;
		height = h;
		size = s;
		color = col;
		tiles = new TileMap(width, height, size);
		coords = new boolean[width][height];
		
		getColliders();
		fillColliders();
	}

	public void drawTileMap(Graphics2D g) {
		for (int i = 0; i < tiles.floors.length; i++) {
			for (int j = 0; j < tiles.floors[0].length; j++) {
				if(tiles.floors[i][j] == 1){
					g.setColor(color);
					g.fillRect(i * size, j	* size, size, size);
				}
			}
		}
	}

	public void getColliders() {
		ArrayList<Rectangle> colliders = new ArrayList<Rectangle>();

		for (int i = 0; i < tiles.floors.length; i++) {
			for (int j = 0; j < tiles.floors[0].length; j++) {
				if (tiles.floors[i][j] == 1) {
					Rectangle rect = new Rectangle(i * size, j * size, size, size);
					colliders.add(rect);
				}
			}
		}

		ret = new Rectangle[colliders.size()];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = colliders.get(i);
		}
	}

	public void resetLevel() {
		tiles = new TileMap(width, height, size);
	}
	
	public void fillColliders(){
		coords = new boolean[width][height];
		for(int i = 0; i < ret.length; i++){
			int xx = ret[i].x;
			int yy = ret[i].y;
			int w = ret[i].width;
			int h = ret[i].height;
			
			for(int x = xx; x < xx+w; x++){
				for(int y = yy; y < yy+h; y++){
					coords[x][y] = true;
				}
			}
		}
	}

}
