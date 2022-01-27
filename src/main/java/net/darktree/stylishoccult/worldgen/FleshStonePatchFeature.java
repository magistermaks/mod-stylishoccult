package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;

public class FleshStonePatchFeature extends OreFeature implements SimpleFeature {

	public FleshStonePatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos pos, OreFeatureConfig oreFeatureConfig) {
		boolean generated = super.generate(structureWorldAccess, chunkGenerator, random, pos, oreFeatureConfig);
		if(generated) this.debugWrite(pos);
		return generated;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
        return configure( new OreFeatureConfig(
					OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
					ModBlocks.STONE_FLESH.getDefaultState(),
					StylishOccult.SETTINGS.featureFleshStoneVainSize ))   // vein size
                .decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(
                        0,      // bottom offset
                        0,      // min y level
                        60)))   // max y level
                .spreadHorizontally()
                .repeat(3);    // number of veins per chunk
    }

}
