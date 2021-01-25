package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.Rune;
import net.darktree.stylishoccult.script.components.RuneType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class DirectionalRuneBlock extends RuneBlock {

    public static final DirectionProperty FACING = Properties.FACING;

    public DirectionalRuneBlock(Rune rune) {
        super(RuneType.TRANSFER, rune);
        setDefaultState( getDefaultState().with(FACING, Direction.NORTH) );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
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
}
