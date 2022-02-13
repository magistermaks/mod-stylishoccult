package net.darktree.stylishoccult.worldgen.structure.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.darktree.stylishoccult.worldgen.processor.SanctumStructureProcessor;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class SanctumGenerator {

	public static final Identifier ID = new ModIdentifier("sanctum");

	private static final StructureProcessorList SANCTUM_PROCESSOR = WorldGen.addProcessorList("sanctum", ImmutableList.of(new SanctumStructureProcessor()));

	public static final StructurePool POOL = StructurePools.register(new StructurePool(ID, StructurePools.EMPTY.getValue(), ImmutableList.of(
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(ID + "/upper", SANCTUM_PROCESSOR), 1)
	), StructurePool.Projection.RIGID));

}
