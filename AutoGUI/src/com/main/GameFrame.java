package com.main;

import com.graphics.Renderer;

public abstract class GameFrame {

	public abstract void init(GameContainer gc);
	
	public abstract void update(GameContainer gc, float dt);

	public abstract void render(GameContainer gc, Renderer r);
}