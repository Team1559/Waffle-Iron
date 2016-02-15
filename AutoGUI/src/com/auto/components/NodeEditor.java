package com.auto.components;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import com.auto.Assets;
import com.auto.Node;
import com.auto.components.Plotter.PlotterListener;
import com.graphics.Font;
import com.graphics.Renderer;
import com.gui.UIPanel;
import com.gui.UIScrollField;
import com.gui.UITextField;
import com.gui.UIToggleButton;
import com.main.GameContainer;

public class NodeEditor extends AutoComponent implements PlotterListener {

	public final float BUMPER_THICKNESS = 3.25f;// M@ Klein was here
	public final float ROBOT_WIDTH_BUMPERS_INCHES = 34.75f;
	public final float ROBOT_HEIGHT_BUMPERS_INCHES = 37.75f;
	public final int THREE = (int) (Math.PI);

	private UIPanel panel;
	private UIScrollField speedField, waitField, angleField;
	private UIToggleButton btnShoot, btnWait;
	private UITextField distField;
	private double northWallDist = 9999, eastWallDist = 9999, southWallDist = 9999, westWallDist = 9999;
	private Node n;
	private boolean showRobotBounds;
	private Node[] robotPoints = new Node[4];

	public NodeEditor(int x, int y) {
		super(x, y);
		this.panel = new UIPanel(x, y, 220, 140, "Node Editor");
		btnShoot = new UIToggleButton(10, 60, Assets.imgShootPt.width + 10, Assets.imgShootPt.height + 10, Assets.imgShootPt, panel);
		btnWait = new UIToggleButton(50, 60, Assets.imgWaitPt.width + 10, Assets.imgWaitPt.height + 10, Assets.imgWaitPt, panel);
		waitField = new UIScrollField(90, btnWait.getY(), 120, btnWait.getHeight(), "", panel);
		speedField = new UIScrollField(waitField.getX(), waitField.getY() + 40, waitField.getWidth(), waitField.getHeight(), "Speed", panel);
		angleField = new UIScrollField(speedField.getX(), speedField.getY(), speedField.getWidth(), speedField.getHeight(), "Angle", panel);
		distField = new UITextField(10, 60, 200, 70, panel);
	}

	public void init(GameContainer ac) {
	}

	public void update(GameContainer ac, float dt) {

		if (ac.getInput().isKeyPressed(KeyEvent.VK_B)) {
			showRobotBounds = !showRobotBounds;
		}

		if (n != null) {
			Plotter plotter = parent.getMap().getPlotter();
			double angle = parent.selectedNode != 0 ? n.getAngle() : ((NodeEditor) parent.getComponent("nodeeditor")).getAngleField().getValue();
			angle += 90;
			robotPoints[0] = new Node(rotatePoint(n.x - plotter.xToPixels(ROBOT_WIDTH_BUMPERS_INCHES) / 2, n.y - plotter.yToPixels(ROBOT_HEIGHT_BUMPERS_INCHES) / 2, new Point(n.x, n.y), angle), plotter);
			robotPoints[1] = new Node(rotatePoint(n.x - plotter.xToPixels(ROBOT_WIDTH_BUMPERS_INCHES) / 2, n.y + plotter.yToPixels(ROBOT_HEIGHT_BUMPERS_INCHES) / 2, new Point(n.x, n.y), angle), plotter);
			robotPoints[2] = new Node(rotatePoint(n.x + plotter.xToPixels(ROBOT_WIDTH_BUMPERS_INCHES) / 2, n.y + plotter.yToPixels(ROBOT_HEIGHT_BUMPERS_INCHES) / 2, new Point(n.x, n.y), angle), plotter);
			robotPoints[3] = new Node(rotatePoint(n.x + plotter.xToPixels(ROBOT_WIDTH_BUMPERS_INCHES) / 2, n.y - plotter.yToPixels(ROBOT_HEIGHT_BUMPERS_INCHES) / 2, new Point(n.x, n.y), angle), plotter);

			int delta = ac.getInput().isKeyDown(KeyEvent.VK_SHIFT) ? 8 : 1;
			if (ac.getInput().isKeyPressed(KeyEvent.VK_LEFT)) {
				n.x -= delta;
				if(parent.selectedNode > 0) {
					n.setAngle(getAngle(parent.nodes.get(parent.selectedNode - 1), n));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_RIGHT)) {
				n.x += delta;
				if(parent.selectedNode > 0) {
					n.setAngle(getAngle(parent.nodes.get(parent.selectedNode - 1), n));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_UP)) {
				n.y -= delta;
				if(parent.selectedNode > 0) {
					n.setAngle(getAngle(parent.nodes.get(parent.selectedNode - 1), n));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_DOWN)) {
				n.y += delta;
				if(parent.selectedNode > 0) {
					n.setAngle(getAngle(parent.nodes.get(parent.selectedNode - 1), n));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_PAGE_UP)) {
				if (parent.selectedNode == 0) {
					n.setAngle(Math.max(n.getAngle() - 15, -180));
				} else if (parent.selectedNode > 0) {
					n.setSpeed(Math.min(n.getSpeed() + .1f, 1.0f));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_PAGE_DOWN)) {
				if (parent.selectedNode == 0) {
					n.setAngle(Math.min(n.getAngle() + 15, 180));
				} else if (parent.selectedNode > 0) {
					n.setSpeed(Math.max(n.getSpeed() - .1f, 0.01f));
				}
			} else if (ac.getInput().isKeyPressed(KeyEvent.VK_DELETE)) {
				parent.nodes.setSize(parent.selectedNode);
				parent.getMap().getPlotter().setSelectedNode(-1);
				return;
			}
			if (ac.getInput().isKeyDown(KeyEvent.VK_PERIOD)) {
				eastWallDist = westWallDist = southWallDist = northWallDist = Double.MAX_VALUE;
				for (int i = 0; i < robotPoints.length; i++) {
					eastWallDist = Math.min(parent.getMap().getPlotter().MAP_WIDTH_INCHES - robotPoints[i].toInches()[0], eastWallDist);
					westWallDist = Math.min(robotPoints[i].toInches()[0], westWallDist);
					southWallDist = Math.min(parent.getMap().getPlotter().MAP_HEIGHT_INCHES - robotPoints[i].toInches()[1], southWallDist);
					northWallDist = Math.min(robotPoints[i].toInches()[1], northWallDist);
				}
			} else {
				speedField.setValue(Math.min(Math.max(speedField.getDeltaY() * 0.01 + n.getSpeed(), 0.01), 1.0));
				if (ac.getInput().isButtonReleased(1)) {
					n.setSpeed((float) speedField.getValue());
				}
				angleField.setValue(Math.min(Math.max(angleField.getDeltaY() + n.getAngle(), -180), 180));
				if (ac.getInput().isButtonReleased(1)) {
					n.setAngle(angleField.getValue());
				}
				waitField.setValue(Math.max(waitField.getDeltaY() * 0.01 + n.getWaitTime(), 0.01));
				if (ac.getInput().isButtonReleased(1)) {
					n.setWaitTime((float) waitField.getValue());
				}
				if (btnWait.isDown()) {
					waitField.update(ac, dt);
				}
				if (parent.selectedNode == 0) {
					angleField.update(ac, dt);
				} else if (parent.selectedNode > 0) {
					speedField.update(ac, dt);
				}

				btnShoot.update(ac, dt);
				btnWait.update(ac, dt);

				n.setShoot(btnShoot.isDown());
				if (!btnWait.isDown()) {
					n.setWaitTime(0.0f);
					waitField.reset();
				}
			}
		}

	}

	public void render(GameContainer ac, Renderer r) {
		panel.render(r);

		if (n != null) {
			if (parent.getSelectedNode() != null && showRobotBounds) {

				int color = 0xffffffff;
				if (!fieldContainsRobot(robotPoints))
					color = 0xffff0000;
				for (int i = 0; i < 4; i++) {
					r.drawLine(robotPoints[i].x, robotPoints[i].y, robotPoints[(i + 1) % 4].x, robotPoints[(i + 1) % 4].y, color);
				}
			}
			// NODE INFORMATION \\
			String nx = "";
			String ny = "";
			if (ac.getInput().isKeyDown(KeyEvent.VK_SLASH)) {
				nx = String.valueOf(n.x);
				ny = String.valueOf(n.y);
			} else {
				DecimalFormat df = new DecimalFormat("0.00");
				nx = df.format(n.toInches()[0]);
				ny = df.format(n.toInches()[1]);
			}
			if (parent.selectedNode == 0) {
				r.drawString("Starting Node: (" + nx + ", " + ny + ")", 0xffffffff, x + 30, y + 40, Font.LEFT, Font.MIDDLE);
				r.drawImage(Assets.imgRobotPt, x + 15 - Assets.imgRobotPt.width / 2, y + 40 - Assets.imgRobotPt.height / 2);
			} else {
				r.drawString("Node " + parent.selectedNode + ": (" + nx + ", " + ny + ")", 0xffffffff, x + 30, y + 40, Font.LEFT, Font.MIDDLE);
				r.drawImage(Assets.imgMovePt, x + 15 - Assets.imgMovePt.width / 2, y + 40 - Assets.imgMovePt.height / 2);
			}
			if (ac.getInput().isKeyDown(KeyEvent.VK_PERIOD)) {
				distField.render(r);
				r.drawString("Distance From:", 0xffffffff, distField.getX() + x + 5, distField.getY() + y + 5);
				DecimalFormat df = new DecimalFormat("0.00");
				r.drawString((eastWallDist < westWallDist ? "East Wall: " + df.format(eastWallDist) : "West Wall: " + df.format(westWallDist)) + " inches", eastWallDist < 0 || westWallDist < 0 ? 0xffff0000 : 0xffffffff,
						distField.getX() + x + 10, distField.getY() + y + 25);
				r.drawString((northWallDist < southWallDist ? "North Wall: " + df.format(northWallDist) : "South Wall: " + df.format(southWallDist)) + " inches", northWallDist < 0 || southWallDist < 0 ? 0xffff0000 : 0xffffffff,
						distField.getX() + x + 10, distField.getY() + y + 45);

			} else {
				btnShoot.render(r);
				btnWait.render(r);
				if (parent.selectedNode > 0) {
					speedField.render(r);
				} else if (parent.selectedNode == 0) {
					angleField.render(r, "degrees");
				}
				if (btnWait.isDown()) {
					waitField.render(r, "seconds");
				}
			}
		}
	}

	private Point rotatePoint(double x, double y, Point c, double angle) {
		double theta = Math.toRadians(angle);
		double tempX = x - c.x;
		double tempY = y - c.y;
		double rotatedX = tempX * Math.cos(theta) - tempY * Math.sin(theta);
		double rotatedY = tempX * Math.sin(theta) + tempY * Math.cos(theta);
		return new Point((int) rotatedX + c.x, (int) rotatedY + c.y);
	}

	public void selectedNodeChanged() {
		n = parent.getSelectedNode();
		if (n != null) {
			btnShoot.setDown(n.isShoot());
			btnWait.setDown(n.getWaitTime() > 0.0f);
		}
	}

	private boolean fieldContainsNode(Node n) {
		Plotter plotter = parent.getMap().getPlotter();
		return plotter.getFieldBounds().contains(n.toPoint()) && !plotter.getPassageBounds().contains(n.toPoint());
	}

	private boolean fieldContainsRobot(Node[] corners) {
		for (int i = 0; i < corners.length; i++) {
			if (!fieldContainsNode(corners[i])) {
				return false;
			}
		}
		return true;
	}

	public UIScrollField getWaitField() {
		return waitField;
	}

	public UIScrollField getSpeedField() {
		return speedField;
	}

	public UIScrollField getAngleField() {
		return angleField;
	}
	
	private double getAngle(Node n0, Node n1) {
		return Math.toDegrees(Math.atan2(n1.y - n0.y, n1.x - n0.x));
	}
}