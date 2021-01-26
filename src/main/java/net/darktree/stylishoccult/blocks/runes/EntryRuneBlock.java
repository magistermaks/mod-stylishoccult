package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EntryRuneBlock extends RuneBlock {

    public EntryRuneBlock( String name ) {
        super( RuneType.INPUT, name );
    }

    protected void emit(World world, BlockPos pos) {
        execute( world, pos, world.getBlockState(pos), new RunicScript() );
    }

    @Override
    public Direction[] getDirections( World world, BlockPos pos, RunicScript script ) {
        return Direction.values();
    }

    @Override
    public boolean canAcceptSignal() {
        return false;
    }

}
