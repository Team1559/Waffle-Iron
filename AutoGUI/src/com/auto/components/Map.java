package com.auto.components;

import com.auto.Assets;
import com.auto.Defense;
import com.graphics.Font;
import com.graphics.Image;
import com.graphics.Renderer;
import com.gui.UIPanel;
import com.main.GameContainer;

public class Map extends AutoComponent {

	private UIPanel panel;
	private Image imgEmpty, imgHighlighted;
	private Image imgLabelBkgd;
	private Defense[] defenses;
	private int highlightedSpot = -1;
	private final int spotX = 42;
	private final int spotY = 334;
	private final int spotW, spotH;
	private final int spacing = 11;
	private final int mapX = 10;
	private final int mapY = 30;
	private Plotter plotter;

	public Map(int x, int y) {
		super(x, y);
		panel = new UIPanel(x, y, Assets.imgMap.width + 20, Assets.imgMap.height + 40, "Field Map");
		imgEmpty = new Image("/spotEmpty.png");
		imgHighlighted = new Image("/spotHighlighted.png");
		imgLabelBkgd = new Image("/spotLabelBkgd.png");
		spotW = imgEmpty.width;
		spotH = imgEmpty.height;
	}

	public void init(GameContainer ac) {
		defenses = new Defense[5];
		defenses[0] = Defense.LOW_BAR;
		defenses[1] = Defense.NULL;
		defenses[2] = Defense.NULL;
		defenses[3] = Defense.NULL;
		defenses[4] = Defense.NULL;
	}

	public void update(GameContainer ac, float dt) {
		highlightedSpot = -1;
		if (ac.getInput().getMouseY() >= y + spotY && ac.getInput().getMouseY() <= y + spotY + spotH) {
			for (int i = 0; i < 5; i++) {
				int dx = ac.getInput().getMouseX() - (x + spotX + i * (spotW + spacing));
				if (dx >= 0 && dx <= spotW) {
					highlightedSpot = i;
					break;
				}
			}
		}
		if (highlightedSpot >= 1 && highlightedSpot <= 4 && ((DefenseSelector) parent.getComponent("defenseselector")).getSelectedDefense() != Defense.NULL && ac.getInput().isButtonPressed(1)) {
			defenses[highlightedSpot] = ((DefenseSelector) parent.getComponent("defenseselector")).getSelectedDefense();
		}
		if (plotter != null) {
			plotter.update(ac, dt);
		}
	}

	public void render(GameContainer ac, Renderer r) {
		panel.render(r);
		r.drawImage(Assets.imgMap, x + mapX, y + mapY);
		for (int i = 0; i < 5; i++) {
			if (defenses[i] != Defense.NULL) {
				r.drawImage(defenses[i].getBirdsEyeImage(), x + spotX + i * (spotW + spacing), y + spotY);
			} else {
				if (highlightedSpot == i) {
					r.drawImage(imgHighlighted, x + spotX + i * (spotW + spacing), y + spotY);
				} else {
					r.drawImage(imgEmpty, x + spotX + i * (spotW + spacing), y + spotY);
				}
			}
		}
		if (highlightedSpot > -1 && defenses[highlightedSpot] != Defense.NULL) {
			r.drawImage(imgLabelBkgd, x + spotX + highlightedSpot * (spotW + spacing) - (imgLabelBkgd.width - imgEmpty.width) / 2, y + spotY);
			r.drawString(defenses[highlightedSpot].getName(), 0xffbfbfbf, x + spotX + highlightedSpot * (spotW + spacing) + imgEmpty.width / 2 + 1, y + spotY + 22 + 1, Font.CENTER, Font.TOP);
			r.drawString(defenses[highlightedSpot].getName(), 0xff000000, x + spotX + highlightedSpot * (spotW + spacing) + imgEmpty.width / 2, y + spotY + 22, Font.CENTER, Font.TOP);
		}
		if (plotter != null) {
			plotter.render(ac, r);
		}
	}

	public void addPlotter(GameContainer ac) {
		if(plotter == null) {
			this.plotter = new Plotter(this);
			this.plotter.setParent(parent);
			this.plotter.init(ac);
		}
	}
	
	public Plotter getPlotter() {
		return plotter;
	}
	
	public Defense[] getDefenses() {
		return defenses;
	}

	public int getMapX() {
		return x + mapX;
	}

	public int getMapY() {
		return y + mapY;
	}
}