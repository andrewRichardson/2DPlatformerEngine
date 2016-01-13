package com.arichardson.main.graphics.ui;

import java.awt.Color;

public abstract class UIComponent {

	public int x, y, width, height;
	public Color backgroundColor;
	public float alpha;
	public boolean staticSize, clicked;
	
	public UIComponent(int x, int y, int width, int height, boolean staticSize, Color backgroundColor, float alpha){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.staticSize = staticSize;
		this.backgroundColor = backgroundColor;
		this.alpha = alpha;
	}
	
}
