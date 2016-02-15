package com.gui;

import com.graphics.Font;
import com.graphics.Image;
import com.graphics.Renderer;
import com.main.GameContainer;

public class UIToggleButton extends UIComponent {

	private static final int BORDER_COLOR = 0xff000000;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int BORDER_SHADOW_COLOR = 0xff0f0f0f;
	private static final int BACKGROUND_COLOR = 0xff2f2f2f;
	private static final int TEXT_COLOR = 0xffefefef;
	private static final int TEXT_SHADOW_COLOR = 0xff0f0f0f;

	private String text;
	private Image image;
	private boolean hover, depressed, down;

	public UIToggleButton(int x, int y, int width, int height, String text, Image image, UIPanel parent) {
		super(x, y, width, height, parent);
		this.text = text;
		this.image = image;
	}

	public UIToggleButton(int x, int y, int width, int height, String text, UIPanel parent) {
		this(x, y, width, height, text, null, parent);
	}

	public UIToggleButton(int x, int y, int width, int height, Image image, UIPanel parent) {
		this(x, y, width, height, null, image, parent);
	}

	public UIToggleButton(int x, int y, int width, int height, UIPanel parent) {
		this(x, y, width, height, null, null, parent);
	}

	public void init(GameContainer ac) {

	}

	public void update(GameContainer ac, float dt) {
		int mx = ac.getInput().getMouseX();
		int my = ac.getInput().getMouseY();
		depressed = ac.getInput().isButtonDown(1) && bounds.contains(mx, my);
		if (bounds.contains(mx, my)) {
			if (ac.getInput().isButtonReleased(1) && down) {
				down = false;
			} else if (ac.getInput().isButtonReleased(1) && !down) {
				down = true;
			}
			hover = true;
		} else {
			hover = false;
		}
	}

	public void render(Renderer r) {
		if (down || depressed) {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR - (hover ? 0 : 0x101010));
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_SHADOW_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay, width, height, BORDER_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_HL_COLOR + (hover ? 0x101010 : 0));
		} else {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay, width, height, BORDER_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_HL_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_SHADOW_COLOR + (hover ? 0x101010 : 0));
		}

		if (image != null && text == null) { // image only
			r.drawImage(image, ax + width / 2 - image.width / 2, ay + height / 2 - image.height / 2);
		} else if (image == null && text != null) { // text only
			r.drawString(text, TEXT_SHADOW_COLOR + (hover ? 0x101010 : 0), ax + width / 2 + 1, ay + 6, Font.CENTER, Font.TOP);
			r.drawString(text, TEXT_COLOR + (hover ? 0x101010 : 0), ax + width / 2, ay + 5, Font.CENTER, Font.TOP);
		} else if (image != null && text != null) {
			r.drawImage(image, ax + 5, ay + height / 2 - image.height / 2);
			r.drawString(text, TEXT_SHADOW_COLOR + (hover ? 0x101010 : 0), ax + image.width + 6, ay + 6, Font.CENTER, Font.TOP);
			r.drawString(text, TEXT_COLOR + (hover ? 0x101010 : 0), ax + image.width + 5, ay + 5, Font.CENTER, Font.TOP);
		}
	}

	public boolean isHover() {
		return hover;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean b) {
		this.down = b;
	}
}