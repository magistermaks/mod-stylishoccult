package net.darktree.stylishoccult.blocks.runes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneDigitalInputRuneBlock extends EntryRuneBlock {

    private static final BooleanProperty POWERED = Properties.POWERED;

    public RedstoneDigitalInputRuneBlock(String name) {
        super(name);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(POWERED));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean power = world.isReceivingRedstonePower(pos);

        if( state.get(POWERED) != power ) {
            world.setBlockState( pos, state.with(POWERED, power) );
            if( power ) {
                emit(world, pos);
            }
        }
    }
}
