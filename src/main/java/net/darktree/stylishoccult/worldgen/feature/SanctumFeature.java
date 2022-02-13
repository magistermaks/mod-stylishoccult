package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Random;

public class SanctumFeature extends SimpleFeature<DefaultFeatureConfig> {

	public SanctumFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {



		return false;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return configure( new DefaultFeatureConfig() )
				.decorate( Decorator.COUNT_MULTILAYER.configure(
						new CountConfig(1)
				) );
	}
}
