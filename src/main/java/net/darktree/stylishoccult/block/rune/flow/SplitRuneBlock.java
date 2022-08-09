package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.rune.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SplitRuneBlock extends DirectionalRuneBlock {

    public SplitRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        return Directions.opposites(getFacing(world, pos));
    }

}
