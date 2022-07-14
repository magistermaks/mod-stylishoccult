package net.darktree.stylishoccult;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.config.Settings;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.effects.ModEffects;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.tags.ModTags;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StylishOccult implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Stylish Occult");
    public static final String NAMESPACE = "stylish_occult";
    public static final Settings SETTINGS = new Settings();

    public static void debug( String message ) {
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
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
