package net.darktree.stylishoccult.worldgen.processor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class SimpleStructureProcessor extends StructureProcessor {

	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos1, BlockPos pos2, Structure.StructureBlockInfo info1, Structure.StructureBlockInfo info2, StructurePlacementData data) {
		final BlockState state = process(data.getRandom(info2.pos), info2.state.getBlock(), info2.state);
		return state != null ? new Structure.StructureBlockInfo(info2.pos, state, info2.nbt) : info2;
	}

	@Nullable
	public abstract BlockState process(Random random, Block block, BlockState state);

	/**
	 * Utility method for copying state of one stair block onto another
	 */
	protected final BlockState copyStair(BlockState source, Block block) {
		return block.getDefaultState()
				.with(StairsBlock.FACING, source.get(StairsBlock.FACING))
				.with(StairsBlock.HALF, source.get(StairsBlock.HALF))
				.with(StairsBlock.SHAPE, source.get(StairsBlock.SHAPE));
	}

	/**
	 * Utility method for copying state of one slab block onto another
	 */
	protected final BlockState copySlab(BlockState source, Block block) {
		return block.getDefaultState()
				.with(SlabBlock.TYPE, source.get(SlabBlock.TYPE));
	}

}
