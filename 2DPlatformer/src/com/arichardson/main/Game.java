package com.arichardson.main;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.arichardson.main.entities.Player;
import com.arichardson.main.graphics.Drawing;
import com.arichardson.main.graphics.Lighting;
import com.arichardson.main.graphics.ui.UIComponent;
import com.arichardson.main.graphics.ui.UIController;
import com.arichardson.main.graphics.ui.UILabel;
import com.arichardson.main.graphics.ui.UIMenu;
import com.arichardson.main.input.InputHandler;

public class Game extends Canvas implements Runnable, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private int width = 1280;
	private int height = 720;

	private boolean running = false;

	private Thread thread;

	private JFrame frame;

	@SuppressWarnings("unused")
	private int ups = 0;
	private int fps = 0;
	
	private int tileSize = 20;
	
	private int mouseX;
	private int mouseY;

	private boolean paused = false;

	private static String title = "2D Platformer Engine - Level Editor";
	private boolean showFPS = true;

	private InputHandler input;
	private Drawing drawer;
	private Player player;
	private Lighting lighting;
	private UIController uiControl;
	
	UIComponent[] brushMenuComponents;
	UIComponent brushButton;
	UIMenu brushMenu;
	UIComponent[] mainMenuComponents;
	UIMenu mainMenu;
	
	private String tileSet = "res/underground-tileSet.png";

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame(title);
		input = new InputHandler();

		addKeyListener(input);
		addMouseListener(input);
		addMouseWheelListener(input);
		addMouseMotionListener(this);

		drawer = new Drawing(width, height, tileSize, Color.GRAY, input, tileSet);
		player = new Player(drawer.level, input, 1, width, height);
		drawer.playerwidth = player.playerRect.getBounds().width;
		drawer.playerheight = player.playerRect.getBounds().height;
		lighting = new Lighting(drawer.level, player, width, height);
		uiControl = new UIController(width, height, input);
		brushMenuComponents = new UIComponent[]{new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "DESTROY BLOCK"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "PLACE BLOCK"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "PLACE SPAWN"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE MENU")};
		brushButton = new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH TYPE");
		
		brushMenu = new UIMenu(0, 0, UIMenu.VERT_LAYOUT, brushMenuComponents, new int[]{5, 5, 150, 50}, true, true, Color.BLACK, false);
		
		mainMenuComponents = new UIComponent[]{new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "SAVE MAP"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "OPEN"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH SHAPE"), 
				brushButton, 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "RESET PLAYER"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE")};
		mainMenu = new UIMenu(10, 10, UIMenu.HORZ_LAYOUT, mainMenuComponents, new int[]{5, 5, 600, 60}, false, true, Color.BLACK, true);
		uiControl.addMenu(mainMenu);
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.toFront();
		game.frame.setState(JFrame.NORMAL);
		game.frame.requestFocus();

		game.frame.setVisible(true);
		game.start();

	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long oldTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		double ns = 1000000000.0 / 60.0;
		long newTime;
		double delta = 0;

		while (running) {
			newTime = System.nanoTime();
			delta += (double) (newTime - oldTime) / ns;
			oldTime = newTime;
			if (delta >= 1) {
				update();
				delta--;
				ups++;
			}
			render();
			fps++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				if(showFPS)
					frame.setTitle(title + " | " + fps + " fps");
				else
					frame.setTitle(title);
				//System.out.println(ups + " ups, " + fps + " fps");
				fps = 0;
				ups = 0;
			}
		}
		stop();
	}

	private void update() {
		input.update();

		if (!paused) {
			if(!input.escape)
				player.update();
			handleUI();
			drawer.update();
			drawer.mouseX = mouseX;
			drawer.mouseY = mouseY;
			uiControl.mouseX = mouseX;
			uiControl.mouseY = mouseY;
			uiControl.eventHandler();
			drawer.playerX = player.px;
			drawer.playerY = player.py;
		}
	}
	
	private void handleUI(){
		boolean flag = false;
		if(brushMenuComponents[0].clicked){
			drawer.changeBrushType(0);
		}
		if(brushMenuComponents[1].clicked){
			drawer.changeBrushType(1);
		}
		if(brushMenuComponents[2].clicked)
			drawer.changeBrushType(2);
		if(brushMenuComponents[3].clicked && mainMenuComponents[3].equals(brushMenu)){
			mainMenuComponents[3] = brushButton;
			mainMenu.autoPlaceComponents();
			flag = true;
		}
		if(mainMenuComponents[3].clicked && mainMenuComponents[3].equals(brushButton) && !flag){
			mainMenuComponents[3] = brushMenu;
			mainMenu.autoPlaceComponents();
		}
		if(mainMenuComponents[4].clicked)
			player.resetPlayer();
		if(mainMenuComponents[5].clicked)
			input.escape = false;
		if(mainMenuComponents[2].clicked)
			input.ctrl ^= true;
		if(mainMenuComponents[1].clicked){
			String file = JOptionPane.showInputDialog(frame, "Type the name of the level (w/o extension)", "Open Level", JOptionPane.QUESTION_MESSAGE);
			if(file != null)
				retrieveLevel(file);
			input.mouseLeft = false;
		}
		if(mainMenuComponents[0].clicked){
			String file = JOptionPane.showInputDialog(frame, "Type the name of the new level (w/o extension)", "Save Level", JOptionPane.QUESTION_MESSAGE);
			if(file != null)
				saveLevel(file, drawer.level);
			input.mouseLeft = false;
		}
		
		for(UIComponent comp : brushMenuComponents)
			comp.clicked = false;
		for(UIComponent comp : mainMenuComponents)
			comp.clicked = false;
	}
	
	private void retrieveLevel(String levelName){
		int width = 0;
		int height = 0;
		int tileSize = 0;
		int spawnPoint[] = new int[2];
		int r, g, b;
		int[][] newLevel = null;
		Color color = null;
		try {
			//TODO: Change to use "Scanner" instead of BufferedReader
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(newLevel != null){
			drawer.level = new Level(width, height, tileSize, color, tileSet);
			drawer.level.spawnPoint[0] = spawnPoint[0];
			drawer.level.spawnPoint[1] = spawnPoint[1];
			for(int y = 0; y < height/tileSize; y++){
				for(int x = 0; x < width/tileSize; x++){
					drawer.level.tileMap.tiles[x][y] = newLevel[x][y];
				}
			}
			drawer.level.getColliders();
			drawer.level.fillColliders();
			player = new Player(drawer.level, input, 1, width, height);
		}
		else
			JOptionPane.showMessageDialog(frame, "Level does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void saveLevel(String levelName, Level level) {
		paused = true;
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
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + levelName + "'");
        }
		paused = false;
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		//lightScene(g);

		//renderLight(g);
		
		drawer.render(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		g.setColor(Color.WHITE);
		//g.fill(player.playerRect);
		g.fillRect(player.px, player.py, drawer.level.size, drawer.level.size*3);
		
		uiControl.render(g);

		int fontSize = width / 16;

		g.setFont(new Font("HELVETICA", Font.BOLD, fontSize));
		g.setColor(Color.WHITE);

		if (paused) {
		}

		g.dispose();
		bs.show();
	}
	
	public void mouseDragged(MouseEvent e){
		lighting.lightX = e.getX();
		lighting.lightY = e.getY();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
