package com.arichardson.main.api;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.arichardson.main.graphics.Sprite;
import com.arichardson.main.graphics.ui.UIController;

public class Graphics {

	private Graphics2D g;
	private Canvas canvas;
	private int width;
	private int height;
	private BufferStrategy bs;
	
	public Graphics(Canvas canvas, int width, int height) {
		this.canvas = canvas;
		this.width = width;
		this.height = height;
	}
	
	public boolean startRender(){
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return true;
		}

		g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		return false;
	}
	
	public void finishRender(){
		int fontSize = width / 16;

		g.setFont(new Font("HELVETICA", Font.BOLD, fontSize));
		g.setColor(Color.WHITE);
		
		g.dispose();
		bs.show();
	}
	
	public void drawLine(int x, int y, int x2, int y2, Color color){
		g.setColor(color);
		g.drawLine(x, y, x2, y2);
	}
	
	public void drawRect(int x, int y, int width, int height, boolean filled, Color color){
		g.setColor(color);
		if(filled)
			g.fillRect(x, y, width, height);
		else
			g.drawRect(x, y, width, height);
	}
	
	public void drawRect(int x, int y, int width, int height, boolean filled, Color color, float alpha){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		drawRect(x, y, width, height, filled, color);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
	}
	
	public void drawEllipse(int x, int y, int width, int height, boolean filled, Color color){
		g.setColor(color);
		if(filled)
			g.fillOval(x, y, width, height);
		else
			g.drawOval(x, y, width, height);
	}
	
	
	public void drawEllipse(int x, int y, int width, int height, boolean filled, Color color, float alpha){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		drawEllipse(x, y, width, height, filled, color);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
	}
	
	public void drawSprite(int x, int y, int width, int height, Sprite sprite){
		g.drawImage(sprite.getImage(), x, y, width, height, null);
	}
	
	public void drawSprite(int x, int y, Sprite sprite){
		drawSprite(x, y, sprite.getWidth(), sprite.getHeight(), sprite);
	}
	
	public void drawImage(Image image, int x, int y){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.drawImage(image, x, y, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
	}
	
	public void drawUI(UIController uiController){
		uiController.render(g);
	}
	
	public BufferedImage loadImage(String fileName){
		BufferedImage image = null;
		try {
			File file = new File(fileName);
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
