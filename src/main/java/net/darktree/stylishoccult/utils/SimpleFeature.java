package net.darktree.stylishoccult.utils;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public abstract class SimpleFeature extends Feature<DefaultFeatureConfig> {

    public SimpleFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    public abstract ConfiguredFeature<?, ?> configure();

}
