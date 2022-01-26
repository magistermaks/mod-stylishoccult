package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class FleshPatchFeature extends OreFeature implements SimpleFeature {

	public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return configure( new OreFeatureConfig(
				OreFeatureConfig.Rules.BASE_STONE_NETHER,
				ModBlocks.DEFAULT_FLESH.getDefaultState(),
				36 ))   // vein size
				.decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(
						0,      // bottom offset
						0,      // min y level
						250)))  // max y level
				.spreadHorizontally()
				.repeat(1);    // number of veins per chunk
	}

}

