package net.darktree.stylishoccult.render;

public record ArcConfig(
		float red, float green, float blue, float alpha, float redShift, float greenShift,
		float blueShift, float alphaShift, float jaggedness, float dumpingFactor, boolean split, int splitRarity,
		int splitFactor, float sjx, float sjy, float sjz, float convection, float separation, boolean detailed, float detailOne, float detailTwo) {

	public static ArcConfig ALTAR_ARC = new ArcConfig(
			0.7f, 0.2f, 0.1f, 0.5f, 1.1f, 1.0f, 1.0f, 0.6f, 0.2f, 0.8f, true, 20, 3, 3, 5, 3, 0.65f, 0.005f, true, 0.3f, 0.1f
	);

	public static ArcConfig ARCANE_SPELL = new ArcConfig(
			0.65f, 0.2f, 0.1f, 0.5f, 1.3f, 1.0f, 1.0f, 0.6f, 0.15f, 0.84f, true, 8, 5, 8, 8, 8, 0.0f, 0.005f, false, 0.0f, 0.0f
	);

}
