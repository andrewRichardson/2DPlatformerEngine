package com.arichardson.main.api;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.arichardson.main.GameInit;
import com.arichardson.main.Level;

public class LevelLoader {
	/**
	 * Loads a Level object from the specified sub-folder. 
	 * The levelPath argument must specify a relative path of type {@link String}.
	 * <p>
	 * This method returns a Level object when finished loading.
	 *
	 * @param  levelPath  an relative String path of the level to be loaded
	 * @param  init the GameInit object used for its JFrame
	 * @return      Level
	 * @see         Level
	 */
	public static Level retrieveLevel(String levelPath, GameInit init){
		int width = 0;
		int height = 0;
		int tileSize = 0;
		int spawnPoint[] = new int[2];
		int r, g, b;
		int[][] newLevel = null;
		Color color = null;
		Level level;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(levelPath)));
			
			if(br != null){
				width = Integer.parseInt(br.readLine());
				height = Integer.parseInt(br.readLine());
				tileSize = Integer.parseInt(br.readLine());
				spawnPoint[0] = Integer.parseInt(br.readLine());
				spawnPoint[1] = Integer.parseInt(br.readLine());
				r = Integer.parseInt(br.readLine());
				g = Integer.parseInt(br.readLine());
				b = Integer.parseInt(br.readLine());
				color = new Color(r, g, b);
				
				newLevel = new int[width][height];
				
				for(int y = 0; y < height/tileSize; y++){
					String line = br.readLine();
					for(int x = 0; x < width/tileSize; x++){
						newLevel[x][y] = Integer.parseInt(line.charAt(x*2)+"");
						System.out.print(newLevel[x][y] + " ");
					}
					System.out.println("");
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("false");
		if(newLevel == null){
			JOptionPane.showMessageDialog(init.frame, "Level does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			 return null;
		}else{

			level = new Level(width, height, tileSize, color, 0);
			level.spawnPoint[0] = spawnPoint[0];
			level.spawnPoint[1] = spawnPoint[1];
			for(int y = 0; y < height/tileSize; y++){
				for(int x = 0; x < width/tileSize; x++){
					level.tileMap.tiles[x][y] = newLevel[x][y];
				}
			}
			level.getColliders();
			level.fillColliders();
		}
		return level;
	}
	
	/**
	 * Saves a Level object in the specific sub-folder. 
	 * The levelName argument must specify a relative path of type {@link String}.
	 * <p>
	 * This method returns when finished saving.
	 *
	 * @param  levelPath  an relative String path of the level to be saved
	 * @param  level the level object to be saved
	 * @return      void
	 * @see         Level
	 */
	public static void saveLevel(String levelPath, Level level) {
		try {
            FileWriter fileWriter = new FileWriter(levelPath);

            BufferedWriter bw = new BufferedWriter(fileWriter);

            bw.write(level.width+"");
            bw.newLine();
            bw.write(level.height+"");
            bw.newLine();
            bw.write(level.size+"");
            bw.newLine();
            bw.write(level.spawnPoint[0]+"");
            bw.newLine();
            bw.write(level.spawnPoint[1]+"");
            bw.newLine();
            bw.write(level.color.getRed()+"");
            bw.newLine();
            bw.write(level.color.getGreen()+"");
            bw.newLine();
            bw.write(level.color.getBlue()+"");
            bw.newLine();
            
            for(int y = 0; y < level.height/level.size; y++){
    			for(int x = 0; x < level.width/level.size; x++){
    				bw.write(level.tileMap.tiles[x][y] + " ");
    			}
    			bw.newLine();
    		}
            
            bw.close();
            fileWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + levelPath + "'");
        }
	}
	
}
