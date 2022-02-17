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

		if(spacing <= separation) {
			printDebug();
			throw new RuntimeException("Invalid structure configuration, invalid spacing must be larger than separation!");
		}

		if(depth <= 0 || spacing <= 0) {
			printDebug();
			throw new RuntimeException("Invalid structure configuration, depth and spacing must be a larger than 0!");
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

	private void printDebug() {
		StylishOccult.LOGGER.error("Tried to create invalid structure config: {spacing={} separation={} salt={} depth={}}", spacing, separation, salt, depth);
	}

}
