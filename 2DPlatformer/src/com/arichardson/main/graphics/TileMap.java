package com.arichardson.main.graphics;

import com.arichardson.main.math.GenerateWalls;

//import java.util.Arrays;

public class TileMap {

	public int[][] tiles;
	
	public int size;

	public TileMap(int width, int height, int s) {
		size = s;

		tiles = new int[width / size][height / size];
		
		GenerateWalls wallGen = new GenerateWalls(tiles.length,  tiles[0].length, 47, 5, 4);
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = wallGen.getMap()[i][j];
			}
		}

	}

}
