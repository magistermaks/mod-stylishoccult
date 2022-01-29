package net.darktree.stylishoccult.utils;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.interference.api.MutableHardness;
import net.darktree.interference.mixin.HardnessAccessor;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.List;


public class SimpleBlock extends Block implements MutableHardness, DefaultLoot {

    public SimpleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public float getHardness(BlockState state, BlockView world, BlockPos pos) {
        return getStoredHardness(state);
    }

    public float getStoredHardness(BlockState state) {
        return ((HardnessAccessor) state).getStoredHardness();
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public LootTable getInternalLootTableId() {
        return null;
    }

    @Override
    public List<ItemStack> getDefaultStacks(BlockState state, LootContext.Builder builder, Identifier identifier, LootContext lootContext, ServerWorld serverWorld, net.minecraft.loot.LootTable lootTable) {
        return LootManager.dispatch(state, builder, this.lootTableId, getInternalLootTableId());
    }

}
