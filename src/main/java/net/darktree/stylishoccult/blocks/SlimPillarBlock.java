package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.utils.BuildingBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SlimPillarBlock extends BuildingBlock {

    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final VoxelShape SHAPE_X = Utils.box(0, 4, 4, 16, 12, 12);
    public static final VoxelShape SHAPE_Y = Utils.box(4, 0, 4, 12, 16, 12);
    public static final VoxelShape SHAPE_Z = Utils.box(4, 4, 0, 12, 12, 16);

    public SlimPillarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        switch( state.get(AXIS) ) {
            case X: return SHAPE_X;
            case Z: return SHAPE_Z;
            default: return SHAPE_Y;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

}
