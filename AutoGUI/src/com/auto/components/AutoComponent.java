package com.auto.components;

import com.auto.AutoContainer;
import com.graphics.Renderer;
import com.main.GameContainer;

public abstract class AutoComponent {

	protected int x, y;
	protected String id;
	protected AutoContainer parent;
	
	public AutoComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public abstract void init(GameContainer ac);

	public abstract void update(GameContainer ac, float dt);

	public abstract void render(GameContainer ac, Renderer r);

	public void setParent(AutoContainer parent) {
		this.parent = parent;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}