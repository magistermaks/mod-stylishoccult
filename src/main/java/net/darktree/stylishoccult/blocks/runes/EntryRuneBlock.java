package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.Rune;
import net.darktree.stylishoccult.script.components.RuneType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EntryRuneBlock extends RuneBlock {

    public EntryRuneBlock(Rune rune) {
        super( RuneType.INPUT, rune );
    }

    protected void emit(World world, BlockPos pos) {
        execute( world, pos, world.getBlockState(pos), new RunicScript() );
    }

    @Override
    protected Direction[] getDirections(RuneBlockEntity entity, Rune rune) {
        return Direction.values();
    }

    @Override
    protected boolean canAcceptSignal() {
        return false;
    }

}
