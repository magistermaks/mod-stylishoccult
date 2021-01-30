package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RedstoneAnalogOutputRuneBlock extends ActorRuneBlock {

    public static final IntProperty POWER = Properties.POWER;

    public RedstoneAnalogOutputRuneBlock(String name) {
        super(name);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(POWER));
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(COOLDOWN) != 0 ? state.get(POWER) : 0;
    }

    @Override
    protected void onTriggered(RunicScript script, World world, BlockPos pos, BlockState state) {
        try {
            world.setBlockState(pos, world.getBlockState(pos).with(POWER, toPowerLevel(script.getStack().pull())));
        }catch(Exception exception) {
            throw RuneExceptionType.INVALID_ARGUMENT_COUNT.get();
        }
    }

    private static int toPowerLevel( double value ) {
        return MathHelper.clamp( (int) value, 0, 15 );
    }

}
