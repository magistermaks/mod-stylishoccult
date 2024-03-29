package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
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

	@Override
	public boolean generate(FeatureContext<OreFeatureConfig> context) {
		boolean generated = super.generate(context);
		return generated;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
        return configure( new OreFeatureConfig(
					OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES,
					ModBlocks.STONE_FLESH.getDefaultState(),
					StylishOccult.SETTING.flesh_stone_vain_size ))   // vein size
                .decorate(Decorator.RANGE.configure( new RangeDecoratorConfig(BiasedToBottomHeightProvider.create(
						YOffset.aboveBottom(0), YOffset.aboveBottom(100), 20
				))))
                .spreadHorizontally()
                .repeat(StylishOccult.SETTING.flesh_stone_vain_count);    // number of veins per chunk
    }

}
