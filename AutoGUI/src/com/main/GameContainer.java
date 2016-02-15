package com.main;

import java.awt.event.KeyEvent;

import com.graphics.Renderer;
import com.input.Input;

public class GameContainer implements Runnable {

	private int width = 320;
	private int height = 240;
	private float scale = 2.0f;
	private String title = "AutoGUI (\"The Waffle Iron\")";

	private double frameCap = 1.0 / 60.0;

	private Thread thread;
	private boolean isRunning = false;

	private Window window;
	private GameFrame game;
	private Renderer renderer;
	private Input input;

	// Rendering options
	private boolean lightEnabled = false;
	private boolean dynamicLights = false;
	private boolean clearScreen = false;
	private boolean lockFramerate = false;
	private boolean debug = false;

	public GameContainer(GameFrame game) {
		this.game = game;
	}

	public void start() {
		if (isRunning)
			return;

		// INITIALIZE ENGINE COMPONENTS \\
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);

		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		isRunning = true;

		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		double frameTime = 0;
		int frames = 1;
		int fps = 0;

		game.init(this);

		while (isRunning) {
			boolean render = !lockFramerate;
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;

			unprocessedTime += passedTime;
			frameTime += passedTime;

			while (unprocessedTime >= frameCap) {
				if (input.isKeyPressed(KeyEvent.VK_F2)) {
					debug = !debug;
				}
				// UPDATE \\
				game.update(this, (float) frameCap);
				input.update();

				unprocessedTime -= frameCap;
				render = true;

				if (frameTime >= 1) {
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
			}

			if (render) {
				// RENDER \\
				if (clearScreen) {
					renderer.clear();					
				}

				game.render(this, renderer);

				if (lightEnabled || dynamicLights) {
					renderer.drawLightArray();
					renderer.flushMaps();
				}
				if (debug) {
					renderer.setTranslate(false);
					renderer.drawString("FPS - " + fps, 0xffffffff, 0, 0);
					renderer.setTranslate(true);
				}

				window.updateImage();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		cleanUp();
	}

	public void stop() {
		if (!isRunning)
			return;
		isRunning = false;
	}

	public void cleanUp() {
		window.cleanUp();
	}

	public void setFrameCap(int i) {
		this.frameCap = 1.0 / i;
	}

	public Window getWindow() {
		return window;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public String getTitle() {
		return title;
	}

	public boolean isDynamicLights() {
		return dynamicLights;
	}

	public void setDynamicLights(boolean dynamicLights) {
		this.dynamicLights = dynamicLights;
	}

	public boolean isLightEnabled() {
		return lightEnabled;
	}

	public void setLightEnabled(boolean lightEnabled) {
		this.lightEnabled = lightEnabled;
	}

	public boolean isClearScreen() {
		return clearScreen;
	}

	public void setClearScreen(boolean clearScreen) {
		this.clearScreen = clearScreen;
	}

	public boolean isLockFramerate() {
		return lockFramerate;
	}

	public void setLockFramerate(boolean lockFramerate) {
		this.lockFramerate = lockFramerate;
	}

	public GameFrame getGame() {
		return game;
	}

	public Input getInput() {
		return input;
	}
}