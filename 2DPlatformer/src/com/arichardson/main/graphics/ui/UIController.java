package com.arichardson.main.graphics.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.arichardson.main.input.InputHandler;

public class UIController {

	@SuppressWarnings("unused")
	private int width, height;
	private InputHandler input;
	public int mouseX, mouseY;
	
	private ArrayList<UIMenu> UIMenus = new ArrayList<UIMenu>();
	
	public UIController(int width, int height, InputHandler inputHandler){
		this.width = width;
		this.height = height;
		input = inputHandler;
	}
	
	public void render(Graphics2D g){
		if(input.escape){
			for(UIMenu uiMenu : UIMenus){
				renderMenu(uiMenu, g, false);
			}
		}
	}
	
	public void eventHandler(){
		for(UIMenu menu : UIMenus)
			testMenus(menu);
	}
	
	public void testMenus(UIMenu menu){
		for(UIComponent uiComp : menu.uiComponents){
			if(uiComp.getClass() == UIMenu.class){
				testMenus((UIMenu)uiComp);
			}else{
				if(mouseX > uiComp.x && mouseX < uiComp.x+uiComp.width && mouseY > uiComp.y && mouseY < uiComp.y+uiComp.height && input.mouseLeft && input.escape){
					uiComp.clicked = true;
				}
			}
		}
	}
	
	public void renderMenu(UIMenu uiMenu, Graphics2D g, boolean repeat){
		int xOffset = 0;
		int yOffset = 0;
		if(repeat){
			switch(uiMenu.layout){
			case 0:
				xOffset = -uiMenu.paddingX;
				break;
			}
		}
		if(!uiMenu.keepCompSize)
			uiMenu.autoSizeComponents();
		if(uiMenu.backgroundPanel){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiMenu.panel.alpha));
			g.setColor(uiMenu.panel.backgroundColor);
			g.fillRect(uiMenu.panel.x+xOffset, uiMenu.panel.y+yOffset, uiMenu.panel.width, uiMenu.panel.height);
		}
		if(repeat){
			switch(uiMenu.layout){
			case 1:
				xOffset = -uiMenu.paddingX;
				yOffset = 0;
				break;
			case 0:
				yOffset = uiMenu.paddingY;
				xOffset = 0;
				break;
			}
		}
		for(UIComponent uiComp : uiMenu.uiComponents){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiComp.alpha));
			g.setColor(uiComp.backgroundColor);
			g.fillRect(uiComp.x+xOffset, uiComp.y+yOffset, uiComp.width, uiComp.height);
			
			if(uiComp.getClass() == UIMenu.class){
				renderMenu(((UIMenu)uiComp), g, true);
			}
			if(uiComp.getClass() == UILabel.class){
				g.setColor(((UILabel)uiComp).textColor);
				int textWidth = g.getFontMetrics().stringWidth(((UILabel)uiComp).text);
				int textHeight = (int)g.getFont().createGlyphVector(g.getFontMetrics().getFontRenderContext(), ((UILabel)uiComp).text).getVisualBounds().getHeight();
				g.drawString(((UILabel)uiComp).text, uiComp.x+uiComp.width/2-textWidth/2+xOffset, uiComp.y+uiComp.height/2+textHeight/2+yOffset);
			}
			if(uiComp.getClass() == UISlider.class){
				UISlider slider = (UISlider)uiComp;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (uiComp.alpha * 1.5f <= 1f)?uiComp.alpha * 1.5f : 1f));
				g.fillRect(uiComp.x+xOffset+uiMenu.paddingX+(int)(slider.value*(uiComp.width-uiMenu.paddingX*2-(uiComp.height-uiMenu.paddingX*2))), uiComp.y+uiMenu.paddingX+yOffset, uiComp.height-uiMenu.paddingX*2, uiComp.height-uiMenu.paddingX*2);
			}
		}
	}
	
	public void addMenu(UIMenu uiMenu){
		UIMenus.add(uiMenu);
	}
	
}
