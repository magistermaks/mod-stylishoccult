package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlackstoneStructureProcessor extends StructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("blackstone", BlackstoneStructureProcessor::new);

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos1, BlockPos pos2, Structure.StructureBlockInfo info1, Structure.StructureBlockInfo info2, StructurePlacementData data) {
		Random random = data.getRandom(info2.pos);
		BlockState state = info2.state;
		BlockPos pos = info2.pos;

		BlockState newState = null;

		if (state.getBlock() == Blocks.RED_CANDLE) newState = permuteCandle(state, random);
		if (state.getBlock() == ModBlocks.URN && RandUtils.getBool(25f)) newState = Blocks.AIR.getDefaultState();

		return newState != null ? new Structure.StructureBlockInfo(pos, newState, info2.nbt) : info2;
	}

	private BlockState permuteCandle(BlockState source, Random random) {
		return source
				.with(CandleBlock.LIT, RandUtils.getBool(80f, random))
				.with(CandleBlock.CANDLES, RandUtils.rangeInt(1, 4, random));
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
