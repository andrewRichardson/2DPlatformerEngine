package com.arichardson.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import com.arichardson.main.input.InputHandler;

public class GameInit extends Canvas implements Runnable, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;

	//@SuppressWarnings("unused")
	public int ups = 0;
	private int fps = 0;
	private int mouseX;
	private int mouseY;
	private boolean running = false;
	private boolean showFPS = false;
	public Thread thread;
	public JFrame frame;
	@SuppressWarnings("unused")
	private static InputHandler input;
	private static String title;
	
	public boolean render;

	public GameInit(String title, int width, int height) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		GameInit.title = title;
		frame = new JFrame(title);

		addMouseMotionListener(this);
	}

	public void main() {
		System.setProperty("sun.java2d.opengl", "true");
		
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.toFront();
		frame.setState(JFrame.NORMAL);
		frame.requestFocus();

		frame.setVisible(true);
		start();

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
				delta--;
				ups++;
			}
			render = true;
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
	
	public int getMouseX(){
		return mouseX;
	}

	public int getMouseY(){
		return mouseY;
	}
	
	public void mouseDragged(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
