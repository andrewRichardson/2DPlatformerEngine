package com.arichardson.main;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.arichardson.main.input.InputHandler;

public class Game extends Canvas implements Runnable, MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private int width = 1280;
	private int height = 720;

	private boolean running = false;

	private Thread thread;

	private JFrame frame;

	private int ups = 0;
	private int fps = 0;

	private InputHandler input;

	private Polygon playerRect;
	private int px = 0;
	private int py = 0;
	private int velX = 0;
	private int velY = 0;
	private int speedX = 1;
	private int speedY = 2;
	private int tileSize = 8;
	private int gravity = 1;
	
	private BufferedImage gradient;
	private int[] gradientArray;
	
	private int lightX = width/2;
	private int lightY = height/2;
	private int lightBallSize = 2;
	private int lightFalloff = 75;
	private int lightPasses = 200;
	private int lightStep = 2;
	private int lightStartA = 0;
	private int lightA = 360;
	
	private int lightRed = 255;
	private int lightBlue = 255;
	private int lightGreen = 255;
	
	private int mouseX;
	private int mouseY;
	private boolean mouseIsHovering = false;
	private int tileHovered = 0;
	private double blockDistance = 0;
	private double maxDistance = 70;

	private boolean paused = false;

	private static String title = "2D Platformer with Dynamic Lighting";

	private Level level;

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame(title);
		input = new InputHandler();

		addKeyListener(input);
		addMouseMotionListener(this);
		addMouseListener(this);

		level = new Level(width, height, tileSize, Color.GRAY);

		px = width / 2 - tileSize / 2;
		py = height / 2 - tileSize / 2;
		
		int[] xPoints = {px, px, px+tileSize, px+tileSize*2, px+tileSize*2};
		int[] yPoints = {py, py+tileSize*4-tileSize, py+tileSize*4, py+tileSize*4-tileSize, py};
		
		playerRect = new Polygon(xPoints, yPoints, 5);

		gradient = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		gradientArray = ((DataBufferInt) gradient.getRaster().getDataBuffer()).getData();
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				Color c = new Color((int)(((double)(height-y)/height)*255), (int)(((double)(height-y)/height)*255), (int)(((double)(height-y)/height)*255));
				gradientArray[x + y * width] = c.getRGB();
			}
		}
		
		
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
			int oldx = px;
			int oldy = py;

			if(Math.abs(velY) < 5){
				if (input.up) {
					velY -= speedY;
				}
			}
			/*
			if (input.down) {
				velY += playerSpeed;
			}*/
			
			if(Math.abs(velX) < 4){
				if (input.left) {
					velX -= speedX;
				}
				if (input.right) {
					velX += speedX;
				}	
			}
			else{
				velX += (velX < 0) ? 1 : -1;
			}
			
			px += velX;
			py += velY;
			
			if(velY < 10)
				velY += gravity;

			playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
			playerRect.translate(px, py);

			if (px > width - tileSize || px < 0)
				px = oldx;

			if (py > height - tileSize || py < 0)
				py = oldy;

			for (int i = 0; i < level.ret.length; i++) {
				if (playerRect.intersects(level.ret[i])) {
					px = oldx;
					i = level.ret.length;
					if(velX != 0)
						velX -= speedX/2 * (velX/Math.abs(velX));
				}
			}
			
			playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
			playerRect.translate(px, py);
			
			for (int i = 0; i < level.ret.length; i++) {
				if (playerRect.intersects(level.ret[i])) {
					py = oldy;
					velY = 0;
					i = level.ret.length;
				}
			}
			
			playerRect.translate(-playerRect.getBounds().x, -playerRect.getBounds().y);
			playerRect.translate(px, py);
			
			checkMouseHover();
		}
	}

	@SuppressWarnings("unused")
	private void resetPlayer() {
		px = width / 2 - tileSize / 2;
		py = height / 2 - tileSize / 2;

		int[] xPoints = {px, px, px+tileSize, px+tileSize*2, px+tileSize*2};
		int[] yPoints = {py, py+tileSize*4-tileSize, py+tileSize*4, py+tileSize*4-tileSize, py};
		playerRect.xpoints = xPoints;
		playerRect.ypoints = yPoints;
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
		
		level.drawTileMap(g);
		
		blockDistance = Math.sqrt(((mouseX)-(px+playerRect.getBounds().width/2))*((mouseX)-(px+playerRect.getBounds().width/2)) + ((mouseY)-(py+playerRect.getBounds().height/2))*((mouseY)-(py+playerRect.getBounds().height/2)));
		
		if(mouseIsHovering){
			if(blockDistance <= maxDistance)
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.RED);
			g.drawRect(level.ret[tileHovered].x-1, level.ret[tileHovered].y-1, level.ret[tileHovered].width+2, level.ret[tileHovered].height+2);
		}
		
		g.setColor(Color.WHITE);
		g.fill(playerRect);

		int fontSize = width / 18;

		g.setFont(new Font("Quartz MS", Font.BOLD, fontSize));
		g.setColor(Color.WHITE);

		if (paused) {
		}

		g.dispose();
		bs.show();
	}
	
	public void checkMouseHover(){
		for(int i = 0; i < level.ret.length; i++){
			if(level.ret[i].contains(mouseX, mouseY)){
				mouseIsHovering = true;
				tileHovered = i;
				i = level.ret.length;
			}
			else
				mouseIsHovering = false;
		}
	}
	
	public void tryBreakBlock(){
		int x = level.ret[tileHovered].x/level.ret[tileHovered].width;
		int y = level.ret[tileHovered].y/level.ret[tileHovered].height;
		if(mouseIsHovering && blockDistance <= maxDistance){
			level.tiles.floors[x][y] = 0;
			level.getColliders();
			level.fillColliders();
			System.out.println("Breaking block");
		}
	}
	
	public void tryPlaceBlock(){
		int x = level.ret[tileHovered].x/level.ret[tileHovered].width;
		int y = level.ret[tileHovered].y/level.ret[tileHovered].height;
		if(blockDistance <= maxDistance){
			level.tiles.floors[x][y] = 1;
			level.getColliders();
			level.fillColliders();
			System.out.println("Placing block");
		}
	}

	public void lightScene(Graphics2D g) {
		for(int l = 0; l < lightPasses; l++){
			for(int r = 1; r < lightFalloff+1; r++){
				int x = (int)(lightX+lightStep*(r*Math.cos(Math.toRadians(((double)l/lightPasses)*lightA + lightStartA))));
				int y = (int)(lightY+lightStep*(r*Math.sin(Math.toRadians(((double)l/lightPasses)*lightA + lightStartA))));
				
				boolean collide = false;
				
				if(level.coords[x][y] || playerRect.intersects(x-lightBallSize/2, y-lightBallSize/2, lightBallSize, lightBallSize)){
					r = lightFalloff+1;
					collide = true;
				}
				
				if(!collide){
					g.setColor(new Color(lightRed-(int)(((double)r/lightFalloff)*lightRed),lightBlue-(int)(((double)r/lightFalloff)*lightBlue),lightGreen-(int)(((double)r/lightFalloff)*lightGreen)));
					g.fillOval(x, y, lightBallSize+(r/(lightFalloff/5)), lightBallSize+(r/(lightFalloff/5)));
				}
			}
		}
	}
	
	public void renderLight(Graphics2D g, int xx, int yy, int size, float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		for(int l = 0; l < lightPasses; l++){
			for(int r = 1; r < size+1; r++){
				int x = (int)(xx+lightStep*(r*Math.cos(Math.toRadians(((double)l/lightPasses)*360))));
				int y = (int)(yy+lightStep*(r*Math.sin(Math.toRadians(((double)l/lightPasses)*360))));
				
				g.setColor(new Color(lightRed-(int)(((double)r/size)*lightRed),lightBlue-(int)(((double)r/size)*lightBlue),lightGreen-(int)(((double)r/size)*lightGreen)));
				g.fillOval(x, y, size, size);
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
	}
	
	public void mouseDragged(MouseEvent e){
		lightX = e.getX();
		lightY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			tryBreakBlock();
		if(e.getButton() == MouseEvent.BUTTON3)
			tryPlaceBlock();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
