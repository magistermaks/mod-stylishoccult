package net.darktree.stylishoccult.blocks.runes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUpdateRuneBLock extends EntryRuneBlock {

    public BlockUpdateRuneBLock(String name) {
        super(name);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        emit( world, pos );
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }
}
