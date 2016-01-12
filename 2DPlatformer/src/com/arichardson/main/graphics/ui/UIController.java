package com.arichardson.main.graphics.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.arichardson.main.input.InputHandler;

public class UIController {

	@SuppressWarnings("unused")
	private int width, height;
	private InputHandler input;
	
	private ArrayList<UIMenu> UIMenus = new ArrayList<UIMenu>();
	
	public UIController(int width, int height, InputHandler inputHandler){
		this.width = width;
		this.height = height;
		input = inputHandler;
	}
	
	public void render(Graphics2D g){
		if(input.tilde){
			for(UIMenu uiMenu : UIMenus){
				renderMenu(uiMenu, g);
			}
		}
	}
	
	public void renderMenu(UIMenu uiMenu, Graphics2D g){
		if(!uiMenu.keepCompSize)
			uiMenu.autoSizeComponents();
		if(uiMenu.backgroundPanel){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiMenu.panel.alpha));
			g.setColor(uiMenu.panel.backgroundColor);
			g.fillRect(uiMenu.panel.x, uiMenu.panel.y, uiMenu.panel.width, uiMenu.panel.height);
		}
		for(UIComponent uiComp : uiMenu.uiComponents){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiComp.alpha));
			g.setColor(uiComp.backgroundColor);
			g.fillRect(uiComp.x, uiComp.y, uiComp.width, uiComp.height);
				
			if(uiComp.getClass() == UIMenu.class)
				renderMenu((UIMenu)uiComp, g);
			}
	}
	
	public void addMenu(UIMenu uiMenu){
		UIMenus.add(uiMenu);
	}
	
}
