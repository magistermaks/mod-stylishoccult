package net.darktree.stylishoccult.worldgen;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

public class ModFeatures {

    private static <F extends Feature<? extends FeatureConfig>> F registerFeature(String name, F feature) {
        return Registry.register(Registry.FEATURE, new ModIdentifier(name), feature);
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        Identifier id = new ModIdentifier(name);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
        return RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, id);
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> register( String str, SimpleFeature feature ) {
        registerFeature( str, (Feature<?>) feature);
        return registerConfiguredFeature( str, feature.configure() );
    }

    public static void init() {

        // Lava Demon Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and( BiomeSelectors.categories( Biome.Category.OCEAN ).negate() ),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                register( "lava_demon_feature", new LavaDemonFeature( DefaultFeatureConfig.CODEC ) )
        );

        // Flesh Patch Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                register( "flesh_patch_feature", new FleshPatchFeature( OreFeatureConfig.CODEC ) )
        );

        // Nether Grass/Fern Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.RAW_GENERATION,
                register( "nether_grass_patch_feature", new NetherGrassFeature( DefaultFeatureConfig.CODEC ) )
        );

        // Nether Grass/Fern Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.RAW_GENERATION,
                register( "spark_vent_feature", new SparkVentFeature( DefaultFeatureConfig.CODEC ) )
        );

        // Random Urn Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.RAW_GENERATION,
                register( "urn_feature", new UrnFeature( DefaultFeatureConfig.CODEC ) )
        );

        // Runic Walls Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.RAW_GENERATION,
                register( "runic_wall_feature", new WallsFeature( DefaultFeatureConfig.CODEC ) )
        );

        // Flash Altar Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.RAW_GENERATION,
                register( "flesh_altar_feature", new FleshAltarFeature( DefaultFeatureConfig.CODEC ) )
        );

    }

}
