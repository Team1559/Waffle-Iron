package com.auto.components;

import java.text.DecimalFormat;

import com.auto.Node;
import com.graphics.Renderer;
import com.gui.UIPanel;
import com.gui.UITextField;
import com.main.GameContainer;

public class StepRecorder extends AutoComponent {

	private UIPanel panel;
	private UITextField textField;
	private Map map;

	public StepRecorder(int x, int y) {
		super(x, y);
	}

	public void init(GameContainer ac) {
		this.panel = new UIPanel(x, y, 240, 420, "Robot Instructions");
		this.textField = new UITextField(10, 35, 220, panel.getHeight() - 45, panel);
		this.map = parent.getMap();
	}

	public void update(GameContainer ac, float dt) {
	}

	public void render(GameContainer ac, Renderer r) {
		panel.render(r);
		textField.render(r);
		int lineNumber = 0;
		for (int i = 0; i < parent.nodes.size(); i++) {
			Node n = parent.nodes.get(i);
			if (i >= 1) {
				if ((n.y > 354 && parent.nodes.get(i - 1).y < 354) || (n.y < 354 && parent.nodes.get(i - 1).y > 354)) {
					if (n.x < parent.getMap().getPlotter().BORDER_12) {
						input(r, lineNumber, "CROSS DEFENSE", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
						input(r, lineNumber, "Low Bar", 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
					} else if (n.x < parent.getMap().getPlotter().BORDER_23) {
						input(r, lineNumber, "CROSS DEFENSE", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
						input(r, lineNumber, map.getDefenses()[1].getName(), 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
					} else if (n.x < parent.getMap().getPlotter().BORDER_34) {
						input(r, lineNumber, "CROSS DEFENSE", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
						input(r, lineNumber, map.getDefenses()[2].getName(), 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
					} else if (n.x < parent.getMap().getPlotter().BORDER_45) {
						input(r, lineNumber, "CROSS DEFENSE", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
						input(r, lineNumber, map.getDefenses()[3].getName(), 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
					} else {
						input(r, lineNumber, "CROSS DEFENSE", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
						input(r, lineNumber, map.getDefenses()[4].getName(), 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
					}
					lineNumber++;
				}
				input(r, lineNumber, "GO TO " + i, 0xffffffff, x + 20, y + lineNumber * 16 + 40);
				input(r, lineNumber, "Speed: " + new DecimalFormat("0.00").format(n.getSpeed()), 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
				lineNumber++;
			}
			if (n.isShoot()) {
				input(r, lineNumber, "SHOOT", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
				lineNumber++;
			}
			if (n.getWaitTime() > 0.0f) {
				input(r, lineNumber, "WAIT", 0xffffffff, x + 20, y + lineNumber * 16 + 40);
				input(r, lineNumber, "Time: " + new DecimalFormat("0.00").format(n.getWaitTime()) + " sec", 0xffbfbfbf, x + 130, y + lineNumber * 16 + 40);
				lineNumber++;
			}
		}
	}

	private void input(Renderer r, int lineNumber, String s, int color, int x, int y) {
		if (lineNumber < 23) {
			r.drawString(s, color, x, y);
		}
	}
}