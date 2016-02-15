package com.auto;

import com.graphics.Image;

public enum Defense {

	NULL(-1, "Undefined", null),
	PORTCULLIS(0, "Portcullis", "portcullis.png"),
	CHEVAL_DE_FRISE(1, "Cheval de Frise", "cheval.png"),
	MOAT(2, "Moat", "moat.png"),
	RAMPARTS(3, "Ramparts", "ramparts.png"),
	DRAWBRIDGE(4, "Drawbridge", "drawbridge.png"),
	SALLY_PORT(5, "Sally Port", "sallyport.png"),
	ROCK_WALL(6, "Rock Wall", "rockwall.png"),
	ROUGH_TERRAIN(7, "Rough Terrain","rough.png"),
	LOW_BAR(8, "Low Bar", "lowbar.png", false, true);

	private int m_id;
	private String m_name;
	private Image m_iso, m_birdsEye;

	Defense(int id, String name, String imgFile) {
		this(id, name, imgFile, true, true);
	}

	Defense(int id, String name, String imgFile, boolean iso, boolean birdsEye) {
		m_id = id;
		m_name = name;
		if (imgFile != null) {
			if (iso) {
				m_iso = new Image("/obstacles/iso/" + imgFile);
			}
			if (birdsEye) {
				m_birdsEye = new Image("/obstacles/birdsEye/" + imgFile);
			}
		}
	}

	public int getId() {
		return m_id;
	}

	public Image getIsoImage() {
		return m_iso;
	}

	public Image getBirdsEyeImage() {
		return m_birdsEye;
	}

	public static Defense getDefenseById(int id) {
		for (int i = 0; i < Defense.values().length; i++) {
			if (Defense.values()[i].getId() == id) {
				return Defense.values()[i];
			}
		}
		return Defense.NULL;
	}
	
	public String getName() {
		return m_name;
	}
}
