package net.darktree.stylishoccult;

import net.darktree.interference.MessageInjector;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.config.Settings;
import net.darktree.stylishoccult.effects.ModEffects;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.advancement.Criteria;
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
        Criteria.init();
    }

    @Override
    public void onInitializeClient() {
        ModBlocks.clientInit();
        Network.clientInit();
        ModItems.clientInit();
        Particles.clientInit();
        ModEntities.clientInit();

        MessageInjector.injectPlain("D" + "own with JSON!");
        MessageInjector.injectPlain("T" + "iny potato!");
        MessageInjector.injectPlain("S" + "tylish nightmare!");
        MessageInjector.injectPlain("T" + "he nightmare before breakfast!");
        MessageInjector.injectPlain("T" + "he wheels of progress frozen motionless...");
        MessageInjector.injectPlain("B" + "y the power of The Mixin Subsystem!");

        LOGGER.info("Additional sound effects from https://www.zapsplat.com");
    }
}
