package net.darktree.stylishoccult;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.config.Settings;
import net.darktree.stylishoccult.effects.ModEffects;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.event.LookAtEvent;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.tags.ModTags;
import net.darktree.stylishoccult.worldgen.ModFeatures;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StylishOccult implements ModInitializer, ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Stylish Occult");
    public static final String NAMESPACE = "stylish_occult";
    public static final Settings SETTINGS = new Settings();

    public static void debug( String message ) {
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info(message);
        }
    }

    @Override
    public void onInitialize() {

        if(!FabricLoader.getInstance().getConfigDir().resolve("stylish_occult_116_nowarn.lock").toFile().exists()) {
            LOGGER.fatal("YOU ARE USING A NO LONGER SUPPORTED AND INCOMPLETE VERSION OF STYLISH OCCULT,");
            LOGGER.fatal("MANY FEATURES MAY BE MISSING OR UNSTABLE! YOU AT YOUR OWN RISK!");
            LOGGER.fatal("Consider switching to a 1.17 version of Stylish Occult, if you would like to");
            LOGGER.fatal("silence this warning create a file 'stylish_occult_116_nowarn.lock' in the game's config directory");
        }

        Sounds.init();
        ModBlocks.init();
        Network.init();
        ModItems.init();
        LootTables.init();
        Particles.init();
        BlockEntities.init();
        ModEntities.init();
        ModFeatures.init();
        ModEffects.init();
        ModTags.init();
        LookAtEvent.init();
    }

    @Override
    public void onInitializeClient() {
        ModBlocks.clientInit();
        Network.clientInit();
        ModItems.clientInit();
        Particles.clientInit();
        ModEntities.clientInit();
        LOGGER.info( "Additional sound effects from https://www.zapsplat.com" );
    }
}
