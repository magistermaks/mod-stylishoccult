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

public class StonehengeGenerator {

	public static final Identifier ID = new ModIdentifier("stonehenge");
	public static final StructurePool POOL = StructurePools.register(new StructurePool(ID, new Identifier("empty"), ImmutableList.of(
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(StylishOccult.NAMESPACE + ":stonehenge_1", WorldGen.STONE_PROCESSOR), 7),
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(StylishOccult.NAMESPACE + ":stonehenge_2", WorldGen.BLACKSTONE_PROCESSOR), 1),
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(StylishOccult.NAMESPACE + ":stonehenge_3", WorldGen.DEEPSLATE_PROCESSOR), 2)
	), StructurePool.Projection.TERRAIN_MATCHING));

	public static void init() {

	}

}
