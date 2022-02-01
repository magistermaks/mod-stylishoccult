package net.darktree.stylishoccult.blocks.runes.flow;

import net.darktree.stylishoccult.blocks.runes.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ForkRuneBlock extends DirectionalRuneBlock {

    public ForkRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script ) {
        return new Direction[] { script.direction, getFacing(world, pos) };
    }

}
