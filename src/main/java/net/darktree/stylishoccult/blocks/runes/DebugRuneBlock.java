package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.RuneType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRuneBlock extends RuneBlock {

    public DebugRuneBlock( String name ) {
        super(RuneType.TRANSFER, name);
    }

    @Override
    protected void onTriggered(World world, BlockPos pos, BlockState state) {
        System.out.println( "Debug rune block at: " + pos + " tag: " + getEntity(world, pos).getScript().toTag().asString() );
    }
}
