package com.gui;

import java.awt.Rectangle;

import com.graphics.Font;
import com.graphics.Pixel;
import com.graphics.Renderer;
import com.main.GameContainer;

public class UIDropDown extends UIComponent {

	private static final int BORDER_COLOR = 0xff000000;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int BORDER_SHADOW_COLOR = 0xff0f0f0f;
	private static final int BACKGROUND_COLOR = 0xff2f2f2f;
	private static final int TEXT_COLOR = 0xffefefef;
	private static final int TEXT_SHADOW_COLOR = 0xff0f0f0f;

	private String title;
	private String[] options;
	private int highlightedOption = -1;
	private int selectedOption = -1;
	private boolean opened;
	private boolean hover;

	public UIDropDown(int x, int y, int width, int height, String title, String[] options, UIPanel parent) {
		super(x, y, width, height, parent);
		this.title = title;
		this.options = options;
		selectedOption = 0;
	}

	public void init(GameContainer ac) {

	}

	public void update(GameContainer ac, float dt) {
		int mx = ac.getInput().getMouseX();
		int my = ac.getInput().getMouseY();
		if (bounds.contains(mx, my)) {
			if (ac.getInput().isButtonReleased(1)) {
				opened = !opened;
			}
			hover = !ac.getInput().isButtonDown(1);
		} else {
			hover = false;
		}
		if (opened) {
			highlightedOption = -1;
			for (int i = 0; i < options.length; i++) {
				Rectangle optionBounds = new Rectangle(ax, ay + (i + 1) * height, width, height);
				if (optionBounds.contains(mx, my)) {
					highlightedOption = i;
					break;
				}
			}
		}
		if (highlightedOption > -1 && ac.getInput().isButtonPressed(1)) {
			selectedOption = highlightedOption;
			opened = false;
		}
	}

	public void render(Renderer r) {
		if (opened) {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR - 0x101010);
			r.drawRect(ax, ay, width, height, BORDER_COLOR);
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_SHADOW_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_HL_COLOR);
			for (int i = 0; i < options.length; i++) {
				r.fillRect(ax, ay + (i + 1) * height, width, height, BACKGROUND_COLOR + (highlightedOption == i ? 0x101010 : 0));
				r.drawRect(ax, ay + (i + 1) * height, width, height, BORDER_COLOR + (highlightedOption == i ? 0x101010 : 0));
				r.drawRect(ax + 1, ay + 1 + (i + 1) * height, width - 2, 0, BORDER_HL_COLOR + (highlightedOption == i ? 0x101010 : 0));
				r.drawRect(ax, ay + height + 1 + (i + 1) * height, width, 0, BORDER_SHADOW_COLOR + (highlightedOption == i ? 0x101010 : 0));
				r.drawString(options[i], TEXT_SHADOW_COLOR + (highlightedOption == i ? 0x101010 : 0), ax + 8 + 1, ay + height / 2 + (i + 1) * height + 1, Font.LEFT, Font.MIDDLE);
				int textColor = options[i].equalsIgnoreCase("Party") ? Pixel.getRandomColor() : TEXT_COLOR + (highlightedOption == 1 ? 0x101010 : 0);
				r.drawString(options[i], textColor, ax + 8, ay + height / 2 + (i + 1) * height, Font.LEFT, Font.MIDDLE);
			}
		} else {
			r.fillRect(ax, ay, width, height, BACKGROUND_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay, width, height, BORDER_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax + 1, ay + 1, width - 2, 0, BORDER_HL_COLOR + (hover ? 0x101010 : 0));
			r.drawRect(ax, ay + height + 1, width, 0, BORDER_SHADOW_COLOR);
		}
		if (title != null) {
			r.drawString(title, TEXT_SHADOW_COLOR, ax + 4 + 1, ay + 1, Font.LEFT, Font.BOTTOM);
			r.drawString(title, TEXT_COLOR, ax + 4, ay, Font.LEFT, Font.BOTTOM);
		}
		if (selectedOption > -1 && selectedOption < options.length && options[selectedOption] != null) {
			r.drawString(options[selectedOption], TEXT_SHADOW_COLOR + (hover ? 0x101010 : 0), ax + 8 + 1, ay + height / 2 + 1, Font.LEFT, Font.MIDDLE);
			int textColor = options[selectedOption].equalsIgnoreCase("Party") ? Pixel.getRandomColor() : TEXT_COLOR + (hover ? 0x101010 : 0);
			r.drawString(options[selectedOption], textColor, ax + 8, ay + height / 2, Font.LEFT, Font.MIDDLE);
		}
		r.drawLine(ax + width - (height * 3 / 4) + 2, ay + height / 3 + 1, ax + width - height / 4 - 2, ay + height / 3 + 1, BORDER_HL_COLOR);
		r.drawLine(ax + width - (height * 3 / 4), ay + height / 3, ax + width - height / 4, ay + height / 3, BORDER_COLOR);
		r.drawLine(ax + width - (height * 3 / 4), ay + height / 3 + 1, ax + width - height / 2, ay + height * 2 / 3 + 1, BORDER_SHADOW_COLOR);
		r.drawLine(ax + width - height / 4, ay + height / 3 + 1, ax + width - height / 2, ay + height * 2 / 3 + 1, BORDER_SHADOW_COLOR);
		r.drawLine(ax + width - (height * 3 / 4), ay + height / 3, ax + width - height / 2, ay + height * 2 / 3, BORDER_COLOR);
		r.drawLine(ax + width - height / 4, ay + height / 3, ax + width - height / 2, ay + height * 2 / 3, BORDER_COLOR);

	}

	public String getSelectedOption() {
		if (selectedOption > -1 && selectedOption < options.length) {
			return options[selectedOption];
		} else {
			return null;
		}
	}
}