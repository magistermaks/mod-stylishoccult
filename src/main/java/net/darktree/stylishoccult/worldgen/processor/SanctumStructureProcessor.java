package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.EyesBlock;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.tags.ModTags;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.structure.processor.StructureProcessorType;

import java.util.Random;

public class SanctumStructureProcessor extends SimpleStructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("sanctum", SanctumStructureProcessor::new);

	private static final Block[] BRICKS = {Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE};
	private static final Block[] STAIRS = {Blocks.BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS};
	private static final Block[] SLABS = {Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB};
	private static final Block[] RUBBLE = {Blocks.BLACKSTONE, Blocks.AIR};

	@Override
	public BlockState process(Random random, Block block, BlockState state) {
		if (block == Blocks.BLACK_WOOL) return RandUtils.getArrayEntry(RUBBLE, random).getDefaultState(); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICKS) return RandUtils.getBool(8f, random) ? getRune(random) : RandUtils.getArrayEntry(BRICKS, random).getDefaultState(); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS) return copyStair(state, RandUtils.getArrayEntry(STAIRS, random)); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_SLAB) return copySlab(state, RandUtils.getArrayEntry(SLABS, random)); else
		if (block == ModBlocks.URN && RandUtils.getBool(50f, random)) return Blocks.AIR.getDefaultState(); else
		if (block == Blocks.RED_CANDLE) return RandUtils.getBool(20f, random) ? Blocks.AIR.getDefaultState() : state.with(CandleBlock.CANDLES, RandUtils.rangeInt(1, 4, random)); else
		if (block == ModBlocks.EYES_FLESH || block == ModBlocks.WARTS_FLESH) return state.with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3, random)); else
		if (block == Blocks.BLACKSTONE && RandUtils.getBool(20f, random)) return ModBlocks.CRYSTALLINE_BLACKSTONE.getDefaultState();

		return null;
	}

	private BlockState getRune(Random random) {
		return ModTags.RUNES.getRandom(random).getDefaultState().with(RuneBlock.FROZEN, true);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
