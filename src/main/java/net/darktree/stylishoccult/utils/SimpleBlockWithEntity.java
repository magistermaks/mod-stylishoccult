package net.darktree.stylishoccult.utils;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.interference.api.MutableHardness;
import net.darktree.interference.mixin.HardnessAccessor;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.loot.LootTable;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.List;

// TODO clean this up, after 1.17 it became a mess

public abstract class SimpleBlockWithEntity extends BlockWithEntity implements MutableHardness, DefaultLoot {

    protected SimpleBlockWithEntity(Block.Settings settings) {
        super(settings);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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
