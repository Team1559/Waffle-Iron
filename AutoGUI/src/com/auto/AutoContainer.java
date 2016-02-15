package com.auto;

import java.util.Vector;

import com.auto.components.AutoComponent;
import com.auto.components.Map;
import com.graphics.Renderer;
import com.main.GameContainer;

public class AutoContainer {

	public Vector<Node> nodes = new Vector<Node>();
	public int selectedNode = -1;
	public Vector<AutoComponent> components = new Vector<AutoComponent>();
	private Map map;

	public AutoContainer() {
	}

	public void init(GameContainer ac) {
		for (AutoComponent c : components) {
			c.init(ac);
		}
	}

	public void update(GameContainer ac, float dt) {
		for (AutoComponent c : components) {
			c.update(ac, dt);
		}
	}

	public void render(GameContainer ac, Renderer r) {
		for (AutoComponent c : components) {
			c.render(ac, r);
		}
	}

	public Node getSelectedNode() {
		if (nodes.size() > selectedNode && selectedNode > -1)
			return nodes.get(selectedNode);
		return null;
	}

	public void addComponent(AutoComponent c) {
		components.add(c);
		c.setParent(this);
	}

	// ease of access
	public Map getMap() {
		if (map == null) {
			map = (Map) getComponent("map");
		}
		if (map != null) {
			return map;
		}
		return null;
	}

	public AutoComponent getComponent(String s) {
		if (s == null) {
			return null;
		} else {
			for (AutoComponent comp : components) {
				if (comp.getClass().getSimpleName().equalsIgnoreCase(s)) {
					return comp;
				}
			}
		}
		return null;
	}
}