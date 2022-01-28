package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class FleshPatchFeature extends OreFeature implements SimpleFeatureProvider {

	public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext context) {
		if(RandUtils.getBool(StylishOccult.SETTINGS.featureFleshVainChance, context.getRandom())) {
			boolean generated = super.generate(context);
			if (generated) this.debugWrite(context.getOrigin());
			return generated;
		}

		return false;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return configure( new OreFeatureConfig(
					OreFeatureConfig.Rules.BASE_STONE_NETHER,
					ModBlocks.DEFAULT_FLESH.getDefaultState(),
					StylishOccult.SETTINGS.featureFleshVainSize ))   // vein size
				.decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(UniformHeightProvider.create(
						YOffset.aboveBottom(14), YOffset.aboveBottom(240)
				))))
				.spreadHorizontally()
				.repeat(1);    // number of veins per chunk
	}

}

