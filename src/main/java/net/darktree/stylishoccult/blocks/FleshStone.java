package net.darktree.stylishoccult.blocks;

import net.darktree.interference.AxeScrapeHelper;
import net.darktree.interference.api.AxeScrapeable;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class FleshStone extends BuildingBlock implements AxeScrapeable {

	public FleshStone(Settings settings) {
		super(settings);
	}

	@Override
	public LootTable getInternalLootTableId() {
		return LootTables.STONE_FLESH;
	}

	@Override
	public Optional<BlockState> getScrapedState(World world, BlockPos pos, BlockState state, PlayerEntity entity) {
		Block.dropStack(world, pos, new ItemStack(ModItems.VEINS, world.random.nextInt(3) + 1));
		return AxeScrapeHelper.scrapeOff(world, pos, entity, Blocks.STONE.getDefaultState());
	}
}
