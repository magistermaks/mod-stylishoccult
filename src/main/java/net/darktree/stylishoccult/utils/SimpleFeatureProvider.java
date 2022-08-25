package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

public interface SimpleFeatureProvider {

	ConfiguredFeature<?, ?> configure();
	PlacedFeature placed(RegistryEntry<ConfiguredFeature<?, ?>> configured); // TODO make it return just hte array

	default void debugWrite(BlockPos pos) {
		StylishOccult.debug("Generated feature '" + this.getClass().getSimpleName() + "' generated at: " + pos.toShortString());
	}

}
