package com.graphics;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.main.GameContainer;

public class Renderer {

	private static final int CLEAR_COLOR = 0xff131313;
	
	private GameContainer ac;

	private int width, height;
	private int[] pixels;
	private int[] lightMap;
	private ShadowType[] shadowMap;

	private int ambientLight = Pixel.getColor(1, 0.2f, 0.1f, 0.3f);

	private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();


	private int transX, transY;
	private boolean translate = true;

	public Renderer(GameContainer gc) {
		this.ac = gc;
		this.width = gc.getWidth();
		this.height = gc.getHeight();
		this.pixels = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		this.lightMap = new int[pixels.length];
		this.shadowMap = new ShadowType[pixels.length];
	}

	public void drawLightArray() {
		for (LightRequest lr : lightRequests) {
			drawLightRequest(lr.light, lr.x, lr.y);
		}
		lightRequests.clear();
	}

	public void drawLight(Light light, int offX, int offY) {
		if (ac.isDynamicLights() || ac.isLightEnabled()) {
			lightRequests.add(new LightRequest(light, offX, offY));
		}
	}

	private void drawLightRequest(Light light, int offX, int offY) {
		if (ac.isDynamicLights()) {
			for (int i = 0; i <= light.diameter; i++) {
				drawLightLine(light.radius, light.radius, i, 0, light, offX, offY);
				drawLightLine(light.radius, light.radius, i, light.diameter, light, offX, offY);
				drawLightLine(light.radius, light.radius, 0, i, light, offX, offY);
				drawLightLine(light.radius, light.radius, light.diameter, i, light, offX, offY);
			}
		} else {
			for (int y = 0; y < light.diameter; y++) {
				for (int x = 0; x < light.diameter; x++) {
					setLightMap(x + offX - light.radius, y + offY - light.radius, light.getLightValue(x, y));
				}
			}
		}
	}

	private void drawLightLine(int x0, int y0, int x1, int y1, Light light, int offX, int offY) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;

		float power = 1.0f;
		boolean hit = false;

		while (true) {
			if (light.getLightValue(x0, y0) == 0xff000000)
				break;

			int screenX = x0 - light.radius + offX;
			int screenY = y0 - light.radius + offY;

			if (power == 1) {
				setLightMap(screenX, screenY, light.getLightValue(x0, y0));
			} else {
				setLightMap(screenX, screenY, Pixel.getColorPower(light.getLightValue(x0, y0), power));
			}

			if (x0 == x1 && y0 == y1) {
				break;
			}

			if (getShadowMap(screenX, screenY) == ShadowType.TOTAL)
				break;
			else if (getShadowMap(screenX, screenY) == ShadowType.FADE)
				power -= 0.1f;
			else if (getShadowMap(screenX, screenY) == ShadowType.HALF && !hit) {
				hit = true;
				power /= 2;
			} else if (getShadowMap(screenX, screenY) == ShadowType.NONE && hit)
				hit = false;

			if (power <= 0.1f)
				break;
			e2 = 2 * err;
			if (e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	public void drawString(String text, int color, int offX, int offY) {
		drawString(text, color, offX, offY, Font.LEFT, Font.TOP);
	}
	
	public void drawString(String text, int color, int offX, int offY, int xAlign, int yAlign) {
		drawString(text, color, offX, offY, xAlign, yAlign, Font.STANDARD);
	}	
	
	public void drawString(String text, int color, int offX, int offY, int xAlign, int yAlign, Font font) {
		int offset = 0;

		offset -= Math.round(font.getStringSize(text) * (xAlign / 2.0f));

		for (int i = 0; i < text.length(); i++) {
			int unicode = text.codePointAt(i) - 32;
			for (int y = 1; y < font.image.height; y++) {
				for (int x = 0; x < font.widths[unicode]; x++) {
					if (font.image.pixels[(x + font.offsets[unicode]) + y * font.image.width] == 0xff000000) {
						int py = y + offY;
						py -= (font.image.height - 1) * (yAlign / 2.0f);
						setPixel(x + offX + offset, py, color, ShadowType.NONE);
					}
				}
			}
			offset += font.widths[unicode];
		}
	}

	public void drawImage(Image image, int offX, int offY) {
		for (int y = 0; y < image.height; y++) {
			for (int x = 0; x < image.width; x++) {
				setPixel(x + offX, y + offY, image.pixels[x + y * image.width], image.shadowType);
			}
		}
	}

	public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
		for (int y = 0; y < image.tileHeight; y++) {
			for (int x = 0; x < image.tileWidth; x++) {
				setPixel(x + offX, y + offY, image.pixels[(x + tileX * image.tileWidth) + (y + tileY * image.tileHeight) * image.width], image.shadowType);
			}
		}
	}

	public void fillRect(int offX, int offY, int width, int height, int color, ShadowType shadowType) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setPixel(x + offX, y + offY, color, shadowType);
			}
		}
	}

	public void drawRect(int offX, int offY, int width, int height, int color, ShadowType shadowType) {
		for (int x = 0; x <= width; x++) {
			setPixel(x + offX, offY, color, shadowType);
			setPixel(x + offX, offY + height, color, shadowType);
		}
		for (int y = 0; y <= height; y++) {
			setPixel(offX, y + offY, color, shadowType);
			setPixel(offX + width, y + offY, color, shadowType);
		}
	}

	// Bresenham Algorithm
	public void drawLine(int x0, int y0, int x1, int y1, int color) {
		int w = x1 - x0;
		int h = y1 - y0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			setPixel(x0, y0, color, ShadowType.NONE);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x0 += dx1;
				y0 += dy1;
			} else {
				x0 += dx2;
				y0 += dy2;
			}
		}
	}

	public void setPixel(int x, int y, int color, ShadowType shadowType) {
		float alpha = Pixel.getAlpha(color);
		if (alpha == 1) {
			if (translate) {
				x -= transX;
				y -= transY;
			}
			if (x < 0 || x >= width || y < 0 || y >= height || color == 0xffff00ff) {
				return;
			}
			pixels[x + y * width] = color;
			shadowMap[x + y * width] = shadowType;
		} else if (Pixel.getAlpha(color) == 0) {
			return;
		}
	}

	public void setLightMap(int x, int y, int color) {
		x -= transX;
		y -= transY;
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return;
		}
		lightMap[x + y * width] = Pixel.getMax(color, lightMap[x + y * width]);
	}

	public ShadowType getShadowMap(int x, int y) {
		x -= transX;
		y -= transY;
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return ShadowType.TOTAL;
		}
		return shadowMap[x + y * width];
	}

	public void flushMaps() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setPixel(x, y, Pixel.getLightBlend(pixels[x + y * width], lightMap[x + y * width], ambientLight), shadowMap[x + y * width]);
				lightMap[x + y * width] = ambientLight;
			}
		}
	}

	public void clear() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = CLEAR_COLOR;
			}
		}
	}

	// SETTERS \\

	public void setAmbientLight(int ambientLight) {
		this.ambientLight = ambientLight;
	}

	public void setTransX(int transX) {
		this.transX = transX;
	}

	public void setTransY(int transY) {
		this.transY = transY;
	}

	public void setTranslate(boolean b) {
		this.translate = b;
	}

	// WRAPPERS \\

	public void drawImage(Image image) {
		drawImage(image, 0, 0);
	}

	public void drawImageTile(ImageTile image, int tileX, int tileY) {
		drawImageTile(image, 0, 0, tileX, tileY);
	}

	public void drawLight(Light light) {
		drawLight(light, 0, 0);
	}

	public void fillRect(int offX, int offY, int width, int height, int color) {
		fillRect(offX, offY, width, height, color, ShadowType.NONE);
	}

	public void drawRect(int offX, int offY, int width, int height, int color) {
		drawRect(offX, offY, width, height, color, ShadowType.NONE);
	}
}