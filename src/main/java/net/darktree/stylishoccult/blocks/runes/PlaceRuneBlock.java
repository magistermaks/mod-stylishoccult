package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceRuneBlock extends TransferRuneBlock {

    private final BlockState state;

    public PlaceRuneBlock(String name, Block block) {
        super(name);

        state = block.getDefaultState();
    }

    @Override
    public void apply(RunicScript script, World world, BlockPos pos) {
        try {
            int x = Math.round( (float) script.getStack().pull() );
            int y = Math.round( (float) script.getStack().pull() );
            int z = Math.round( (float) script.getStack().pull() );
            BlockPos target = pos.add(x, y, z);
            BlockState targetState = world.getBlockState(target);

            if( targetState.isAir() || targetState.getMaterial().isReplaceable() ) {
                world.setBlockState( target, state );
            }
        }catch (Exception exception) {
            throw new RuneException("invalid_arguments");
        }

        super.apply(script);
    }


}
