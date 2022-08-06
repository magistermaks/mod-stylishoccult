package net.darktree.stylishoccult.data.generator;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class DataGenerator {

	private static boolean ENABLED = true;
	private static Path ROOT, ALTAR;

	public static void init() {
		ROOT = FabricLoader.getInstance().getGameDir().resolve("stylish_data");

		if (ENABLED) {
			if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
				StylishOccult.LOGGER.error("You fool! DataGenerator was left enabled outside of Development Environment! Disabling...");
				ENABLED = false;
			}

			StylishOccult.LOGGER.info("Running with data generation enabled!");

			// prepare directories
			ALTAR = prepare("altar");
		}
	}

	private static Path prepare(String name) {
		Path path = ROOT.resolve(name);
		path.toFile().mkdirs();
		return path;
	}

	public static void generateAltarRitual(RuneBlock block) {

	}

}
