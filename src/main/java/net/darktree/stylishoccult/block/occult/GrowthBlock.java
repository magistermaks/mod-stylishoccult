package net.darktree.stylishoccult.block.occult;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.darktree.interference.Voxels;
import net.darktree.interference.api.FluidReplaceable;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GrowthBlock extends SimpleBlock implements ImpureBlock, FluidReplaceable {

	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final IntProperty SIZE = IntProperty.of("size", 1, 3);

	private static final ImmutableSet<Pair<BooleanProperty, VoxelShape>> SHAPES = new ImmutableSet.Builder<Pair<BooleanProperty, VoxelShape>>()
			.add(Pair.of(UP,    Voxels.shape(  0, 16,  0, 16, 15, 16 )))
			.add(Pair.of(DOWN,  Voxels.shape(  0,  0,  0, 16,  1, 16 )))
			.add(Pair.of(SOUTH, Voxels.shape(  0,  0, 15, 16, 16, 16 )))
			.add(Pair.of(NORTH, Voxels.shape(  0,  0,  0, 16, 16,  1 )))
			.add(Pair.of(WEST,  Voxels.shape(  0,  0,  0,  1, 16, 16 )))
			.add(Pair.of(EAST,  Voxels.shape( 15,  0,  0, 16, 16, 16 )))
			.build();

	public static boolean hasSide(BlockState state) {
		return state.get(EAST) || state.get(WEST) || state.get(NORTH) || state.get(SOUTH);
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.GROWTH;
	}

	public GrowthBlock() {
		super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).slipperiness(0.7f).ticksRandomly() );
		setDefaultState( getDefaultState().with(SIZE, 1).with(UP, false).with(DOWN, false).with(SOUTH, false).with(NORTH, false).with(WEST, false).with(EAST, false) );
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, SOUTH, NORTH, WEST, EAST, SIZE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = VoxelShapes.empty();

		for (Pair<BooleanProperty, VoxelShape> entry : SHAPES) {
			if (state.get(entry.getFirst())) shape = VoxelShapes.combine(entry.getSecond(), shape, BooleanBiFunction.OR);
		}

		return shape;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BooleanProperty property = fromDirection(direction);
		if (state.get(property) && !canSupport(world, pos, direction)) {
			state = state.with(property, false);
			world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);
		}

		return getSideCount(state) == 0 ? Blocks.AIR.getDefaultState() : state;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getStateToFit( ctx.getWorld(), ctx.getBlockPos() );
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state
				.with(fromDirection(rotation.rotate(Direction.NORTH)), state.get(NORTH))
				.with(fromDirection(rotation.rotate(Direction.EAST)), state.get(EAST))
				.with(fromDirection(rotation.rotate(Direction.SOUTH)), state.get(SOUTH))
				.with(fromDirection(rotation.rotate(Direction.WEST)), state.get(WEST));
	}

	public BlockState getStateToFit(World world, BlockPos pos ) {
		BlockState state = getDefaultState();
		boolean flag = false;

		for (Direction side : Directions.ALL) {
			if (canSupport(world, pos, side)) {
				state = state.with(fromDirection(side), true);
				flag = true;
			}
		}

		return flag ? state : null;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int count = getSideCount(state);

		if (count == 0) {
			StylishOccult.LOGGER.warn("Empty growth block was found and removed!");
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			return;
		}

		if (OccultHelper.touchesSource(world, pos)) {
			if( BlockUtils.countInArea(world, pos, FoliageFleshBlock.class, 3) != 0 ) {
				return;
			}else{
				if (RandUtils.getBool(10, random)) {
					return;
				}
			}
		}

		if (BlockUtils.countInArea(world, pos, GrowthBlock.class, 3) > 9) {
			return;
		}

		if (count != 6) {
			if (random.nextInt(count + 1) == 0) {
				applyRandom(state, pos, world, random);
			}

			BlockPos target = pos.offset(RandUtils.getEnum(Direction.class, random));

			if (random.nextInt( 8 ) == 0) {
				target = target.offset(RandUtils.getEnum(Direction.class, random));
			}

			BlockState targetState = world.getBlockState( target );

			try {
				if (targetState.getBlock() != this) {
					if (targetState.isAir() || targetState.getMaterial().isReplaceable() || targetState.canReplace(null)) {
						applyRandom(getDefaultState(), target, world, random);
						return;
					}
				}
			} catch(Exception ignore) {}
		}

		int size = state.get(SIZE);
		if (size < 3 && random.nextInt(64 - count * 4) == 0) {
			world.setBlockState(pos, state.with(SIZE, size + 1));
		}

	}

	private void applyRandom( BlockState state, BlockPos pos, ServerWorld world, Random random ) {
		Direction side = RandUtils.getEnum(Direction.class, random);
		BlockState target = state.with(fromDirection(side), true);

		if (canSupport(world, pos, side)) {
			world.setBlockState(pos, target);
		}
	}

	private int getSideCount(BlockState state) {
		int count = 0;

		for (Pair<BooleanProperty, VoxelShape> entry : SHAPES) {
			if (state.get(entry.getFirst())) count ++;
		}

		return count;
	}

	private BooleanProperty fromDirection(Direction direction) {
		return switch (direction) {
			case UP -> UP;
			case DOWN -> DOWN;
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case WEST -> WEST;
			case EAST -> EAST;
		};
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return context.getStack().isEmpty() || context.getStack().getItem() != this.asItem();
	}

	private boolean canSupport(WorldAccess world, BlockPos pos, Direction direction) {
		BlockPos support = pos.offset(direction);
		BlockState state = world.getBlockState( support );
		return state.isFullCube(world, support) || isShapeFullCube(state.getOutlineShape(world, support)) || state.isSideSolidFullSquare( world, support, direction.getOpposite() );
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);

		if (getSideCount(state) <= 1) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		} else {
			for (Direction side : Directions.ALL) {
				BooleanProperty property = fromDirection(side);
				if (state.get(property)) {
					world.setBlockState(pos, state.with(property, false));
					return;
				}
			}
		}
	}

	@Override
	public int impurityLevel(BlockState state) {
		return (getSideCount(state) + state.get(SIZE)) * 3;
	}

}
