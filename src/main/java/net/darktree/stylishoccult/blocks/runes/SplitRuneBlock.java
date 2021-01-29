package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SplitRuneBlock extends DirectionalRuneBlock {

    public SplitRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, RunicScript script ) {
        Direction direction = getFacing(world, pos);
        return new Direction[] { direction, direction.getOpposite() };
    }

}
