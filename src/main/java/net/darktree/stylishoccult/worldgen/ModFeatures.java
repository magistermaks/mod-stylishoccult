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
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

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
        registerFeature( str, feature );
        return registerConfiguredFeature( str, feature.configure() );
    }

    public static void init() {

        // Lava Demon Feature
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and( BiomeSelectors.categories( Biome.Category.OCEAN ).negate() ),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                register( "lava_demon_feature", new LavaDemonFeature( DefaultFeatureConfig.CODEC ) )
        );

    }

}
