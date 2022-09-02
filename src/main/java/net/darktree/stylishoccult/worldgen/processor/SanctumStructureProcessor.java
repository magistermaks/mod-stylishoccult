package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.lootboxes.LootBoxes;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.EyesBlock;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.tag.ModTags;
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
		if (block == Blocks.BLACK_WOOL) return RandUtils.pickFromArray(RUBBLE, random).getDefaultState(); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICKS) return RandUtils.nextBool(6f, random) ? getRune(random) : RandUtils.pickFromArray(BRICKS, random).getDefaultState(); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS) return copyStair(state, RandUtils.pickFromArray(STAIRS, random)); else
		if (block == Blocks.POLISHED_BLACKSTONE_BRICK_SLAB) return copySlab(state, RandUtils.pickFromArray(SLABS, random)); else
		if (block == LootBoxes.URN_BLOCK && RandUtils.nextBool(50f, random)) return Blocks.AIR.getDefaultState(); else
		if (block == Blocks.RED_CANDLE) return RandUtils.nextBool(20f, random) ? Blocks.AIR.getDefaultState() : state.with(CandleBlock.CANDLES, RandUtils.nextInt(1, 4, random)); else
		if (block == ModBlocks.EYES_FLESH || block == ModBlocks.WARTS_FLESH) return state.with(EyesBlock.SIZE, RandUtils.nextInt(1, 3, random)); else
		if (block == Blocks.BLACKSTONE && RandUtils.nextBool(20f, random)) return ModBlocks.CRYSTALLINE_BLACKSTONE.getDefaultState();

		return null;
	}

	private BlockState getRune(Random random) {
		return RandUtils.pickFromTag(ModTags.RUNES, random, Blocks.AIR).getDefaultState().with(RuneBlock.FROZEN, true);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
