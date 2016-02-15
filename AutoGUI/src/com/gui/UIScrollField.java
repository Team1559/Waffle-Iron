package com.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import com.graphics.Font;
import com.graphics.Renderer;
import com.main.GameContainer;

public class UIScrollField extends UIComponent {

	private static final int BORDER_COLOR = 0xff000000;
	private static final int SHADOW_COLOR = 0xff070707;
	private static final int BORDER_HL_COLOR = 0xff5f5f5f;
	private static final int BACKGROUND_COLOR = 0xff0f0f0f;
	private static final int TEXT_COLOR = 0xffefefef;
	private static final int TEXT_SHADOW_COLOR = 0xff0f0f0f;

	private DecimalFormat dFormat = new DecimalFormat("0.00");

	private int dy;
	private String label;

	private Point clickLocation = new Point(-1, -1);
	private boolean highlight;
	
	private double value;

	public UIScrollField(int x, int y, int width, int height, String label, UIPanel parent) {
		super(x, y, width, height, parent);
		this.label = label;
		this.bounds = new Rectangle(ax, ay, width, height);
	}

	public void init(GameContainer ac) {
		
	}
	
	public void update(GameContainer ac, float dt) {
		Point mouseLocation = new Point(ac.getInput().getMouseX(), ac.getInput().getMouseY());
		highlight = bounds.contains(mouseLocation) || (bounds.contains(clickLocation) && ac.getInput().isButtonDown(1));

		if (ac.getInput().isButtonPressed(1)) {
			clickLocation = mouseLocation;
		}

		if (ac.getInput().isButtonDown(1)) {
			if (bounds.contains(clickLocation)) {
				dy = clickLocation.y - mouseLocation.y;
			}
		}
		if (ac.getInput().isButtonReleased(1)) {
			dy = 0;
		}
	}

	public void render(Renderer r) {
		render(r, "");
	}

	public void render(Renderer r, String units) {
		r.fillRect(ax, ay, width, height, BACKGROUND_COLOR + (highlight ? 0x00101010 : 0));
		r.drawRect(ax, ay, width, height, BORDER_COLOR + (highlight ? 0x00101010 : 0));
		r.drawRect(ax + 1, ay + 1, width - 2, 0, SHADOW_COLOR + (highlight ? 0x00101010 : 0));
		r.drawRect(ax, ay + height + 1, width, 0, BORDER_HL_COLOR + (highlight ? 0x00101010 : 0));
		r.drawString(dFormat.format(value) + " " + units, TEXT_SHADOW_COLOR, ax + 6, ay + height / 2 + 1, Font.LEFT, Font.MIDDLE);
		r.drawString(dFormat.format(value) + " " + units, TEXT_COLOR, ax + 5, ay + height / 2, Font.LEFT, Font.MIDDLE);
		r.drawString(label, TEXT_SHADOW_COLOR, ax - 4, ay + height / 2 + 1, Font.RIGHT, Font.MIDDLE);
		r.drawString(label, TEXT_COLOR, ax - 5, ay + height / 2, Font.RIGHT, Font.MIDDLE);
	}

	public int getDeltaY() {
		return dy;
	}

	public void reset() {
		dy = 0;
	}

	public void setValue(double d) {
		this.value = d;
	}
	
	public double getValue() {
		return value;
	}
}