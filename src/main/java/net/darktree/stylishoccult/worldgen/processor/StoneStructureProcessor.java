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

public class StoneStructureProcessor extends StructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("stone", StoneStructureProcessor::new);

	Block[] BRICKS = {Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.COBBLESTONE};
	Block[] MOSSY_BRICKS = {Blocks.MOSSY_STONE_BRICKS, Blocks.GRASS_BLOCK, Blocks.MOSSY_COBBLESTONE};
	Block[] MOSSY_SLAB = {Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.ANDESITE_SLAB, Blocks.AIR, Blocks.AIR};
	Block[] SLAB = {Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.AIR, Blocks.ANDESITE_SLAB};
	Block[] STAIR = {Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.COBBLESTONE_STAIRS};
	Block[] STONE = {Blocks.STONE, Blocks.ANDESITE, Blocks.GRAVEL, Blocks.COBBLESTONE};

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos1, BlockPos pos2, Structure.StructureBlockInfo info1, Structure.StructureBlockInfo info2, StructurePlacementData data) {
		Random random = data.getRandom(info2.pos);
		BlockState state = info2.state;
		BlockPos pos = info2.pos;

		BlockState newState = null;

		if (state.getBlock() == Blocks.STONE_BRICKS) newState = RandUtils.getArrayEntry(BRICKS, random).getDefaultState(); else
		if (state.getBlock() == Blocks.MOSSY_STONE_BRICKS) newState = RandUtils.getArrayEntry(MOSSY_BRICKS, random).getDefaultState(); else
		if (state.getBlock() == Blocks.MOSSY_COBBLESTONE_SLAB) newState = RandUtils.getArrayEntry(MOSSY_SLAB, random).getDefaultState(); else
		if (state.getBlock() == Blocks.COBBLESTONE_SLAB) newState = RandUtils.getArrayEntry(SLAB, random).getDefaultState(); else
		if (state.getBlock() == Blocks.STONE) newState = RandUtils.getArrayEntry(STONE, random).getDefaultState(); else
		if (state.getBlock() == Blocks.STONE_BRICK_STAIRS) newState = copyStair(state, RandUtils.getArrayEntry(STAIR, random));
		if (state.getBlock() == ModBlocks.URN && RandUtils.getBool(80f)) newState = Blocks.AIR.getDefaultState();

		return newState != null ? new Structure.StructureBlockInfo(pos, newState, info2.nbt) : info2;
	}

	private BlockState copyStair(BlockState source, Block block) {
		return block.getDefaultState()
				.with(StairsBlock.FACING, source.get(StairsBlock.FACING))
				.with(StairsBlock.HALF, source.get(StairsBlock.HALF))
				.with(StairsBlock.SHAPE, source.get(StairsBlock.SHAPE))
				.with(StairsBlock.WATERLOGGED, source.get(StairsBlock.WATERLOGGED));
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
