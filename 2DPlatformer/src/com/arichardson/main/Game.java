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
import com.arichardson.main.graphics.ui.UIMenu;
import com.arichardson.main.graphics.ui.UIPanel;
import com.arichardson.main.input.InputHandler;

public class Game extends Canvas implements Runnable, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private int width = 1280;
	private int height = 720;

	private boolean running = false;

	private Thread thread;

	private JFrame frame;

	private int ups = 0;
	private int fps = 0;
	
	private int tileSize = 8;
	
	private int mouseX;
	private int mouseY;

	private boolean paused = false;

	private static String title = "2D Platformer Engine";

	private InputHandler input;
	private Level level;
	private Drawing drawer;
	private Player player;
	private Lighting lighting;
	private UIController uiControl;

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame(title);
		input = new InputHandler();

		addKeyListener(input);
		addMouseListener(input);
		addMouseWheelListener(input);
		addMouseMotionListener(this);

		level = new Level(width, height, tileSize, Color.GRAY);
		drawer = new Drawing(level, input);
		player = new Player(level, input, 1, width, height);
		lighting = new Lighting(level, player, width, height);
		uiControl = new UIController(width, height, input);
		UIComponent[] components = {new UIPanel(0, 0, 0, 0, false, Color.LIGHT_GRAY, 0.8f), 
				new UIPanel(0, 0, 0, 0, false, Color.LIGHT_GRAY, 0.8f), 
				new UIPanel(0, 0, 0, 0, false, Color.LIGHT_GRAY, 0.8f), 
				new UIPanel(0, 0, 0, 0, false, Color.LIGHT_GRAY, 0.8f)};
		
		UIMenu menu = new UIMenu(0, 0, UIMenu.VERT_LAYOUT, components, new int[]{0, 10, 150, 50}, true, false, Color.WHITE, false);
		
		UIComponent[] components2 = {new UIPanel(0, 0, 150, 50, false, Color.LIGHT_GRAY, 0.8f), 
				new UIPanel(0, 0, 150, 50, false, Color.LIGHT_GRAY, 0.8f), 
				menu, 
				new UIPanel(0, 0, 150, 50, false, Color.LIGHT_GRAY, 0.8f)};
		UIMenu menu2 = new UIMenu(10, 10, UIMenu.HORZ_LAYOUT, components2, new int[]{10, 10, 0, 0}, false, false, Color.WHITE, true);
		uiControl.addMenu(menu2);
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
				System.out.println(ups + " ups, " + fps + " fps");
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
			
			drawer.update();
			drawer.mouseX = mouseX;
			drawer.mouseY = mouseY;
			
			drawer.blockDistance = Math.sqrt(((mouseX)-(player.px+player.playerRect.getBounds().width/2))*((mouseX)-(player.px+player.playerRect.getBounds().width/2)) + ((mouseY)-(player.py+player.playerRect.getBounds().height/2))*((mouseY)-(player.py+player.playerRect.getBounds().height/2)));
		}
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

		int fontSize = width / 18;

		g.setFont(new Font("Quartz MS", Font.BOLD, fontSize));
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
