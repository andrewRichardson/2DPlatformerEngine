package com.arichardson.main.graphics.ui;

import java.awt.Color;

public class UIMenu extends UIComponent{

	public static final int HORZ_LAYOUT = 0;
	public static final int VERT_LAYOUT = 1;
	
	private int layout, paddingX, paddingY, componentX, componentY;
	public UIComponent[] uiComponents;
	boolean backgroundPanel, keepCompSize, boundsForComponent;
	UIPanel panel;
	Color bgColor;
	
	public UIMenu(int x, int y, int layout, UIComponent[] UIComponents, int[] fourBounds, boolean boundsForComponent, boolean backPanel, Color backgroundColor, boolean keepComponentSize){
		super(x, y, fourBounds[2], fourBounds[3], false, backgroundColor, 0f);
		this.layout = layout;
		uiComponents = UIComponents;
		paddingX = fourBounds[0];
		paddingY = fourBounds[1];
		componentX = fourBounds[2];
		componentY = fourBounds[3];
		bgColor = backgroundColor;
		keepCompSize = keepComponentSize;
		this.boundsForComponent = boundsForComponent;
		
		if(boundsForComponent && !keepCompSize){
			if(layout == VERT_LAYOUT){
				width = width+2*paddingX;
				height = (height+paddingY)*uiComponents.length+paddingY;
			}
			if(layout == HORZ_LAYOUT){
				width = (width+paddingX)*uiComponents.length+paddingX;
				height = height+2*paddingY;
			}
		} else if(!boundsForComponent && keepCompSize){
			if(layout == VERT_LAYOUT){
				int w = 0;
				int h = paddingY;
				for(int i = 0; i < uiComponents.length; i++){
					if(uiComponents[i].width > w)
						w = uiComponents[i].width;
					h += uiComponents[i].height + paddingY;
				}
				width = w;
				height = h;
			}
			if(layout == HORZ_LAYOUT){
				int w = paddingX;
				int h = 0;
				for(int i = 0; i < uiComponents.length; i++){
					if(uiComponents[i].height > h)
						h = uiComponents[i].height;
					w += uiComponents[i].width + paddingX;
				}
				width = w;
				height = h;
			}
		}
		
		if(!keepCompSize)
			autoSizeComponents();
		else
			autoPlaceComponents();
	}
	
	public void autoSizeComponents(){
		if(layout == VERT_LAYOUT){
			if(!boundsForComponent){
				int componentHeight = ((height-(paddingY*uiComponents.length+1))/uiComponents.length);
				for(int i = 0; i < uiComponents.length; i++){
					uiComponents[i].x = this.x+paddingX;
					uiComponents[i].y = this.y+(paddingY+componentHeight)*i;
					uiComponents[i].width = width-paddingX*2;
					uiComponents[i].height = componentHeight;
				}
				if(backgroundPanel)
					panel = new UIPanel(x, y, width, height, false, bgColor, 0.8f);
			} else{
				for(int i = 0; i < uiComponents.length; i++){
					uiComponents[i].x = this.x+paddingX;
					uiComponents[i].y = this.y+(paddingY+componentY)*i;
					uiComponents[i].width = componentX;
					uiComponents[i].height = componentY;
				}
				if(backgroundPanel)
					panel = new UIPanel(x, y, componentX+2*paddingX, (componentY+paddingY)*uiComponents.length+paddingY, false, bgColor, 0.8f);
			}
		} else if(layout == HORZ_LAYOUT){
			if(!boundsForComponent){
				int componentWidth = ((width-(paddingX*uiComponents.length+1))/uiComponents.length);
				for(int i = 0; i < uiComponents.length; i++){
					uiComponents[i].y = this.y+paddingY;
					uiComponents[i].x = this.x+(paddingX+componentWidth)*i;
					uiComponents[i].height = height-paddingY*2;
					uiComponents[i].width = componentWidth;
				}
				if(backgroundPanel)
					panel = new UIPanel(x, y, width, height, false, bgColor, 0.8f);
			} else{
				for(int i = 0; i < uiComponents.length; i++){
					uiComponents[i].x = this.x+(paddingX+componentX)*i;
					uiComponents[i].y = this.y+paddingY;
					uiComponents[i].width = componentX;
					uiComponents[i].height = componentY;
				}
				if(backgroundPanel)
					panel = new UIPanel(x, y, (componentX+paddingX)*uiComponents.length+paddingX, componentY+2*paddingY, false, bgColor, 0.8f);
			}
		}
	}
	
	public void autoPlaceComponents(){
		if(layout == VERT_LAYOUT){
			int h = this.y + paddingY;
			for(int i = 0; i < uiComponents.length; i++){
				uiComponents[i].y = h;
				uiComponents[i].x = this.x+paddingX;
				h += uiComponents[i].height + paddingY;
			}
		} else if(layout == HORZ_LAYOUT){
			int w = this.x + paddingX;
			for(int i = 0; i < uiComponents.length; i++){
				uiComponents[i].x = w;
				uiComponents[i].y = this.y+paddingY;
				w += uiComponents[i].width + paddingX;
			}
		}
	}
	
}
