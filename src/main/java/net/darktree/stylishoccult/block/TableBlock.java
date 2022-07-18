package net.darktree.stylishoccult.block;

import net.darktree.interference.api.DropsItself;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.rune.EntryRuneBlock;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.block.rune.VerticalRuneLink;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Utils;
import net.darktree.stylishoccult.utils.Voxels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends EntryRuneBlock implements DropsItself, VerticalRuneLink {

	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty TOP = BooleanProperty.of("top");

	public static final VoxelShape SHAPE_FULL = Voxels.box(4.5f, 0, 4.5f, 11.5f, 12, 11.5f).box(0, 12, 0, 16, 16, 16).build();
	public static final VoxelShape SHAPE_STACKED = Utils.shape(4.5f, 0, 4.5f, 11.5f, 12, 11.5f);

	public TableBlock(String name) {
		super(name);
	}

	@Override
	public String getTranslationKey() {
		return "block." + StylishOccult.NAMESPACE + ".runestone_table";
	}

	@Override
	public MutableText getName() {
		return new TranslatableText(getTranslationKey());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SOUTH, NORTH, WEST, EAST, TOP);
		super.appendProperties(builder);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(TOP) ? SHAPE_STACKED : SHAPE_FULL;
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

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state
				.with(fromDirection(rotation.rotate(Direction.NORTH)), state.get(NORTH))
				.with(fromDirection(rotation.rotate(Direction.EAST)), state.get(EAST))
				.with(fromDirection(rotation.rotate(Direction.SOUTH)), state.get(SOUTH))
				.with(fromDirection(rotation.rotate(Direction.WEST)), state.get(WEST));
	}

	private BooleanProperty fromDirection( Direction direction ) {
		return switch (direction) {
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case WEST -> WEST;
			case EAST -> EAST;
			default -> null;
		};
	}

	private BlockState getStateToFit(WorldAccess world, BlockPos pos, BlockState state) {
		return state.with(WEST, world.getBlockState(pos.west()).isOf(this))
				.with(EAST, world.getBlockState(pos.east()).isOf(this))
				.with(SOUTH, world.getBlockState(pos.south()).isOf(this))
				.with(NORTH, world.getBlockState(pos.north()).isOf(this))
				.with(TOP, world.getBlockState(pos.up()).isOf(this));
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return new Direction[] {Direction.DOWN};
	}

	@Override
	public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
		return from == Direction.DOWN;
	}

	@Override
	public boolean onEndReached(World world, BlockPos pos, StackElement element) {
		Script script = new Script();

		if (element != null) {
			script.stack.push(element);
		}

		if (world.getBlockState(pos.down()).getBlock() instanceof RuneBlock) {
			emitScript(world, pos, script);
			return true;
		}

		return false;
	}

	@Override
	protected void onSignalAccepted(World world, BlockPos pos) {
		sendUp(world, pos, null);
	}

}
