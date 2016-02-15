package com.gui;

import java.awt.Rectangle;

import com.graphics.Renderer;
import com.main.GameContainer;

public abstract class UIComponent {

	protected int ax, ay;
	protected int x, y, width, height;
	protected UIPanel parent;
	protected Rectangle bounds;
	
	public UIComponent(int x, int y, int width, int height, UIPanel parent) {
		this.ax = x + parent.getX();
		this.ay = y + parent.getY();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
		defineBounds(ax, ay, width, height);
	}

	public abstract void init(GameContainer ac);

	public abstract void update(GameContainer ac, float dt);

	public abstract void render(Renderer r);

	private void defineBounds(int x, int y, int width, int height) {
		this.bounds = new Rectangle(x, y, width, height);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}