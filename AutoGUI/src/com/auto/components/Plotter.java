package com.auto.components;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.auto.Assets;
import com.auto.Node;
import com.graphics.Font;
import com.graphics.Renderer;
import com.input.Input;
import com.main.GameContainer;

public class Plotter extends AutoComponent {

	public interface PlotterListener {
		void selectedNodeChanged();
	}

	public void addListener(PlotterListener pl) {
		listeners.add(pl);
	}

	private List<PlotterListener> listeners = new ArrayList<PlotterListener>();

	public final float MAP_WIDTH_INCHES = 319;
	public final float MAP_HEIGHT_INCHES = 324.5f;

	private final Rectangle mapBounds;
	private final Rectangle fieldBounds, passageBounds, outerWorksBounds;
	public final int BORDER_12 = 347;
	public final int BORDER_23 = 417;
	public final int BORDER_34 = 488;
	public final int BORDER_45 = 558;

	private boolean mouseInMap = false;
	private boolean mouseInBounds = false;

	private boolean displayWarning = false;

	private double startAngle;

	public Plotter(Map map) {
		super(map.getMapX(), map.getMapY());
		mapBounds = new Rectangle(x, y, Assets.imgMap.width, Assets.imgMap.height);
		fieldBounds = new Rectangle(x + 29, y + 49, 422, 431);
		passageBounds = new Rectangle(x + 382, y + 49, 69, 380);
		outerWorksBounds = new Rectangle(x + 19, y + 302, 363, 65);
	}

	public void init(GameContainer ac) {
	}

	public void update(GameContainer ac, float dt) {

		if (parent.selectedNode == 0) {
			startAngle = Math.toRadians(((NodeEditor) parent.getComponent("nodeeditor")).getAngleField().getValue() - 90);
		}

		int mx = ac.getInput().getMouseX();
		int my = ac.getInput().getMouseY();
		mouseInMap = mapBounds.contains(mx, my);
		mouseInBounds = fieldBounds.contains(mx, my) && !passageBounds.contains(mx, my) && !outerWorksBounds.contains(mx, my);
		if (parent.nodes.size() == 0) {
			mouseInBounds = mouseInBounds && my > 377;
		}

		if (mouseInMap) {
			if (mouseInBounds) {
				displayWarning = false;
				if (parent.getSelectedNode() != null) {
					int defenseBoundary = outerWorksBounds.y + outerWorksBounds.height / 2;
					if (!ac.getInput().isKeyDown(KeyEvent.VK_SHIFT) && ((parent.nodes.get(parent.nodes.size() - 1).y > defenseBoundary && my < defenseBoundary) || (parent.nodes.get(parent.nodes.size() - 1).y < defenseBoundary && my > defenseBoundary))) {
						displayWarning = true;
					}
				}
				if (ac.getInput().isButtonPressed(Input.LMB)) {
					if (ac.getInput().isKeyDown(KeyEvent.VK_SHIFT)) {
						parent.nodes.add(getSnappedNode(mx, my));
					} else {
						parent.nodes.add(new Node(mx, my, this));
					}
					if (parent.nodes.size() > 1) {
						parent.nodes.get(parent.nodes.size() - 1).setAngle(normalizeAngle(getAngle(parent.nodes.get(parent.nodes.size() - 2), parent.nodes.get(parent.nodes.size() - 1)) + 90));
						parent.nodes.get(parent.nodes.size() - 1).setSpeed(parent.nodes.get(parent.nodes.size() - 2).getSpeed());
					}
					setSelectedNode(parent.nodes.size() - 1);
				}
				if (ac.getInput().isButtonPressed(Input.RMB)) {
					if (getDistance(parent.nodes.get(0), new Point(mx, my)) < Assets.imgRobotPt.width / 2) {
						setSelectedNode(0);
					}
					for (int i = 1; i < parent.nodes.size(); i++) {
						if (getDistance(parent.nodes.get(i), new Point(mx, my)) < Assets.imgMovePt.width / 2) {
							setSelectedNode(i);
							break;
						}
					}
				}
			}
		}
	}

	public void render(GameContainer ac, Renderer r) {

		if (mouseInMap) {
			Node mousePt = ac.getInput().isKeyDown(KeyEvent.VK_SHIFT) ? getSnappedNode(ac.getInput().getMouseX(), ac.getInput().getMouseY()) : new Node(ac.getInput().getMouseX(), ac.getInput().getMouseY(), this);
			if (mouseInBounds) {
				if (displayWarning) {
					r.drawString("Robot MUST cross defenses perpendicularly!", 0xffffffff, mousePt.x, mousePt.y);
				}
				r.drawImage(Assets.imgMousePt, mousePt.x - Assets.imgMousePt.width / 2, mousePt.y - Assets.imgMousePt.height / 2);
			} else {
				r.drawImage(Assets.imgMousePtRestricted, mousePt.x - Assets.imgMousePtRestricted.width / 2, mousePt.y - Assets.imgMousePtRestricted.height / 2);
			}
		}
		for (int i = 1; i < parent.nodes.size(); i++) {
			if (i >= 1) {
				r.drawLine(parent.nodes.get(i - 1).x, parent.nodes.get(i - 1).y, parent.nodes.get(i).x, parent.nodes.get(i).y, 0xffffffff);
				r.drawString(String.valueOf(i), 0xff000000, parent.nodes.get(i).x + 5, parent.nodes.get(i).y + 5, Font.LEFT, Font.TOP);
				r.drawString(String.valueOf(i), 0xff007fff, parent.nodes.get(i).x + 4, parent.nodes.get(i).y + 4, Font.LEFT, Font.TOP);
			}
			r.drawImage(Assets.imgMovePt, parent.nodes.get(i).x - 7, parent.nodes.get(i).y - 7);
		}
		if (parent.nodes.size() > 0) {
			r.drawImage(Assets.imgRobotPt, parent.nodes.get(0).x - Assets.imgRobotPt.width / 2, parent.nodes.get(0).y - Assets.imgRobotPt.height / 2);
			r.drawLine(parent.nodes.get(0).x, parent.nodes.get(0).y, parent.nodes.get(0).x + (int) (20 * Math.cos(startAngle)), parent.nodes.get(0).y + (int) (20 * Math.sin(startAngle)), 0xffff6f2f);
			r.drawString("R", 0xff000000, parent.nodes.get(0).x + 5, parent.nodes.get(0).y + 5, Font.LEFT, Font.TOP);
			r.drawString("R", 0xffff3f00, parent.nodes.get(0).x + 4, parent.nodes.get(0).y + 4, Font.LEFT, Font.TOP);
		}
		if (parent.selectedNode > -1 && parent.selectedNode < parent.nodes.size()) {

		}
	}

	private Node getSnappedNode(int mx, int my) {
		if (parent.nodes.size() > 0) {
			int dy = my - parent.nodes.get(parent.nodes.size() - 1).y;
			int dx = mx - parent.nodes.get(parent.nodes.size() - 1).x;
			double angle = -Math.atan2(dy, dx);
			double roundedAngle = (Math.PI / 4) * (Math.round(angle / (Math.PI / 4)));
			if (roundedAngle % (Math.PI / 2) == 0) {
				if (Math.abs(dx) > Math.abs(dy)) {
					return new Node(mx, parent.nodes.get(parent.nodes.size() - 1).y, this);
				} else {
					return new Node(parent.nodes.get(parent.nodes.size() - 1).x, my, this);
				}
			} else {
				int delta = Math.min(Math.abs(dx), Math.abs(dy));
				return new Node((int) (parent.nodes.get(parent.nodes.size() - 1).x + delta * Math.signum(dx)), (int) (parent.nodes.get(parent.nodes.size() - 1).y + delta * Math.signum(dy)), this);
			}
		} else {
			return new Node(mx, my, this);
		}

	}

	public Vector<Node> getNodes() {
		return parent.nodes;
	}

	public void setSelectedNode(int i) {
		parent.selectedNode = i;
		for (PlotterListener pl : listeners) {
			pl.selectedNodeChanged();
		}
	}

	private double getDistance(Node n0, Point p1) {
		return Math.abs(Math.sqrt(Math.pow(n0.x - p1.x, 2) + Math.pow(n0.y - p1.y, 2)));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private double getAngle(Node n0, Node n1) {
		return Math.toDegrees(Math.atan2(n1.y - n0.y, n1.x - n0.x));
	}

	public double xToInches(int x) {
		return (x - fieldBounds.getX()) / fieldBounds.getWidth() * MAP_WIDTH_INCHES;
	}

	public double yToInches(float y) {
		return (y - fieldBounds.getY()) / fieldBounds.getHeight() * MAP_HEIGHT_INCHES;
	}

	public double xToPixels(float x) {
		return (x / MAP_WIDTH_INCHES) * fieldBounds.getWidth();
	}

	public double yToPixels(float y) {
		return (y / MAP_HEIGHT_INCHES) * fieldBounds.getHeight();
	}

	public Rectangle getFieldBounds() {
		return fieldBounds;
	}

	public Rectangle getPassageBounds() {
		return passageBounds;
	}

	public Rectangle getOuterWorksBounds() {
		return outerWorksBounds;
	}

	private static double normalizeAngle(double angle) {
		double newAngle = angle;
		while (newAngle < -180)
			newAngle += 360;
		while (newAngle > 180)
			newAngle -= 360;
		return newAngle;
	}
}