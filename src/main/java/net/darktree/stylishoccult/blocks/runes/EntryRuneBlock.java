package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class EntryRuneBlock extends RuneBlock {

    public EntryRuneBlock( String name ) {
        super( RuneType.ENTRY, name );
    }

    protected void emit(World world, BlockPos pos) {
        execute(world, pos, world.getBlockState(pos), new Script());
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        return Direction.values();
    }

    @Override
    public boolean canAcceptSignal() {
        return false;
    }

}
