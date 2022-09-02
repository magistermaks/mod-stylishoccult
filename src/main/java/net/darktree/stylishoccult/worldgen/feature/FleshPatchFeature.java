package net.darktree.stylishoccult.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

public class FleshPatchFeature extends OreFeature implements SimpleFeatureProvider {

	public FleshPatchFeature(Codec<OreFeatureConfig> codec) {
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
					OreConfiguredFeatures.BASE_STONE_NETHER,
					ModBlocks.DEFAULT_FLESH.getDefaultState(),
					StylishOccult.SETTING.flesh_vain_size, // vein size
					0.0f) // discard on air chance
				);
	}

	@Override
	public List<PlacementModifier> modifiers() {
		return ImmutableList.of(
				CountPlacementModifier.of(StylishOccult.SETTING.flesh_vain_count),
				SquarePlacementModifier.of(),
				PlacedFeatures.BOTTOM_TO_TOP_RANGE,
				BiomePlacementModifier.of()
		);
	}

}

