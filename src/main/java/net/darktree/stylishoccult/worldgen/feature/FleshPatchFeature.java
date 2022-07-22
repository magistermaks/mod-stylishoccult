package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class FleshPatchFeature extends OreFeature implements SimpleFeatureProvider {

	public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<OreFeatureConfig> context) {
		if(RandUtils.getBool(80, context.getRandom())) {
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
					StylishOccult.SETTING.flesh_vain_size))   // vein size
				.decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(UniformHeightProvider.create(
						YOffset.aboveBottom(14), YOffset.aboveBottom(240)
				))))
				.spreadHorizontally()
				.repeat(StylishOccult.SETTING.flesh_vain_count);    // number of veins per chunk
	}

}

