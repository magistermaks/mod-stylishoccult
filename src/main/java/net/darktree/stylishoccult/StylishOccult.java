package net.darktree.stylishoccult;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.config.Config;
import net.darktree.stylishoccult.config.Settings;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.effect.ModEffects;
import net.darktree.stylishoccult.entity.ModEntities;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.tag.ModTags;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StylishOccult implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("Stylish Occult");
	public static final String NAMESPACE = "stylish_occult";

	public static final Config<Settings> CONFIG = Config.of(Settings.class, NAMESPACE).request();
	public static Settings SETTING = CONFIG.getConfigured();

	@Deprecated
	public static void debug(String message) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.info(message);
		}
	}

	@Override
	public void onInitialize() {
		Sounds.init();
		ModBlocks.init();
		Network.init();
		ModItems.init();
		LootTables.init();
		Particles.init();
		BlockEntities.init();
		ModEntities.init();
		WorldGen.init();
		ModEffects.init();
		ModTags.init();
		Criteria.init();
		ResourceLoaders.init();
	}

}
