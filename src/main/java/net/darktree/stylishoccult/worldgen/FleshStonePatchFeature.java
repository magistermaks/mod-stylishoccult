package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

public class FleshStonePatchFeature extends OreFeature implements SimpleFeatureProvider {

	public FleshStonePatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	public boolean generate(FeatureContext<OreFeatureConfig> context) {
		boolean generated = super.generate(context);
		if(generated) this.debugWrite(context.getOrigin());
		return generated;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
        return configure( new OreFeatureConfig(
					OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
					ModBlocks.STONE_FLESH.getDefaultState(),
					StylishOccult.SETTINGS.featureFleshStoneVainSize ))   // vein size
                .decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(BiasedToBottomHeightProvider.create(
						YOffset.aboveBottom(0), YOffset.aboveBottom(100), 20
				))))
                .spreadHorizontally()
                .repeat(3);    // number of veins per chunk
    }

}
