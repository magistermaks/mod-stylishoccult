package net.darktree.stylishoccult.worldgen.structure.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;

public class SanctumGenerator {

	public static final StructurePool POOL;
	public static final Identifier TEST = new ModIdentifier("sanctum");

	static {
		POOL = StructurePools.register(
				new StructurePool(
						TEST,
						new Identifier("empty"),
						ImmutableList.of(
								new Pair<>(StructurePoolElement.ofLegacySingle(StylishOccult.NAMESPACE + ":sanctum_top"), 1)
						),
						StructurePool.Projection.RIGID
				)
		);
	}

	public static void init() {

	}

}
