package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScatterRuneBlock extends TransferRuneBlock {

    public ScatterRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, RunicScript script) {
        return Direction.values();
    }

}
