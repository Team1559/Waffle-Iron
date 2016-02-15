package com.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private Canvas canvas;
	private BufferedImage image;
	private BufferStrategy bs;
	private Graphics g;

	public Window(GameContainer gc) {

		canvas = new Canvas();
		canvas.setSize(new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale())));

		setTitle(gc.getTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(canvas);
		pack();
		setLocationRelativeTo(null);
		// setResizable(false);
		setVisible(true);

		canvas.createBufferStrategy(3);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		setIconImage(new ImageIcon("res/waffle.png").getImage());
		canvas.requestFocus();
	}

	public void updateImage() {
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	public void cleanUp() {
		g.dispose();
		bs.dispose();
		image.flush();
		dispose();
	}

	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}
}