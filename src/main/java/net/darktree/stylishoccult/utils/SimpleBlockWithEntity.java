package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.mixin.HardnessAccessor;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.List;

// TODO clean this up, after 1.17 it became a mess

abstract public class SimpleBlockWithEntity extends BlockWithEntity implements MutableHardness {

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
        if ( !canPlaceAt(state, world, pos) ) {
            return Blocks.AIR.getDefaultState();
        }else{
            return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        }
    }

    public net.darktree.stylishoccult.loot.LootTable getInternalLootTableId() {
        return null;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, net.minecraft.loot.context.LootContext.Builder builder) {
        LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
        ServerWorld serverWorld = lootContext.getWorld();
        LootTable lootTable = serverWorld.getServer().getLootManager().getTable(lootTableId);

        if( lootTable == LootTable.EMPTY ) {
            return LootManager.dispatch(state, builder, this.lootTableId, getInternalLootTableId());
        }

        return super.getDroppedStacks(state, builder);
    }

}
