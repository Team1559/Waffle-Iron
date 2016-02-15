package com.auto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

import com.auto.components.ControlsPane;
import com.auto.components.Map;
import com.auto.components.Plotter;
import com.main.GameContainer;

public class WaffleExporter {

	private static final Node POINT_TOWER = new Node(503, 88);

	public static void export(AutoContainer auto, GameContainer ac) {
		Plotter plotter = auto.getMap().getPlotter();
		POINT_TOWER.setPlotter(plotter);
		@SuppressWarnings("unchecked")
		Vector<Node> nodes = (Vector<Node>) auto.nodes.clone();
		List<String> instructions = new ArrayList<String>();
		instructions.add("<<START>>");
		String startLights = ((ControlsPane) auto.getComponent("controlspane")).getStartLightPattern();
		if (startLights != "None") {
			instructions.add("LIGHTS " + startLights);
		}
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (i >= 1) {
				double dist = getDistanceInches(nodes.get(i - 1), n);
				double dAngle = normalizeAngle(n.getAngle());
				if (dist > 0) {
					instructions.add("TURN " + dAngle);
					int defenseBoundary = plotter.getOuterWorksBounds().y + plotter.getOuterWorksBounds().height / 2;
					if ((n.y > defenseBoundary && auto.nodes.get(i - 1).y < defenseBoundary) || (n.y < defenseBoundary && auto.nodes.get(i - 1).y > defenseBoundary)) {
						
						instructions.add("GO distance=\"" + getDistanceInches(nodes.get(i - 1), new Node(nodes.get(i - 1).x, plotter.getOuterWorksBounds().y + plotter.getOuterWorksBounds().height, plotter)) + "\" speed=\"" + new DecimalFormat("0.00").format(n.getSpeed()) + "\"");
						if (n.x < plotter.BORDER_12) {
							instructions.add("DEFENSE id=\"low bar\" active=\"true\"");
						} else if (n.x < plotter.BORDER_23) {
							instructions.add("DEFENSE id=\"" + ((Map) auto.getComponent("map")).getDefenses()[1].getName().toLowerCase() + "\" active=\"true\"");
						} else if (n.x < plotter.BORDER_34) {
							instructions.add("DEFENSE id=\"" + ((Map) auto.getComponent("map")).getDefenses()[2].getName().toLowerCase() + "\" active=\"true\"");
						} else if (n.x < plotter.BORDER_45) {
							instructions.add("DEFENSE id=\"" + ((Map) auto.getComponent("map")).getDefenses()[3].getName().toLowerCase() + "\" active=\"true\"");
						} else {
							instructions.add("DEFENSE id=\"" + ((Map) auto.getComponent("map")).getDefenses()[4].getName().toLowerCase() + "\" active=\"true\"");
						}
						instructions.add("GO distance=\"" + getDistanceInches(n, new Node(nodes.get(i - 1).x, plotter.getOuterWorksBounds().y, plotter)) + "\" speed=\"" + new DecimalFormat("0.00").format(n.getSpeed()) + "\"");
					} else {
						instructions.add("GO distance=\"" + dist + "\" speed=\"" + new DecimalFormat("0.00").format(n.getSpeed()) + "\"");
					}
				}
			}
			if (n.isShoot()) {
				double shootTurnAngle = normalizeAngle(getAngle(n, POINT_TOWER));
				if (shootTurnAngle != 0) {
					instructions.add("TURN " + shootTurnAngle);
				}
				instructions.add("SHOOT");
			}
			if (n.getWaitTime() > 0.0f) {
				instructions.add("WAIT " + n.getWaitTime());
			}
		}
		String endLights = ((ControlsPane) auto.getComponent("controlspane")).getEndLightPattern();
		if (endLights != "None") {
			instructions.add("LIGHTS " + endLights);
		}
		instructions.add("<<STOP>>");
		JFileChooser chooser = new JFileChooser();
		int i = chooser.showOpenDialog(ac.getWindow());
		Path file = null;
		if (i == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile().toPath();
			try {
				Files.write(file, instructions, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private static double normalizeAngle(double angle) {
		double newAngle = angle;
		while (newAngle < -180)
			newAngle += 360;
		while (newAngle > 180)
			newAngle -= 360;
		return newAngle;
	}

	private static double getAngle(Node n0, Node n1) {
		return Math.toDegrees(Math.atan2(n1.toInches()[1] - n0.toInches()[1], n1.toInches()[0] - n0.toInches()[0]));
	}

	private static double getDistanceInches(Node n0, Node n1) {
		return Math.abs(Math.sqrt(Math.pow(n0.toInches()[0] - n1.toInches()[0], 2) + Math.pow(n0.toInches()[1] - n1.toInches()[1], 2)));
	}
}