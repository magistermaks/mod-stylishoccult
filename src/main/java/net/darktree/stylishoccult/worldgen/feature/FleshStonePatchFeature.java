package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

public class FleshStonePatchFeature extends OreFeature implements SimpleFeatureProvider {

	public FleshStonePatchFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<OreFeatureConfig> context) {
		boolean generated = super.generate(context);
		if (generated) this.debugWrite(context.getOrigin());
		return generated;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
        return new ConfiguredFeature<>(this,
				new OreFeatureConfig(
					OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
					ModBlocks.STONE_FLESH.getDefaultState(),
					StylishOccult.SETTING.flesh_stone_vain_size, // vein size
					0.0f) // discard on air chance
				);
    }

	@Override
	public PlacedFeature placed(RegistryEntry<ConfiguredFeature<?, ?>> configured) {
		return new PlacedFeature(
				configured,
				Arrays.asList(
						CountPlacementModifier.of(StylishOccult.SETTING.flesh_stone_vain_count), // number of veins per chunk
						SquarePlacementModifier.of(), // spreading horizontally
						HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(100)),
						BiomePlacementModifier.of()
				));
	}

}
