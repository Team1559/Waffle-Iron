package com.auto;

import com.auto.components.ControlsPane;
import com.auto.components.DefenseSelector;
import com.auto.components.Map;
import com.auto.components.NodeEditor;
import com.auto.components.Plotter;
import com.auto.components.StepRecorder;
import com.graphics.Renderer;
import com.main.GameContainer;
import com.main.GameFrame;

public class MainComponent extends GameFrame {

	AutoContainer container;
	
	public MainComponent() {
		container = new AutoContainer();
	}

	public void init(GameContainer ac) {
		container.addComponent(new DefenseSelector(10, 160));
		container.addComponent(new StepRecorder(750, 110));
		container.addComponent(new ControlsPane(750, 10));
		container.addComponent(new Map(240, 10));
		container.addComponent(new NodeEditor(10, 10));
		container.getMap().addPlotter(ac);
		container.init(ac);
		((Plotter) container.getMap().getPlotter()).addListener((NodeEditor) container.getComponent("nodeeditor"));
	}

	public void update(GameContainer ac, float dt) {
		container.update(ac, dt);
	}

	public void render(GameContainer ac, Renderer r) {
		container.render(ac, r);
	}

	public static void main(String[] args) {
		GameContainer ac = new GameContainer(new MainComponent());
		ac.setWidth(1000);
		ac.setHeight(540);
		ac.setScale(1);
		ac.setClearScreen(true);
		ac.setLockFramerate(true);
		ac.start();
	}
}