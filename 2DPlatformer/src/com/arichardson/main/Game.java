package com.arichardson.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

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
	
	private int tileSize = 8;
	
	private int mouseX;
	private int mouseY;

	private boolean paused = false;

	private static String title = "2D Platformer Engine";

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

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame(title);
		input = new InputHandler();

		addKeyListener(input);
		addMouseListener(input);
		addMouseWheelListener(input);
		addMouseMotionListener(this);

		drawer = new Drawing(width, height, tileSize, Color.GRAY, input);
		player = new Player(drawer.level, input, 1, width, height);
		drawer.playerwidth = player.playerRect.getBounds().width;
		drawer.playerheight = player.playerRect.getBounds().height;
		lighting = new Lighting(drawer.level, player, width, height);
		uiControl = new UIController(width, height, input);
		brushMenuComponents = new UIComponent[]{new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "DESTROY"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "PLACE"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE MENU")};
		brushButton = new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH TYPE");
		
		brushMenu = new UIMenu(0, 0, UIMenu.VERT_LAYOUT, brushMenuComponents, new int[]{5, 5, 150, 50}, true, true, Color.BLACK, false);
		
		mainMenuComponents = new UIComponent[]{new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "SAVE MAP"), 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "BRUSH SHAPE"), 
				brushButton, 
				new UILabel(0, 0, 150, 50, false, Color.LIGHT_GRAY, Color.WHITE, 0.8f, "CLOSE")};
		mainMenu = new UIMenu(10, 10, UIMenu.HORZ_LAYOUT, mainMenuComponents, new int[]{5, 5, 600, 60}, false, true, Color.BLACK, true);
		uiControl.addMenu(mainMenu);
	}

	public static void main(String[] args) {
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
				frame.setTitle(title + " | " + fps + " fps");
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
			drawer.changeBrushType(false);
		}
		if(brushMenuComponents[1].clicked){
			drawer.changeBrushType(true);
		}
		if(brushMenuComponents[2].clicked && mainMenuComponents[2].equals(brushMenu)){
			mainMenuComponents[2] = brushButton;
			mainMenu.autoPlaceComponents();
			flag = true;
		}
		if(mainMenuComponents[2].clicked && mainMenuComponents[2].equals(brushButton) && !flag){
			mainMenuComponents[2] = brushMenu;
			mainMenu.autoPlaceComponents();
		}
		if(mainMenuComponents[3].clicked)
			input.shiftTab = false;
		if(mainMenuComponents[1].clicked)
			input.ctrl ^= true;
		
		for(UIComponent comp : brushMenuComponents)
			comp.clicked = false;
		for(UIComponent comp : mainMenuComponents)
			comp.clicked = false;
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
		
		g.setColor(Color.WHITE);
		g.fill(player.playerRect);
		
		drawer.render(g);
		
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
