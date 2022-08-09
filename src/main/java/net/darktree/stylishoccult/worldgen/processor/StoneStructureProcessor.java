package net.darktree.stylishoccult.worldgen.processor;

import net.darktree.lootboxes.LootBoxes;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.StructureProcessorType;

import java.util.Random;

public class StoneStructureProcessor extends SimpleStructureProcessor {
	public static final StructureProcessorType<?> INSTANCE = WorldGen.addProcessorType("stone", StoneStructureProcessor::new);

	private static final Block[] BRICKS = {Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.COBBLESTONE};
	private static final Block[] MOSSY_BRICKS = {Blocks.MOSSY_STONE_BRICKS, Blocks.GRASS_BLOCK, Blocks.MOSSY_COBBLESTONE};
	private static final Block[] MOSSY_SLAB = {Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.ANDESITE_SLAB, Blocks.AIR, Blocks.AIR};
	private static final Block[] SLAB = {Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.AIR, Blocks.ANDESITE_SLAB};
	private static final Block[] STAIR = {Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.COBBLESTONE_STAIRS};
	private static final Block[] STONE = {Blocks.STONE, Blocks.ANDESITE, Blocks.GRAVEL, Blocks.COBBLESTONE};

	@Override
	public BlockState process(Random random, Block block, BlockState state) {
		if (block == Blocks.STONE_BRICKS) return RandUtils.getArrayEntry(BRICKS, random).getDefaultState();
		if (block == Blocks.MOSSY_STONE_BRICKS) return RandUtils.getArrayEntry(MOSSY_BRICKS, random).getDefaultState();
		if (block == Blocks.MOSSY_COBBLESTONE_SLAB) return RandUtils.getArrayEntry(MOSSY_SLAB, random).getDefaultState();
		if (block == Blocks.COBBLESTONE_SLAB) return RandUtils.getArrayEntry(SLAB, random).getDefaultState();
		if (block == Blocks.STONE) return RandUtils.getArrayEntry(STONE, random).getDefaultState();
		if (block == Blocks.STONE_BRICK_STAIRS) return copyStair(state, RandUtils.getArrayEntry(STAIR, random));
		if (block == LootBoxes.URN_BLOCK && RandUtils.getBool(70f, random)) return Blocks.AIR.getDefaultState();

		return null;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
