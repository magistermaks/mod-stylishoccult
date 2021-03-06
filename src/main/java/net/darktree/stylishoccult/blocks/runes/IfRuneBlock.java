package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class IfRuneBlock extends DirectionalRuneBlock {

    public IfRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, RunicScript script) {
        try {
            if( script.getStack().pull() != 0 ) return new Direction[]{getFacing(world, pos)};
        }catch (Exception ignore) {}

        // Make it explode (?)
        return super.getDirections(world, pos, script);
    }
}
