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

public class FleshPatchFeature extends OreFeature implements SimpleFeature {

	public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, OreFeatureConfig oreFeatureConfig) {
		boolean generated = super.generate(structureWorldAccess, chunkGenerator, random, blockPos, oreFeatureConfig);
		if(generated) StylishOccult.debug("Generated FleshPatchFeature at: " + blockPos.toString());
		return generated;
	}

		@Override
	public ConfiguredFeature<?, ?> configure() {
		return configure( new OreFeatureConfig(
				OreFeatureConfig.Rules.BASE_STONE_NETHER,
				ModBlocks.DEFAULT_FLESH.getDefaultState(),
				30 ))   // vein size
				.decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(
						10,     // bottom offset
						0,      // min y level
						240)))  // max y level
				.spreadHorizontally()
				.repeat(1);    // number of veins per chunk
	}

}

