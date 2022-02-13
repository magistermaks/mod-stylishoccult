package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.structure.processor.StructureProcessorType;

import java.util.Random;

public class BlackstoneStructureProcessor extends SimpleStructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("blackstone", BlackstoneStructureProcessor::new);

	@Override
	public BlockState process(Random random, Block block, BlockState state) {
		if (block == Blocks.RED_CANDLE) return permuteCandle(state, random);
		if (block == ModBlocks.URN && RandUtils.getBool(25f, random)) return Blocks.AIR.getDefaultState();

		return null;
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
