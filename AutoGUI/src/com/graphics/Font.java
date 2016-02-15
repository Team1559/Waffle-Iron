package com.graphics;

public enum Font {

	STANDARD("/fonts/runescape.png"), MEDIEVAL("/fonts/alagard.png");

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	public static final int TOP = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;

	private static final int MARKER = 0xff0000ff;

	public final int NUM_CHARACTERS = 96;
	public int[] offsets = new int[NUM_CHARACTERS];
	public int[] widths = new int[NUM_CHARACTERS];
	public Image image;

	Font(String path) {
		image = new Image(path);

		int unicode = 0;

		// for (int x = 0; x < image.width; x++) {
		// int color = image.pixels[x];
		// if (color == START_MARKER) {
		// unicode++;
		// offsets[unicode] = x;
		// } else if (color == END_MARKER) {
		// widths[unicode] = x - offsets[unicode];
		// }
		// }
		for (int x = 0; x < image.width; x++) {
			int color = image.pixels[x];
			if (color == MARKER) {
				offsets[unicode] = x;
				if (unicode > 0) {
					widths[unicode - 1] = x - offsets[unicode - 1];
				}
				unicode++;
			}
		}
	}

	public int getStringSize(String s) {

		int offset = 0;
		for (int i = 0; i < s.length(); i++) {
			int unicode = s.codePointAt(i) - 32;
			offset += widths[unicode];
		}
		return offset;
	}
}