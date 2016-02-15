package com.gui;

import com.graphics.Renderer;
import com.main.GameContainer;

public class UITextField extends UIComponent {

	// just a box, no actual text
	private static final int BORDER_COLOR = 0xff000000;
	private static final int SHADOW_COLOR = 0xff070707;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int BACKGROUND_COLOR = 0xff0f0f0f;
	
	public UITextField(int x, int y, int width, int height, UIPanel parent) {
		super(x, y, width, height, parent);
	}
	
	public void init(GameContainer ac) {
		
	}

	public void update(GameContainer ac, float dt) {
		
	}
	
	public void render(Renderer r) {
		r.fillRect(ax, ay, width, height, BACKGROUND_COLOR);
		r.drawRect(ax, ay, width, height, BORDER_COLOR);
		r.drawRect(ax + 1, ay + 1, width - 2, 0, SHADOW_COLOR);
		r.drawRect(ax, ay + height + 1, width, 0, BORDER_HL_COLOR);
	}
}