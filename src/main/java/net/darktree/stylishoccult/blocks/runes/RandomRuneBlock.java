package net.darktree.stylishoccult.blocks.runes;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RandomRuneBlock extends EntryRuneBlock {

    public RandomRuneBlock(String name) {
        super(name);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        emit(world, pos);
    }

}
