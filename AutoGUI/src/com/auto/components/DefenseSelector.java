package com.auto.components;

import java.awt.Point;

import com.auto.Defense;
import com.graphics.Font;
import com.graphics.Image;
import com.graphics.Renderer;
import com.gui.UIPanel;
import com.main.GameContainer;

public class DefenseSelector extends AutoComponent {

	private UIPanel panel;
	private Point clickLocation;
	private final int tileX = 45;
	private final int tileY = 30;
	private final int tileW, tileH;
	private Image tileHighlighted, tileDepressed, tileSelected;
	private int highlightedDefense = -1;
	private int selectedDefense = -1;
	private final int spacing = 5;
	private boolean a, b, c, d;

	public DefenseSelector(int x, int y) {
		super(x, y);
		this.panel = new UIPanel(x, y, 220, 370, "Defense Selection");
		tileHighlighted = new Image("/tileHighlighted.png");
		tileDepressed = new Image("/tileDepressed.png");
		tileSelected = new Image("/tileSelected.png");
		tileW = Defense.PORTCULLIS.getIsoImage().width;
		tileH = Defense.PORTCULLIS.getIsoImage().height;
	}

	public void init(GameContainer ac) {

	}

	public void update(GameContainer ac, float dt) {

		a = b = c = d = false;
		for (int i = 1; i < 5; i++) {
			if (parent.getMap().getDefenses()[i] == Defense.NULL)
				continue;
			int id = parent.getMap().getDefenses()[i].getId();
			id /= 2;
			if (id == 0)
				a = true;
			else if (id == 1)
				b = true;
			else if (id == 2)
				c = true;
			else if (id == 3)
				d = true;
		}

		highlightedDefense = -1;

		int row = -1;
		int col = -1;
		int mx = ac.getInput().getMouseX();
		int my = ac.getInput().getMouseY();
		int dx = mx - x - tileX;
		for (int i = 0; i < 2; i++) {
			if (dx > 0 && dx < tileW) {
				col = i;
				break;
			}
			dx -= tileW + spacing;
		}

		int dy = my - y - tileY;
		for (int i = 0; i < 4; i++) {
			if (dy > 0 && dy < tileH) {
				row = i;
				break;
			}
			dy -= tileH + spacing;
		}
		if (row > -1 && col > -1) {
			highlightedDefense = row * 2 + col;
			if (ac.getInput().isButtonPressed(1)) {
				clickLocation = new Point(col, row);
			}
			if (ac.getInput().isButtonReleased(1) && clickLocation.x == col && clickLocation.y == row) {
				selectedDefense = highlightedDefense;
			}
		} else {
			clickLocation = new Point(-1, -1);
			if(ac.getInput().isButtonPressed(1)) {
				selectedDefense = -1;
			}
		}
	}

	public void render(GameContainer ac, Renderer r) {
		panel.render(r);
		for (int i = 0; i < 8; i++) {
			Image img = Defense.getDefenseById(i).getIsoImage();
			r.drawImage(img, x + tileX + (tileW + spacing) * (i % 2), y + tileY + (tileH + spacing) * (i / 2));
		}
		if (highlightedDefense > -1) {
			r.drawString(Defense.getDefenseById(highlightedDefense).getName(), 0xffffffff, x + tileX + (tileW + spacing) * (highlightedDefense % 2) + tileW / 2, y + tileY + (tileH + spacing) * (highlightedDefense / 2) + tileH, Font.CENTER,
					Font.BOTTOM);
			if (ac.getInput().isButtonDown(1)) {
				r.drawImage(tileDepressed, x + tileX + (tileW + spacing) * (highlightedDefense % 2), y + tileY + (tileH + spacing) * (highlightedDefense / 2));
			} else {
				r.drawImage(tileHighlighted, x + tileX + (tileW + spacing) * (highlightedDefense % 2), y + tileY + (tileH + spacing) * (highlightedDefense / 2));
			}
		}

		r.drawString("A)", a ? 0xffbf7f7f : 0xffffffff, x + tileX / 2, y + tileY + (tileH + spacing) * 0 + tileH / 2, Font.CENTER, Font.MIDDLE, Font.MEDIEVAL);
		r.drawString("B)", b ? 0xffbf7f7f : 0xffffffff, x + tileX / 2, y + tileY + (tileH + spacing) * 1 + tileH / 2, Font.CENTER, Font.MIDDLE, Font.MEDIEVAL);
		r.drawString("C)", c ? 0xffbf7f7f : 0xffffffff, x + tileX / 2, y + tileY + (tileH + spacing) * 2 + tileH / 2, Font.CENTER, Font.MIDDLE, Font.MEDIEVAL);
		r.drawString("D)", d ? 0xffbf7f7f : 0xffffffff, x + tileX / 2, y + tileY + (tileH + spacing) * 3 + tileH / 2, Font.CENTER, Font.MIDDLE, Font.MEDIEVAL);

		if (selectedDefense > -1) {
			r.drawImage(tileSelected, x + tileX + (tileW + spacing) * (selectedDefense % 2), y + tileY + (tileH + spacing) * (selectedDefense / 2));
		}
	}

	public Defense getSelectedDefense() {
		if (selectedDefense < 0) {
			return Defense.NULL;
		}
		for (int i = 0; i < Defense.values().length; i++) {
			if (selectedDefense == Defense.values()[i].getId()) {
				return Defense.values()[i];
			}
		}
		return Defense.NULL;
	}
}