package com.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.main.GameContainer;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

	private float scale;

	// keyboard
	private boolean keys[] = new boolean[256];
	private boolean keysLast[] = new boolean[256];

	// mouse buttons
	private boolean buttons[] = new boolean[5];
	private boolean buttonsLast[] = new boolean[5];

	private int mouseX, mouseY;

	public Input(GameContainer gc) {
		this.scale = gc.getScale();
		gc.getWindow().getCanvas().addKeyListener(this);
		gc.getWindow().getCanvas().addMouseListener(this);
		gc.getWindow().getCanvas().addMouseMotionListener(this);
	}

	public void update() {
		keysLast = keys.clone();
		buttonsLast = buttons.clone();
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}

	public boolean isKeyPressed(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}

	public boolean isKeyReleased(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}

	public boolean isButtonDown(int button) {
		return buttons[button];
	}

	public boolean isButtonPressed(int button) {
		return buttons[button] && !buttonsLast[button];
	}

	public boolean isButtonReleased(int button) {
		return !buttons[button] && buttonsLast[button];
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() < 5) {
			buttons[e.getButton()] = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() < 5) {
			buttons[e.getButton()] = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()  < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = false;
		}
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = (int) (e.getX() / scale);
		mouseY = (int) (e.getY() / scale);
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = (int) (e.getX() / scale);
		mouseY = (int) (e.getY() / scale);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}