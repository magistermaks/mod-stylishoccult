package net.darktree.stylishoccult.worldgen.structure.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.darktree.stylishoccult.worldgen.processor.BlackstoneStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.DeepslateStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.StoneStructureProcessor;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class StonehengeGenerator {

	public static final Identifier ID = new ModIdentifier("stonehenge");

	private static final StructureProcessorList STONE_PROCESSOR = WorldGen.addProcessorList("stone", ImmutableList.of(new StoneStructureProcessor()));
	private static final StructureProcessorList DEEPSLATE_PROCESSOR = WorldGen.addProcessorList("deepslate", ImmutableList.of(new DeepslateStructureProcessor()));
	private static final StructureProcessorList BLACKSTONE_PROCESSOR = WorldGen.addProcessorList("blackstone", ImmutableList.of(new BlackstoneStructureProcessor()));

	public static final StructurePool POOL = StructurePools.register(new StructurePool(ID, StructurePools.EMPTY.getValue(), ImmutableList.of(
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(ID + "/stone", STONE_PROCESSOR), 7),
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(ID + "/deepslate", DEEPSLATE_PROCESSOR), 2),
			new Pair<>(StructurePoolElement.ofProcessedLegacySingle(ID + "/blackstone", BLACKSTONE_PROCESSOR), 1)
	), StructurePool.Projection.TERRAIN_MATCHING));

}
