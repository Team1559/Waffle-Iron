package com.gui;

import com.graphics.Font;
import com.graphics.Renderer;

public class UIPanel {

	private static final int BORDER_COLOR = 0xff000000;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int SHADOW_COLOR = 0xff0f0f0f;
	private static final int PANEL_COLOR = 0xff1f1f1f;
	private static final int TITLE_SHADOW_COLOR = 0xff0f0f0f;
	private static final int TITLE_COLOR = 0xffffffff;

	private int x, y, width, height;
	private String title;

	public UIPanel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = null;
	}

	public UIPanel(int x, int y, int width, int height, String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void render(Renderer r) {
		r.drawRect(x + 1, y + 1, width, height, SHADOW_COLOR);
		r.fillRect(x, y, width, height, PANEL_COLOR);
		r.drawRect(x, y + 1, width, 0, BORDER_HL_COLOR);
		r.drawRect(x, y, width, height, BORDER_COLOR);
		if (title != null) {
			r.drawString(title, TITLE_SHADOW_COLOR, x + 6, y + 6, Font.TOP, Font.LEFT, Font.MEDIEVAL);
			r.drawString(title, TITLE_COLOR, x + 5, y + 5, Font.TOP, Font.LEFT, Font.MEDIEVAL);
			r.drawRect(x + 5, y + 25, width - 10, 0, TITLE_COLOR);
		}
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