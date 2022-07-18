package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public interface SimpleFeatureProvider {

	ConfiguredFeature<?, ?> configure();

	default void debugWrite(BlockPos pos) {
		StylishOccult.debug("Generated feature '" + this.getClass().getSimpleName() + "' generated at: " + pos.toShortString());
	}

}
