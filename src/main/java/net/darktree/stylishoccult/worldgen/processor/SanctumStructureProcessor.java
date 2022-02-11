package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.EyesBlock;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.*;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SanctumStructureProcessor extends StructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("sanctum", SanctumStructureProcessor::new);

	Block[] BRICKS = {Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE};
	Block[] STAIRS = {Blocks.BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS};
	Block[] SLABS = {Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB};

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos1, BlockPos pos2, Structure.StructureBlockInfo info1, Structure.StructureBlockInfo info2, StructurePlacementData data) {
		Random random = data.getRandom(info2.pos);
		BlockState state = info2.state;
		BlockPos pos = info2.pos;

		BlockState newState = null;
		Block block = state.getBlock();

		if (block == Blocks.POLISHED_BLACKSTONE_BRICKS) newState = RandUtils.getArrayEntry(BRICKS, random).getDefaultState(); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS) newState = copyStair(state, RandUtils.getArrayEntry(STAIRS, random)); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_SLAB) newState = copySlab(state, RandUtils.getArrayEntry(SLABS, random)); else
		if (block == ModBlocks.URN && RandUtils.getBool(50f)) newState = Blocks.AIR.getDefaultState(); else
		if (block == Blocks.RED_CANDLE) newState = RandUtils.getBool(20f) ? Blocks.AIR.getDefaultState() : state.with(CandleBlock.CANDLES, RandUtils.rangeInt(1, 4, random)); else
		if (block == ModBlocks.EYES_FLESH || block == ModBlocks.WARTS_FLESH) newState = state.with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3, random)); else
		if (block == Blocks.BLACKSTONE && RandUtils.getBool(20f)) newState = ModBlocks.CRYSTALLINE_BLACKSTONE.getDefaultState();

		return newState != null ? new Structure.StructureBlockInfo(pos, newState, info2.nbt) : info2;
	}

	private BlockState copyStair(BlockState source, Block block) {
		return block.getDefaultState()
				.with(StairsBlock.FACING, source.get(StairsBlock.FACING))
				.with(StairsBlock.HALF, source.get(StairsBlock.HALF))
				.with(StairsBlock.SHAPE, source.get(StairsBlock.SHAPE));
	}

	private BlockState copySlab(BlockState source, Block block) {
		return block.getDefaultState()
				.with(SlabBlock.TYPE, source.get(SlabBlock.TYPE));
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
