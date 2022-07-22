package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.config.Config;

public class StructureConfig implements Config.ConfigProperty {

	@Config.Entry(min=1, restart=true)
	public int spacing;

	@Config.Entry(min=0, restart=true)
	public int separation;

	@Config.Entry(min=0, restart=true)
	public int salt;

	@Config.Entry(min=1, restart=true)
	public int depth;

	public StructureConfig(int spacing, int separation, int salt, int depth) {
		this.spacing = spacing;
		this.separation = separation;
		this.salt = salt;
		this.depth = depth;

		validate();
	}

	private void fail(String details) {
		StylishOccult.LOGGER.error("Tried to create invalid structure config: {spacing={} separation={} salt={} depth={}}", spacing, separation, salt, depth);
		throw new RuntimeException("Invalid structure configuration, " + details + "!");
	}

	@Override
	public void validate() {
		if (spacing <= separation) {
			fail("spacing must be larger than separation");
		}

		if (spacing <= 0) {
			fail("spacing must be a larger than 0");
		}

		if (depth <= 0) {
			fail("depth must be a larger than 0");
		}
	}

}
