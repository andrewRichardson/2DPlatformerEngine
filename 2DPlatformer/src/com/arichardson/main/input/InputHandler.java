package com.arichardson.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputHandler implements KeyListener, MouseListener, MouseWheelListener{

	private boolean[] keys = new boolean[256];
	public boolean[] mouse = new boolean[508];
	public boolean up, down, left, right, shift, ctrl, space, mouseLeft, mouseRight, mouseScrollUp, mouseScrollDown, escape;

	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W] || keys[KeyEvent.VK_SPACE];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		shift = keys[KeyEvent.VK_SHIFT];
		space = keys[KeyEvent.VK_SPACE];
		mouseLeft = mouse[MouseEvent.BUTTON1];
		mouseRight = mouse[MouseEvent.BUTTON3];
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		if (keys[KeyEvent.VK_CONTROL])
			ctrl ^= true;
		if(keys[KeyEvent.VK_ESCAPE])
			escape ^= true;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouse[e.getButton()] = false;
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
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() > 0)
			mouseScrollDown = true;
		else if(e.getWheelRotation() < 0)
			mouseScrollUp = true;
	}
	
}
