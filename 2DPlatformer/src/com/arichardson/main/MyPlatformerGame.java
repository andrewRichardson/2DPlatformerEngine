package com.arichardson.main;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.arichardson.main.api.Graphics;
import com.arichardson.main.entities.Player;
import com.arichardson.main.graphics.Drawing;
import com.arichardson.main.graphics.ui.UIComponent;
import com.arichardson.main.graphics.ui.UIController;
import com.arichardson.main.graphics.ui.UILabel;
import com.arichardson.main.graphics.ui.UIMenu;
import com.arichardson.main.graphics.ui.UISlider;
import com.arichardson.main.input.InputHandler;

public class MyPlatformerGame {
	
	private static int width = 1280;
	private static int height = 720;
	private int mouseX;
	private int mouseY;
	private boolean paused = false;
	private static GameInit init;	
	
	
	private int tileSize = 20;
	private int brushMaxRadius = 25;
	private int PM_OffsetX, PM_OffsetY;

	private boolean movePlayer = false;
	private boolean movingPlayer = false;
	
	private String tileSet = "res/underground-tileSet.png";

	private Graphics graphics;
	private static InputHandler input;
	private Drawing drawer;
	private Player player;
	private UIController uiControl;
	private UIComponent[] brushMenuComponents;
	private UIComponent brushButton;
	private UIMenu brushMenu;
	private UIComponent[] fileMenuComponents;
	private UIComponent fileButton;
	private UIMenu fileMenu;
	private UIComponent[] mainMenuComponents;
	private UIMenu mainMenu;
	
	private Image moveSprite;
	
	public MyPlatformerGame() {		
		graphics = new Graphics(init, width, height);
		input = new InputHandler();
		
		init.addKeyListener(input);
		init.addMouseListener(input);
		init.addMouseWheelListener(input);
		
		BufferedImage wholeTileSheet = graphics.loadImage("res/tool-spriteSheet.png");
		moveSprite = wholeTileSheet.getSubimage(0, 0, wholeTileSheet.getWidth()/5, wholeTileSheet.getHeight()/5).getScaledInstance(wholeTileSheet.getWidth()/5, wholeTileSheet.getHeight()/5, BufferedImage.TYPE_INT_ARGB);

		drawer = new Drawing(width, height, tileSize, Color.GRAY, input, tileSet);
		player = new Player(drawer.level, input, 1, width, height, tileSize, tileSize*2);
		drawer.playerwidth = player.playerRect.getBounds().width;
		drawer.playerheight = player.playerRect.getBounds().height;
		uiControl = new UIController(width, height, input);
		brushMenuComponents = new UIComponent[]{new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "DESTROY BLOCK"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "PLACE BLOCK"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "PLACE SPAWN"),
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "v BRUSH RADIUS v"),
				new UISlider(0, 0, 150, 50, false, Color.LIGHT_GRAY, 0.8f, 1), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE")
		};
		brushButton = new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH");
		
		brushMenu = new UIMenu(0, 0, UIMenu.VERT_LAYOUT, brushMenuComponents, new int[]{5, 5, 150, 50}, true, true, Color.BLACK, false);
		
		fileMenuComponents = new UIComponent[]{
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "SAVE MAP"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "OPEN"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "NEW"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE")
		};
		fileButton = new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "FILE");
		
		fileMenu = new UIMenu(0, 0, UIMenu.VERT_LAYOUT, fileMenuComponents, new int[]{5, 5, 150, 50}, true, true, Color.BLACK, false);
		
		mainMenuComponents = new UIComponent[]{
				fileButton, 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH SHAPE"), 
				brushButton, 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "RESET PLAYER"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "MOVE PLAYER"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE")
		};
		mainMenu = new UIMenu(10, 10, UIMenu.HORZ_LAYOUT, mainMenuComponents, new int[]{5, 5, 600, 60}, false, true, Color.BLACK, true);
		uiControl.addMenu(mainMenu);
		
		render();
	}
	
	public static void main(String[] args) {
		init = new GameInit("2D Platformer Engine - Level Editor", width, height);
		init.main();
		@SuppressWarnings("unused")
		MyPlatformerGame game = new MyPlatformerGame();
	}
	
	public void update(){
		mouseX = init.getMouseX();
		mouseY = init.getMouseY();
		
		input.update();

		if (!paused) {
			if(!input.escape)
				player.update();
			drawer.playerheight = player.pHeight;
			if(!movePlayer){
				drawer.update();
				drawer.mouseX = mouseX;
				drawer.mouseY = mouseY;
			} else {
				drawer.mouseX = -1000;
				drawer.mouseY = -1000;
			}
			uiControl.mouseX = mouseX;
			uiControl.mouseY = mouseY;
			if(!movingPlayer)
				uiControl.eventHandler();
			drawer.playerX = player.px;
			drawer.playerY = player.py;
			if(movePlayer){
				if(input.mouseLeft && (player.playerTopCollider.contains(mouseX, mouseY) || player.playerBottomCollider.contains(mouseX, mouseY)) && !movingPlayer){
					movingPlayer = true;
					PM_OffsetX = mouseX-player.px;
					PM_OffsetY = mouseY-player.py;
				}
				if(movingPlayer){
					player.px = mouseX-PM_OffsetX;
					player.py = mouseY-PM_OffsetY;
					player.playerTopCollider.x = player.px-2;
					player.playerTopCollider.y = player.py;
					player.playerBottomCollider.x = player.px;
					player.playerBottomCollider.y = player.py+player.pHeight*2/3;
					player.stopMovement();
				}
				if(!input.mouseLeft)
					movingPlayer = false;
			} else{
				movingPlayer = false;
			}
			handleUI();
		}
	}
	
	public void render(){
		int updates = 0;
		while(true){
			boolean flag = false;
			if(init.render){
				flag = graphics.startRender();
				
				if(!flag){
					if(init.ups > updates){
						updates++;
						update();
					}
					if(init.ups < updates){
						updates = 0;
					}
					drawer.render(graphics);
					
					player.render(graphics);
					
					if(movePlayer){
						graphics.drawImage(moveSprite, mouseX-15, mouseY-15);
					}
					
					graphics.drawUI(uiControl);
	
					if (paused) {
					}
	
					graphics.finishRender();
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void handleUI(){
		boolean flag = false;
		boolean clicked = true;
		
		if(brushMenuComponents[0].clicked){
			drawer.changeBrushType(0);
		}
		else if(brushMenuComponents[1].clicked){
			drawer.changeBrushType(1);
		}
		else if(brushMenuComponents[2].clicked) {
			drawer.changeBrushType(2);
		}
		else if(brushMenuComponents[4].clicked){
			UISlider slider = (UISlider)brushMenuComponents[4];
			float value = (float)(mouseX-brushMenuComponents[4].x)/brushMenuComponents[4].width;
			slider.value = (value < 1?value:1)>0?value:0;
			drawer.scrollNumber = (int)(slider.value*brushMaxRadius)*2;
			clicked = false;
		}
		else if(brushMenuComponents[5].clicked && mainMenuComponents[2].equals(brushMenu)){
			mainMenuComponents[2] = brushButton;
			mainMenu.autoPlaceComponents();
			flag = true;
		}
		else if(mainMenuComponents[2].clicked && mainMenuComponents[2].equals(brushButton) && !flag){
			mainMenuComponents[2] = brushMenu;
			mainMenu.autoPlaceComponents();
		}
		else if(fileMenuComponents[3].clicked && mainMenuComponents[0].equals(fileMenu)){
			mainMenuComponents[0] = fileButton;
			mainMenu.autoPlaceComponents();
			flag = true;
		}
		else if(mainMenuComponents[0].clicked && mainMenuComponents[0].equals(fileButton) && !flag){
			mainMenuComponents[0] = fileMenu;
			mainMenu.autoPlaceComponents();
		}
		else if(fileMenuComponents[2].clicked){
			drawer.level = new Level(width, height, tileSize, Color.GRAY, tileSet, player.pHeight);
			drawer.level.getColliders();
			drawer.level.fillColliders();
			player = new Player(drawer.level, input, 1, width, height, tileSize, tileSize*2);
		}
		else if(mainMenuComponents[3].clicked){
			player.resetPlayer();
		}
		else if(mainMenuComponents[4].clicked){
			movePlayer ^= true;
		}
		else if(mainMenuComponents[5].clicked){
			input.escape = false;
		}
		else if(mainMenuComponents[1].clicked){
			input.ctrl ^= true;
		}
		else if(fileMenuComponents[1].clicked){
			String file = JOptionPane.showInputDialog(init.frame, "Type the name of the level (w/o extension)", "Open Level", JOptionPane.QUESTION_MESSAGE);
			if(file != null)
				retrieveLevel(file);
			input.mouse[MouseEvent.BUTTON1] = false;
		}
		else if(fileMenuComponents[0].clicked){
			String file = JOptionPane.showInputDialog(init.frame, "Type the name of the new level (w/o extension)", "Save Level", JOptionPane.QUESTION_MESSAGE);
			if(file != null)
				saveLevel(file, drawer.level);
			input.mouse[MouseEvent.BUTTON1] = false;
		}
		else {
			float newValue = (float)drawer.brushRadius/brushMaxRadius;
			((UISlider)brushMenuComponents[4]).value = (newValue < 1 ? 1 : newValue) > 0 ? newValue : 0;
			clicked = false;
		}
		
		if(clicked){
			try {
				init.thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				clicked = true;
			}
		}
		
		for(UIComponent comp : brushMenuComponents)
			comp.clicked = false;
		for(UIComponent comp : fileMenuComponents)
			comp.clicked = false;
		for(UIComponent comp : mainMenuComponents)
			comp.clicked = false;
	}
	
	// --------------
	// TODO: Create API for handling level data, remove from client class
	// --------------
	private void retrieveLevel(String levelName){
		int width = 0;
		int height = 0;
		int tileSize = 0;
		int spawnPoint[] = new int[2];
		int r, g, b;
		int[][] newLevel = null;
		Color color = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("res/"+levelName+".txt")));
			
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
		if(newLevel != null){
			drawer.level = new Level(width, height, tileSize, color, tileSet, player.pHeight);
			drawer.level.spawnPoint[0] = spawnPoint[0];
			drawer.level.spawnPoint[1] = spawnPoint[1];
			for(int y = 0; y < height/tileSize; y++){
				for(int x = 0; x < width/tileSize; x++){
					drawer.level.tileMap.tiles[x][y] = newLevel[x][y];
				}
			}
			drawer.level.getColliders();
			drawer.level.fillColliders();
			player = new Player(drawer.level, input, 1, width, height, tileSize, tileSize*2);
		}
		else
			JOptionPane.showMessageDialog(init.frame, "Level does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
		
		input.mouse[MouseEvent.BUTTON1] = false;
	}
	
	private void saveLevel(String levelName, Level level) {
		try {
            FileWriter fileWriter = new FileWriter("res/"+levelName+".txt");

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
            
            for(int y = 0; y < height/level.size; y++){
    			for(int x = 0; x < width/level.size; x++){
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
                + levelName + "'");
        }
		input.mouse[MouseEvent.BUTTON1] = false;
	}

}
