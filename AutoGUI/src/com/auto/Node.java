package com.auto;

import java.awt.Point;

import com.auto.components.Plotter;

public class Node {

	public int x = -1, y = -1;
	private boolean shoot;
	private float waitTime;
	private float speed;
	private double angle;
	private Plotter plotter;

	/**
	 * The toInches() method will not work with this constructor.
	 */
	public Node(Point p, Plotter plotter) {
		this(p.x, p.y, plotter);
	}
	
	public Node(int x, int y) {
		this(x, y, null);
	}

	public Node(int x, int y, Plotter plotter) {
		this.x = x;
		this.y = y;
		this.plotter = plotter;
		speed = 0.5f;
		waitTime = 0.0f;
		shoot = false;
		angle = -90;
	}

	public void setShoot(boolean b) {
		this.shoot = b;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setWaitTime(float f) {
		this.waitTime = f;
	}

	public float getWaitTime() {
		return waitTime;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setAngle(double d) {
		this.angle = d;
	}

	public double getAngle() {
		return angle;
	}

	/**
	 * Convert this node's pixel coordinates into inches. Access x with toInches()[0] and y with toInches()[1]
	 * 
	 * @return x and y stored in a double array.
	 */
	public double[] toInches() {
		if (plotter != null) {
			double ix = plotter.xToInches(x);
			double iy = plotter.yToInches(y);
			return new double[] { ix, iy };
		} else {
			return null;
		}
	}
	
	public Point toPoint() {
		return new Point(x, y);
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
	}
}