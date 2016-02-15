package com.gui;

import com.graphics.Font;
import com.graphics.Image;
import com.graphics.Renderer;
import com.main.GameContainer;

public class UIButton extends UIComponent {

	private static final int BORDER_COLOR = 0xff000000;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int BORDER_SHADOW_COLOR = 0xff0f0f0f;
	private static final int BACKGROUND_COLOR = 0xff2f2f2f;
	private static final int TEXT_COLOR = 0xffefefef;
	private static final int TEXT_SHADOW_COLOR = 0xff0f0f0f;

	private String text;
	private Image image;
	private boolean hover, depressed, click;

	public UIButton(int x, int y, int width, int height, String text, Image image, UIPanel parent) {
		super(x, y, width, height, parent);
		this.text = text;
		this.image = image;
	}

	public UIButton(int x, int y, int width, int height, String text, UIPanel parent) {
		this(x, y, width, height, text, null, parent);
	}
	
	public UIButton(int x, int y, int width, int height, Image image, UIPanel parent) {
		this(x, y, width, height, null, image, parent);
	}
	
	public UIButton(int x, int y, int width, int height, UIPanel parent) {
		this(x, y, width, height, null, null, parent);
	}
	
	public void init(GameContainer ac) {
		
	}

	public void update(GameContainer ac, float dt) {
		int mx = ac.getInput().getMouseX();
		int my = ac.getInput().getMouseY();
		if (bounds.contains(mx, my)) {
			click = ac.getInput().isButtonReleased(1);
			depressed = ac.getInput().isButtonDown(1);
			hover = !ac.getInput().isButtonDown(1);
		} else {
			hover = false;
		}
	}

	public void render(Renderer r) {
		if (depressed) {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR - 0x101010);
			r.drawRect(ax, ay, width, height, BORDER_COLOR);
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_SHADOW_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_HL_COLOR);
		} else {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay, width, height, BORDER_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_HL_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_SHADOW_COLOR);
		}

		if (image != null && text == null) { // image only
			r.drawImage(image, ax + width / 2 - image.width / 2, ay + height / 2 - image.height / 2);
		} else if (image == null && text != null) { // text only
			r.drawString(text, TEXT_SHADOW_COLOR + (hover ? 0x101010 : 0), ax + width / 2 + 1, ay + height / 2 + 1, Font.CENTER, Font.MIDDLE);
			r.drawString(text, TEXT_COLOR + (hover ? 0x101010 : 0), ax + width / 2, ay + height / 2, Font.CENTER, Font.MIDDLE);
		} else if (image != null && text != null) { // image and text
			r.drawImage(image, ax + 5, ay + height / 2 - image.height / 2);
			r.drawString(text, TEXT_SHADOW_COLOR + (hover ? 0x101010 : 0), ax + image.width + 10 + 1, ay + height / 2 + 1, Font.LEFT, Font.MIDDLE);
			r.drawString(text, TEXT_COLOR + (hover ? 0x101010 : 0), ax + image.width + 10, ay + height / 2, Font.LEFT, Font.MIDDLE);
		}
	}

	public boolean isClicked() {
		if (click) {
			click = false;
			return true;
		}
		return false;
	}

	public boolean isHover() {
		return hover;
	}
}