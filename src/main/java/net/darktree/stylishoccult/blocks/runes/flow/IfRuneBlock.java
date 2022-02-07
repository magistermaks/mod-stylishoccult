package net.darktree.stylishoccult.blocks.runes.flow;

import net.darktree.stylishoccult.blocks.runes.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class IfRuneBlock extends DirectionalRuneBlock {

    public IfRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        try {
            if( script.pull(world, pos).value() != 0 ) {
                return new Direction[]{getFacing(world, pos)};
            }
        }catch (Exception ignore) {}

        // Make it explode (?)
        return super.getDirections(world, pos, script);
    }
}
