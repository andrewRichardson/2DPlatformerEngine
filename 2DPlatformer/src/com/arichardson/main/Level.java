package com.arichardson.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.arichardson.main.graphics.TileMap;

public class Level {

	public TileMap tileMap;
	public int width, height;
	public int size;
	public Color color;
	public Rectangle[] colliders;
	public boolean[][] coords;

	public Level(int w, int h, int s, Color col) {
		width = w;
		height = h;
		size = s;
		color = col;
		tileMap = new TileMap(width, height, size);
		coords = new boolean[width][height];
		
		getColliders();
		fillColliders();
	}

	public void drawTileMap(Graphics2D g) {
		for (int i = 0; i < tileMap.tiles.length; i++) {
			for (int j = 0; j < tileMap.tiles[0].length; j++) {
				if(tileMap.tiles[i][j] == 1){
					boolean n = false;
					boolean s = false;
					boolean e = false;
					boolean w = false;
					g.setColor(color);
					g.fillRect(i * size, j	* size, size, size);
					
					g.setColor(Color.LIGHT_GRAY);
					if(i != 0){
						if(tileMap.tiles[i-1][j] == 1)
							w = true;
					}
					if(j != 0){
						if(tileMap.tiles[i][j-1] == 1)
							n = true;
					}
					if(j != tileMap.tiles[0].length-1){
						if(tileMap.tiles[i][j+1] == 1)
							s = true;
					}
					if(i != tileMap.tiles.length-1){
						if(tileMap.tiles[i+1][j] == 1)
							e = true;
					}
					if(n && !s && !e && !w){
						g.drawLine(i*size, j*size, i*size, (j+1)*size);//west
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);//east
					}
					if(!n && s && !e && !w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);
						g.drawLine(i*size, j*size, i*size, (j+1)*size);
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);
					}
					if(!n && !s && e && !w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);
						g.drawLine(i*size, j*size, i*size, (j+1)*size);
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);
					}
					if(!n && !s && !e && w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);
					}
					if(n && s && !e && !w){
						g.drawLine(i*size, j*size, i*size, (j+1)*size);
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);
					}
					if(!n && s && e && !w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);//north
						g.drawLine(i*size, j*size, i*size, (j+1)*size);//west
					}
					if(!n && !s && e && w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);//north
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
					}
					if(n && s && e && !w){
						g.drawLine(i*size, j*size, i*size, (j+1)*size);//west
					}
					if(!n && s && e && w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);//north
					}
					if(n && !s && e && w){
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
					}
					if(n && s && !e && w){
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);//east
					}
					if(n && !s && e && !w){
						g.drawLine(i*size, j*size, i*size, (j+1)*size);//west
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
					}
					if(!n && s && !e && w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);//north
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);//east
					}
					if(n && !s && !e && w){
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);//east
					}
					if(!n && !s && !e && !w){
						g.drawLine(i*size, j*size, (i+1)*size, j*size);//north
						g.drawLine(i*size, j*size, i*size, (j+1)*size);//west
						g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size);//south
						g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size);//east
					}
				}
			}
		}
	}

	public void getColliders() {
		ArrayList<Rectangle> colliders = new ArrayList<Rectangle>();

		for (int i = 0; i < tileMap.tiles.length; i++) {
			for (int j = 0; j < tileMap.tiles[0].length; j++) {
				if (tileMap.tiles[i][j] == 1) {
					Rectangle rect = new Rectangle(i * size, j * size, size, size);
					colliders.add(rect);
				}
			}
		}

		this.colliders = new Rectangle[colliders.size()];

		for (int i = 0; i < this.colliders.length; i++) {
			this.colliders[i] = colliders.get(i);
		}
	}

	public void resetLevel() {
		tileMap = new TileMap(width, height, size);
	}
	
	public void fillColliders(){
		coords = new boolean[width][height];
		for(int i = 0; i < colliders.length; i++){
			int xx = colliders[i].x;
			int yy = colliders[i].y;
			int w = colliders[i].width;
			int h = colliders[i].height;
			
			for(int x = xx; x < xx+w; x++){
				for(int y = yy; y < yy+h; y++){
					coords[x][y] = true;
				}
			}
		}
	}
	
	public int[] findTileFromCoordinate(int x, int y){
		int remainder = x%size;
		int gridX = (x-remainder)/size;
		
		remainder = y%size;
		int gridY = (y-remainder)/size;
		
		return new int[]{gridX, gridY};
	}

}
