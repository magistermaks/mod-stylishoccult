package net.darktree.stylishoccult.blocks;

import net.darktree.interference.api.DropsItself;
import net.darktree.stylishoccult.utils.Utils;
import net.darktree.stylishoccult.utils.Voxels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends Block implements DropsItself {

	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty TOP = BooleanProperty.of("top");

	// TODO: better shape
	public static final VoxelShape SHAPE_BASE = Voxels.box(5, 0, 5, 11, 12, 11).box(0, 12, 0, 16, 16, 16).build();
	public static final VoxelShape SHAPE_CONNECTED = Utils.shape(5, 0, 5, 11, 16, 11);

	public TableBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SOUTH, NORTH, WEST, EAST, TOP);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(TOP) ? SHAPE_CONNECTED : SHAPE_BASE;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return getStateToFit(world, pos, state);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getStateToFit(ctx.getWorld(), ctx.getBlockPos(), getDefaultState());
	}

	private BlockState getStateToFit(WorldAccess world, BlockPos pos, BlockState state) {
		return state.with(WEST, world.getBlockState(pos.west()).isOf(this))
				.with(EAST, world.getBlockState(pos.east()).isOf(this))
				.with(SOUTH, world.getBlockState(pos.south()).isOf(this))
				.with(NORTH, world.getBlockState(pos.north()).isOf(this))
				.with(TOP, world.getBlockState(pos.up()).isOf(this));
	}

}
