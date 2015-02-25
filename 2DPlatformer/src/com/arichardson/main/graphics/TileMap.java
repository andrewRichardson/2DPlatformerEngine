package com.arichardson.main.graphics;

//import java.util.Arrays;

public class TileMap {

	public int[][] floors;
	
	public int size;

	public TileMap(int width, int height, int s) {
		size = s;

		floors = new int[width / size][height / size];
		
		GenerateWalls wallGen = new GenerateWalls(floors.length,  floors[0].length, 47, 5, 4);
		
		for (int i = 0; i < floors.length; i++) {
			for (int j = 0; j < floors[0].length; j++) {
				floors[i][j] = wallGen.getMap()[i][j];
			}
		}

	}

}
