package net.darktree.stylishoccult.block.occult;

import net.darktree.interference.Voxels;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class TentacleBlock extends SimpleBlock implements ImpureBlock, FoliageFleshBlock {

	public static final IntProperty SIZE = IntProperty.of("size", 1, 6);
	public static final BooleanProperty STATIC = BooleanProperty.of("static");

	public static final VoxelShape[] SHAPES = {
			Voxels.shape( 6, 0, 6, 10, 16, 10 ),
			Voxels.shape( 5, 0, 5, 11, 16, 11 ),
			Voxels.shape( 4, 0, 4, 12, 16, 12 ),
			Voxels.shape( 3, 0, 3, 13, 16, 13 ),
			Voxels.shape( 2, 0, 2, 14, 16, 14 ),
			Voxels.shape( 1, 0, 1, 15, 16, 15 )
	};

	public TentacleBlock() {
		super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.STONE, 1.8F, 1.8F, false ).slipperiness(0.8f).ticksRandomly() );
		setDefaultState( getDefaultState().with(SIZE, 6).with(STATIC, false) );
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES[state.get(SIZE) - 1];
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return !state.get(STATIC);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int size = state.get(SIZE);
		BlockState up = world.getBlockState(pos.up());
		BlockState down = world.getBlockState(pos.down());

		if (up.isAir() || up.getMaterial().isReplaceable()) {
			if (grow(world, pos.up(), size, random)) {
				world.setBlockState( pos, state.with(STATIC, true) );
			}
		} else if (down.isAir() || down.getMaterial().isReplaceable()) {
			if (grow(world, pos.down(), size, random)) {
				world.setBlockState( pos, state.with(STATIC, true) );
			}
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SIZE, STATIC);
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState up = ctx.getWorld().getBlockState(ctx.getBlockPos().up());
		BlockState down = ctx.getWorld().getBlockState(ctx.getBlockPos().down());

		if (isValid(up, 0) != isValid(down, 0)) {

			Random random = new Random(ctx.getWorld().getTime() / 10);
			int fallback = RandUtils.rangeInt(1, 6, random) + 1;

			int size = Math.max(getSize(up, fallback), getSize(down, fallback)) - 1;
			if (size >= 1 && size <= 6) {
				return getDefaultState().with(SIZE, size);
			}
		}

		return null;

	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		state = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);

		if (state.getBlock() == this) {
			boolean flag = isStatic(world, pos.up()) || isStatic(world, pos.down());
			return state.with(STATIC, flag);
		}

		return state;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		int size = state.get(SIZE);
		BlockState up = world.getBlockState(pos.up());
		BlockState down = world.getBlockState(pos.down());

		return isValid( up, size ) || isValid( down, size );
	}

	@Override
	public int impurityLevel(BlockState state) {
		return 13 + state.get(SIZE);
	}

	public boolean isStatic( WorldAccess world, BlockPos pos ) {
		BlockState state = world.getBlockState(pos);
		return (state.getBlock() instanceof TentacleBlock) ? state.get(STATIC) : false;
	}

	public boolean isValid( BlockState state, int size ) {
		return (state.getBlock() instanceof TentacleBlock) ? state.get(SIZE) > size : (state.getBlock() instanceof FullFleshBlock);
	}

	public int getSize( BlockState state, int fallback ) {
		return (state.getBlock() instanceof TentacleBlock) ? state.get(SIZE) : ((state.getBlock() instanceof FullFleshBlock) ? fallback : 0);
	}

	public boolean grow(World world, BlockPos pos, int size, Random random) {
		if (size > 1) {
			world.setBlockState( pos, getDefaultState().with(SIZE, size - 1) );
			return false;
		} else {
			if (RandUtils.getBool(80, random)) {
				world.setBlockState(pos, ModBlocks.EYE_FLESH.getDefaultState());
			}
			return true;
		}
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.SIMPLE;
	}

}
