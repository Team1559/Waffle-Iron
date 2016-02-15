package com.graphics;

public class Pixel {

	public static final int WHITE = 0xffffffff;
	public static final int RED = 0xffff0000;
	public static final int GREEN = 0xff00ff00;
	public static final int BLUE = 0xff0000ff;
	public static final int BLACK = 0xff000000;
	public static final int CYAN = 0xff00ffff;
	public static final int MAGENTA = 0xffff00ff;
	public static final int YELLOW = 0xffffff00;

	private static final int[] colors = { WHITE, RED, GREEN, BLUE, BLACK, CYAN, MAGENTA, YELLOW };

	public static int getRandomColor() {
		return colors[(int) (Math.random() * (colors.length - 1))];
	}

	public static float getAlpha(int color) {
		return (float) (0xff & (color >> 24)) / 255f;
	}

	public static float getRed(int color) {
		return (float) (0xff & (color >> 16)) / 255f;
	}

	public static float getGreen(int color) {
		return (float) (0xff & (color >> 8)) / 255f;
	}

	public static float getBlue(int color) {
		return (float) (0xff & color) / 255f;
	}

	public static int getColor(float a, float r, float g, float b) {
		return ((int) (a * 255 + 0.5) << 24 | (int) (r * 255 + 0.5) << 16 | (int) (g * 255 + 0.5) << 8 | (int) (b * 255 + 0.5));
	}

	public static int getColorPower(int color, float power) {
		// ignoring alpha, this can be changed
		return getColor(1, getRed(color) * power, getGreen(color) * power, getBlue(color) * power);
	}

	public static int getLightBlend(int color, int light, int ambientLight) {
		float r = Math.max(getRed(light), getRed(ambientLight));
		float g = Math.max(getGreen(light), getGreen(ambientLight));
		float b = Math.max(getBlue(light), getBlue(ambientLight));
		return getColor(1, r * getRed(color), g * getGreen(color), b * getBlue(color));
	}

	public static int getMax(int c0, int c1) {
		return getColor(1, Math.max(getRed(c0), getRed(c1)), Math.max(getGreen(c0), getGreen(c1)), Math.max(getBlue(c0), getBlue(c1)));
	}
}