package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.config.SimpleConfig;

public class StructureConfig {

	// 'depth' specifies how big the structure can get, affects only recursive structures, such as sanctum
	public final int spacing, separation, salt, depth;

	public StructureConfig(int spacing, int separation, int salt, int depth) {
		this.spacing = spacing;
		this.separation = separation;
		this.salt = salt;
		this.depth = depth;

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

	public StructureConfig(SimpleConfig config, String key, int spacing, int separation, int salt, int depth) {
		this(
				config.getOrDefault(key + ".spacing", spacing),
				config.getOrDefault(key + ".separation", separation),
				config.getOrDefault(key + ".salt", salt),
				config.getOrDefault(key + ".depth", depth)
		);
	}

	private void fail(String details) {
		StylishOccult.LOGGER.error("Tried to create invalid structure config: {spacing={} separation={} salt={} depth={}}", spacing, separation, salt, depth);
		throw new RuntimeException("Invalid structure configuration, " + details + "!");
	}

}
