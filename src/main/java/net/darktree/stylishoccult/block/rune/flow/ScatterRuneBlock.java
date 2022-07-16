package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.rune.TransferRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScatterRuneBlock extends TransferRuneBlock {

    public ScatterRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        return Direction.values();
    }

}
