package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.loot.BakedLootTable;
import net.darktree.stylishoccult.mixin.HardnessAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public abstract class SimpleBlock extends Block implements MutableHardness {

    public SimpleBlock(Settings settings) {
        super(settings);
        this.lootTableId = LootTables.EMPTY;
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

    public BakedLootTable getInternalLootTableId() {
        return null;
    }

    @Override
    final public List<ItemStack> getDroppedStacks(BlockState state, net.minecraft.loot.context.LootContext.Builder builder) {
        return LootManager.dispatch(state, builder, getLootTableId(), getInternalLootTableId());
    }
}
