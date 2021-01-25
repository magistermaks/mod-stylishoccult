package net.darktree.stylishoccult.script.runes;

import net.darktree.stylishoccult.blocks.runes.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.Rune;
import net.darktree.stylishoccult.script.components.RuneException;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ForkRune extends Rune {

    public ForkRune(String name) {
        super(name);
    }

    public Direction[] directions(World world, BlockPos pos, RunicScript script ) {
        BlockState state = world.getBlockState(pos);

        if( state.getBlock() instanceof DirectionalRuneBlock) {
            Direction facing = state.get(DirectionalRuneBlock.FACING);
            return new Direction[] { facing, script.getDirection() };
        }

        throw new RuneException("Invalid state, expected DirectionalRuneBlock!");
    }

}
