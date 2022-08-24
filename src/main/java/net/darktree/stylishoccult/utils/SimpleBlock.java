package net.darktree.stylishoccult.utils;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.loot.LootTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class SimpleBlock extends Block implements DefaultLoot {

	public SimpleBlock(Settings settings) {
		super(settings);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public LootTable getDefaultLootTable() {
		return null;
	}

	@Override
	public List<ItemStack> getDefaultStacks(BlockState state, LootContext.Builder builder, Identifier identifier, LootContext lootContext, ServerWorld serverWorld, net.minecraft.loot.LootTable lootTable) {
		return LootManager.dispatch(state, builder, this.lootTableId, getDefaultLootTable());
	}

}
