package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DeepslateStructureProcessor  extends StructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("deepslate", DeepslateStructureProcessor::new);

	Block[] BRICKS = {Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS};
	Block[] SLAB = {Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.AIR, Blocks.COBBLED_DEEPSLATE_SLAB};
	Block[] WALLS = {Blocks.COBBLED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.DEEPSLATE_BRICK_WALL, Blocks.AIR};

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos1, BlockPos pos2, Structure.StructureBlockInfo info1, Structure.StructureBlockInfo info2, StructurePlacementData data) {
		Random random = data.getRandom(info2.pos);
		BlockState state = info2.state;
		BlockPos pos = info2.pos;

		BlockState newState = null;

		if (state.getBlock() == Blocks.DEEPSLATE_BRICKS) newState = RandUtils.getArrayEntry(BRICKS, random).getDefaultState(); else
		if (state.getBlock() == Blocks.COBBLED_DEEPSLATE_SLAB) newState = RandUtils.getArrayEntry(SLAB, random).getDefaultState(); else
		if (state.getBlock() == Blocks.POLISHED_DEEPSLATE_WALL) newState = RandUtils.getArrayEntry(WALLS, random).getDefaultState();

		return newState != null ? new Structure.StructureBlockInfo(pos, newState, info2.nbt) : info2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
