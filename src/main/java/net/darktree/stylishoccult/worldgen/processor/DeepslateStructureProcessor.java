package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.StructureProcessorType;

import java.util.Random;

public class DeepslateStructureProcessor  extends SimpleStructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("deepslate", DeepslateStructureProcessor::new);

	private static final Block[] BRICKS = {Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS};
	private static final Block[] SLAB = {Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.AIR, Blocks.COBBLED_DEEPSLATE_SLAB};
	private static final Block[] WALLS = {Blocks.COBBLED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.DEEPSLATE_BRICK_WALL, Blocks.AIR};

	@Override
	public BlockState process(Random random, Block block, BlockState state) {
		if (block == Blocks.DEEPSLATE_BRICKS) return RandUtils.pickFromArray(BRICKS, random).getDefaultState();
		if (block == Blocks.COBBLED_DEEPSLATE_SLAB) return RandUtils.pickFromArray(SLAB, random).getDefaultState();
		if (block == Blocks.POLISHED_DEEPSLATE_WALL) return RandUtils.pickFromArray(WALLS, random).getDefaultState();

		return null;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
