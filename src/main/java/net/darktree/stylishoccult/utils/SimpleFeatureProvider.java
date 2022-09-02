package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public interface SimpleFeatureProvider {

	ConfiguredFeature<?, ?> configure();
	List<PlacementModifier> modifiers();

	default PlacedFeature placed(RegistryEntry<ConfiguredFeature<?, ?>> configured) {
		return new PlacedFeature(configured, modifiers());
	}

	default void debugWrite(BlockPos pos) {
		String xyz = pos.getX() + " " + pos.getY() + " " + pos.getZ();
		StylishOccult.debug("Generated feature '" + this.getClass().getSimpleName() + "' generated at: " + xyz);
	}

}
