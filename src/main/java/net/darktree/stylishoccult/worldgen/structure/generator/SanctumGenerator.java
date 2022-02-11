package net.darktree.stylishoccult.worldgen.structure.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;

public class SanctumGenerator {

	public static final StructurePool POOL = StructurePools.register(new StructurePool(new ModIdentifier("sanctum"), new Identifier("empty"), ImmutableList.of(
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(StylishOccult.NAMESPACE + ":sanctum/upper", WorldGen.SANCTUM_PROCESSOR), 1)
	), StructurePool.Projection.RIGID));

	public static void init() {

	}

}
