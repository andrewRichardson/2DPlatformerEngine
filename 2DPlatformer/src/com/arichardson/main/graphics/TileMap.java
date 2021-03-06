package com.arichardson.main.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.arichardson.main.math.GenerateWalls;

//import java.util.Arrays;

public class TileMap {

	public int[][] tiles;
	public int[][] sprites;
	public boolean[][] n, s, e, w;
	private int width, height;
	
	public int size;
	private String fileName;
	public Image[] tileSprites;

	public TileMap(int width, int height, int s) {
		size = s;

		this.width = width;
		this.height = height;
		
		tiles = new int[width / size][height / size];
		w = new boolean[width / size][height / size];
		this.s = new boolean[width / size][height / size];
		e = new boolean[width / size][height / size];
		n = new boolean[width / size][height / size];
		sprites = new int[width / size][height / size];
		
		GenerateWalls wallGen = new GenerateWalls(tiles.length,  tiles[0].length, 47, 5, 4);
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = wallGen.getMap()[i][j];
			}
		}

	}
	
	public TileMap(int width, int height, int s, String filePath) {
		size = s;
		fileName = filePath;
		tileSprites = new Image[23];


		this.width = width;
		this.height = height;
		
		tiles = new int[width / size][height / size];
		w = new boolean[width / size][height / size];
		this.s = new boolean[width / size][height / size];
		e = new boolean[width / size][height / size];
		n = new boolean[width / size][height / size];
		sprites = new int[width / size][height / size];
		
		GenerateWalls wallGen = new GenerateWalls(tiles.length,  tiles[0].length, 47, 5, 4);
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = wallGen.getMap()[i][j];
			}
		}
		
		getTileSheet();
	}
	
	public void getTileSheet() {
		BufferedImage wholeTileSheet = null;
		try {
			File file = new File(fileName);
			wholeTileSheet = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int actualSize = wholeTileSheet.getHeight()/6;
		
		if(wholeTileSheet != null){
			tileSprites[0] = wholeTileSheet.getSubimage(0*actualSize, 0*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[1] = wholeTileSheet.getSubimage(1*actualSize, 0*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[2] = wholeTileSheet.getSubimage(2*actualSize, 2*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[3] = wholeTileSheet.getSubimage(0*actualSize, 1*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[4] = wholeTileSheet.getSubimage(2*actualSize, 1*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[5] = wholeTileSheet.getSubimage(0*actualSize, 2*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[6] = wholeTileSheet.getSubimage(1*actualSize, 2*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[7] = wholeTileSheet.getSubimage(2*actualSize, 2*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[8] = wholeTileSheet.getSubimage(4*actualSize, 0*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[9] = wholeTileSheet.getSubimage(3*actualSize, 1*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[10] = wholeTileSheet.getSubimage(4*actualSize, 1*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[11] = wholeTileSheet.getSubimage(5*actualSize, 1*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[12] = wholeTileSheet.getSubimage(4*actualSize, 2*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[13] = wholeTileSheet.getSubimage(0*actualSize, 3*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[14] = wholeTileSheet.getSubimage(1*actualSize, 3*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[15] = wholeTileSheet.getSubimage(2*actualSize, 3*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[16] = wholeTileSheet.getSubimage(0*actualSize, 4*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[17] = wholeTileSheet.getSubimage(2*actualSize, 4*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[18] = wholeTileSheet.getSubimage(0*actualSize, 5*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[19] = wholeTileSheet.getSubimage(1*actualSize, 5*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[20] = wholeTileSheet.getSubimage(2*actualSize, 5*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[21] = wholeTileSheet.getSubimage(3*actualSize, 3*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
			tileSprites[22] = wholeTileSheet.getSubimage(4*actualSize, 3*actualSize, actualSize, actualSize).getScaledInstance(size, size, BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	public void checkTileBorders(){

		w = new boolean[width / size][height / size];
		s = new boolean[width / size][height / size];
		e = new boolean[width / size][height / size];
		n = new boolean[width / size][height / size];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if(i != 0){
					if(tiles[i-1][j] == 1)
						w[i][j] = true;
				}
				if(j != 0){
					if(tiles[i][j-1] == 1)
						n[i][j] = true;
				}
				if(j != tiles[0].length-1){
					if(tiles[i][j+1] == 1)
						s[i][j] = true;
				}
				if(i != tiles.length-1){
					if(tiles[i+1][j] == 1)
						e[i][j] = true;
				}
			}
		}
	}

}
