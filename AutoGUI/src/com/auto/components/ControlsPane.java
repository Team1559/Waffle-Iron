package com.auto.components;

import com.auto.WaffleExporter;
import com.graphics.Renderer;
import com.gui.UIButton;
import com.gui.UIDropDown;
import com.gui.UIPanel;
import com.main.GameContainer;

public class ControlsPane extends AutoComponent {

	private UIPanel panel;
	private UIButton btnDelete;
	private UIButton btnExport;
	private UIDropDown ddStartLights, ddEndLights;
	private String[] lightOptions = { "None", "Red", "Blue", "Victor", "Rainbow", "Party" };

	public ControlsPane(int x, int y) {
		super(x, y);
		panel = new UIPanel(x, y, 240, 90);
		btnDelete = new UIButton(10, 5, 110, 25, "Delete Path", panel);
		btnExport = new UIButton(btnDelete.getX() + btnDelete.getWidth(), btnDelete.getY(), btnDelete.getWidth(), btnDelete.getHeight(), "Export", panel);
		ddStartLights = new UIDropDown(10, 55, 110, 25, "Starting Lights:", lightOptions, panel);
		ddEndLights = new UIDropDown(ddStartLights.getX() + ddStartLights.getWidth(), ddStartLights.getY(), 110, 25, "Ending Lights:", lightOptions, panel);
	}

	public void init(GameContainer ac) {

	}

	public void update(GameContainer ac, float dt) {
		btnDelete.update(ac, dt);
		btnExport.update(ac, dt);
		ddStartLights.update(ac, dt);
		ddEndLights.update(ac, dt);

		if (btnDelete.isClicked()) {
			parent.nodes.clear();
			parent.getMap().getPlotter().setSelectedNode(-1);
		}
		if (btnExport.isClicked()) {
			WaffleExporter.export(parent, ac);
		}
	}

	public void render(GameContainer ac, Renderer r) {
		panel.render(r);
		btnDelete.render(r);
		r.drawRect(panel.getX() + btnDelete.getX() + 5, panel.getY() + btnDelete.getY() + btnDelete.getHeight() - 5, btnDelete.getWidth() - 10, 0, 0xffbf1f1f);
		btnExport.render(r);
		r.drawRect(panel.getX() + btnExport.getX() + 5, panel.getY() + btnExport.getY() + btnExport.getHeight() - 5, btnExport.getWidth() - 10, 0, 0xff00bf1f);
		ddStartLights.render(r);
		ddEndLights.render(r);
	}

	public String getStartLightPattern() {
		return ddStartLights.getSelectedOption();
	}

	public String getEndLightPattern() {
		return ddEndLights.getSelectedOption();
	}
}