package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.component.RuneType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class DirectionalRuneBlock extends RuneBlock {

    public static final DirectionProperty FACING = Properties.FACING;

    public DirectionalRuneBlock(String name) {
        super(RuneType.TRANSFER, name);
        setDefaultState( getDefaultState().with(FACING, Direction.NORTH) );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if( state != null ) {
            return state.with(FACING, ctx.getPlayerLookDirection());
        }else{
            return getDefaultState();
        }
    }

    public Direction getFacing(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if( state.getBlock() instanceof DirectionalRuneBlock ) {
            return state.get(DirectionalRuneBlock.FACING);
        }

        throw RuneException.of(RuneExceptionType.INVALID_STATE);
    }

}
