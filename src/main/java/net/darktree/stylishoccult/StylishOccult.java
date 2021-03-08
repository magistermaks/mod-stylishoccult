package net.darktree.stylishoccult;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class StylishOccult implements ModInitializer, ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Stylish Occult");
    public static final String NAMESPACE = "stylish_occult";
    public static final Settings SETTINGS = new Settings();

    @Environment(EnvType.CLIENT)
    public static final Random RANDOM = new Random();

    public static void debug( String str ) {
        if( SETTINGS.debug ) {
            LOGGER.info( str );
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
        LookAtEvent.init();
    }

    @Override
    public void onInitializeClient() {
        ModBlocks.clientInit();
        Network.clientInit();
        ModItems.clientInit();
        Particles.clientInit();
        BlockEntities.clientInit();
        ModEntities.clientInit();
        LOGGER.info( "Sound effects for Stylish Occult obtained from https://www.zapsplat.com" );
    }
}
