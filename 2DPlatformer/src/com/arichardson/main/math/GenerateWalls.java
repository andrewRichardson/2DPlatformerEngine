package com.arichardson.main.math;

import java.util.Random;

public class GenerateWalls {
	private int width, height;

	private int randomFillPercent = 47;

	private int[][] map;
	
	private int iterations = 5;
	private int wallCount = 4;
	private int safeZone = 5;

	public GenerateWalls(int width, int height, int rfp, int iter, int wallC){
		this.width = width;
		this.height = height;
		randomFillPercent = rfp;
		iterations = iter;
		wallCount = wallC;
		GenerateMap ();
	}

	private void GenerateMap() {
		map = new int[width][height];
		RandomFillMap ();

		for (int i = 0; i < iterations; i++) {
			SmoothMap();	
		}
	}

	private void RandomFillMap() {

		Random rand = new Random();

		for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(x == 0 || x == width-1 || y == 0 || y == height-1){
					map[x][y] = 1;
				} else if(x >= (width/2)-safeZone && x <= (width/2)+safeZone && y >= (height/2)-safeZone && y <= (height/2)+safeZone){
					map[x][y] = 0;
				} else{
					map[x][y] = (rand.nextInt(100) < randomFillPercent) ? 1 : 0;
				}
			}
		}
	}

	private void SmoothMap() {
		for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int neighborWallTiles = GetSurroundingWallCount(x,y);

				if(neighborWallTiles > wallCount)
					map[x][y] = 1;
				else if(neighborWallTiles < wallCount)
					map[x][y] = 0;
			}
		}
	}

	private int GetSurroundingWallCount(int gridX, int gridY) {
		int wallCount = 0;

		for (int neighborX = gridX-1; neighborX <= gridX+1; neighborX++) {
			for (int neighborY = gridY-1; neighborY <= gridY+1; neighborY++) {
				if(neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height){
					if(neighborX != gridX || neighborY != gridY){
						wallCount += map[neighborX][neighborY];
					}	
				}
				else{
					wallCount++;
				}
			}	
		}

		return wallCount;
	}
	
	public int[][] getMap(){
		return map;
	}
}
