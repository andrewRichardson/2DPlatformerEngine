package com.arichardson.main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import com.arichardson.main.api.Graphics;
import com.arichardson.main.graphics.TileMap;

public class Level {

	public TileMap tileMap;
	public int width, height;
	public int size;
	public Color color;
	public Rectangle[] colliders;
	public boolean[][] coords;
	public int[] spawnPoint;

	public Level(int w, int h, int s, Color col, int playerHeight) {
		width = w;
		height = h;
		size = s;
		color = col;
		tileMap = new TileMap(width, height, size);
		coords = new boolean[width][height];

		spawnPoint = new int[]{tileMap.tiles.length/2*size, tileMap.tiles[0].length/2*size+playerHeight-size};
		tileMap.tiles[(int)(spawnPoint[0]/size)][(int)(spawnPoint[1]/size)+1] = 2;
		
		getColliders();
		fillColliders();
		//tileMap.checkTileBorders();
	}
	
	public Level(int w, int h, int s, Color col, String tileSet, int playerHeight) {
		width = w;
		height = h;
		size = s;
		color = col;
		tileMap = new TileMap(width, height, size, tileSet);
		coords = new boolean[width][height];

		spawnPoint = new int[]{tileMap.tiles.length/2*size, tileMap.tiles[0].length/2*size+playerHeight-size};
		tileMap.tiles[(int)(spawnPoint[0]/size)][(int)(spawnPoint[1]/size)+1] = 2;
		
		getColliders();
		fillColliders();
		//tileMap.checkTileBorders();
	}

	public void drawTileMap(Graphics g) {
		boolean[][] preN, preS, preE, preW;
		preN = new boolean[tileMap.tiles.length][tileMap.tiles[0].length];
		preS = new boolean[tileMap.tiles.length][tileMap.tiles[0].length];
		preE = new boolean[tileMap.tiles.length][tileMap.tiles[0].length];
		preW = new boolean[tileMap.tiles.length][tileMap.tiles[0].length];
		for (int i = 0; i < tileMap.tiles.length; i++) {
			for (int j = 0; j < tileMap.tiles[0].length; j++) {
				preN[i][j] = tileMap.n[i][j];
				preS[i][j] = tileMap.s[i][j];
				preE[i][j] = tileMap.e[i][j];
				preW[i][j] = tileMap.w[i][j];
			}
		}
		tileMap.checkTileBorders();
		for (int i = 0; i < tileMap.tiles.length; i++) {
			for (int j = 0; j < tileMap.tiles[0].length; j++) {
				if(tileMap.tileSprites != null)
					g.drawImage(tileMap.tileSprites[22], i * size, j * size);
				if(tileMap.tiles[i][j] == 1){
					boolean n = tileMap.n[i][j];
					boolean s = tileMap.s[i][j];
					boolean e = tileMap.e[i][j];
					boolean w = tileMap.w[i][j];
					if(tileMap.tileSprites == null){
						g.drawRect(i * size, j	* size, size, size, true, color);
					}
					if(tileMap.tileSprites != null){
						Random r = new Random();
						if(n && !s && !e && !w){
							g.drawImage(tileMap.tileSprites[12], i*size, j*size);
						}
						if(!n && s && !e && !w){
							g.drawImage(tileMap.tileSprites[8], i*size, j*size);
						}
						if(!n && !s && e && !w){
							g.drawImage(tileMap.tileSprites[9], i*size, j*size);
						}
						if(!n && !s && !e && w){
							g.drawImage(tileMap.tileSprites[11], i*size, j*size);
						}
						if(n && s && !e && !w){
							if(preN[i][j] != n || preS[i][j] != s || preE[i][j] != e || preW[i][j] != w){
								tileMap.sprites[i][j] = r.nextInt(2);
							}
							switch(tileMap.sprites[i][j]){
							case 0:
								g.drawImage(tileMap.tileSprites[16], i*size, j*size);
								break;
							case 1:
								g.drawImage(tileMap.tileSprites[17], i*size, j*size);
								break;
							}
						}
						if(!n && s && e && !w){
							g.drawImage(tileMap.tileSprites[13], i*size, j*size);
						}
						if(!n && !s && e && w){
							if(preN[i][j] != n || preS[i][j] != s || preE[i][j] != e || preW[i][j] != w){
								tileMap.sprites[i][j] = r.nextInt(2);
							}
							switch(tileMap.sprites[i][j]){
							case 0:
								g.drawImage(tileMap.tileSprites[14], i*size, j*size);
								break;
							case 1:
								g.drawImage(tileMap.tileSprites[19], i*size, j*size);
								break;
							}
						}
						if(n && s && e && !w){
							g.drawImage(tileMap.tileSprites[4], i*size, j*size);
						}
						if(!n && s && e && w){
							g.drawImage(tileMap.tileSprites[6], i*size, j*size);
						}
						if(n && !s && e && w){
							g.drawImage(tileMap.tileSprites[1], i*size, j*size);
						}
						if(n && s && !e && w){
							g.drawImage(tileMap.tileSprites[3], i*size, j*size);
						}
						if(n && !s && e && !w){
							g.drawImage(tileMap.tileSprites[18], i*size, j*size);
						}
						if(!n && s && !e && w){
							g.drawImage(tileMap.tileSprites[15], i*size, j*size);
						}
						if(n && !s && !e && w){
							g.drawImage(tileMap.tileSprites[20], i*size, j*size);
						}
						if(!n && !s && !e && !w){
							g.drawImage(tileMap.tileSprites[21], i*size, j*size);
						}
						if(n && s && e && w){
							if(preN[i][j] != n || preS[i][j] != s || preE[i][j] != e || preW[i][j] != w){
								tileMap.sprites[i][j] = r.nextInt(5);
							}
							switch(tileMap.sprites[i][j]){
							case 0:
								g.drawImage(tileMap.tileSprites[0], i*size, j*size);
								break;
							case 1:
								g.drawImage(tileMap.tileSprites[2], i*size, j*size);
								break;
							case 2:
								g.drawImage(tileMap.tileSprites[5], i*size, j*size);
								break;
							case 3:
								g.drawImage(tileMap.tileSprites[7], i*size, j*size);
								break;
							case 4:
								g.drawImage(tileMap.tileSprites[10], i*size, j*size);
								break;
							}
						}
					} else {
						if(n && !s && !e && !w){
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);//west
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//east
						}
						if(!n && s && !e && !w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
						}
						if(!n && !s && e && !w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);
						}
						if(!n && !s && !e && w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
						}
						if(n && s && !e && !w){
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);
						}
						if(!n && s && e && !w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//north
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);//west
						}
						if(!n && !s && e && w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//north
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
						}
						if(n && s && e && !w){
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);//west
						}
						if(!n && s && e && w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//north
						}
						if(n && !s && e && w){
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
						}
						if(n && s && !e && w){
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//east
						}
						if(n && !s && e && !w){
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);//west
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
						}
						if(!n && s && !e && w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//north
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//east
						}
						if(n && !s && !e && w){
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//east
						}
						if(!n && !s && !e && !w){
							g.drawLine(i*size, j*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//north
							g.drawLine(i*size, j*size, i*size, (j+1)*size, Color.LIGHT_GRAY);//west
							g.drawLine(i*size, (j+1)*size, (i+1)*size, (j+1)*size, Color.LIGHT_GRAY);//south
							g.drawLine((i+1)*size, (j+1)*size, (i+1)*size, j*size, Color.LIGHT_GRAY);//east
						}
					}
				} else if(tileMap.tiles[i][j] == 2){
					g.drawEllipse(i*size, j*size, size, size, true, Color.WHITE, 0.5f);
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
