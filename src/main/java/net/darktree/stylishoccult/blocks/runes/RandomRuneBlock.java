package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.Rune;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RandomRuneBlock extends EntryRuneBlock {

    public RandomRuneBlock(Rune rune) {
        super(rune);
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
