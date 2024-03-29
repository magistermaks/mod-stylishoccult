package net.darktree.stylishoccult.block.occult;

import net.darktree.interference.Voxels;
import net.darktree.interference.api.LookAtEvent;
import net.darktree.stylishoccult.block.BuildingBlock;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.overlay.PlayerEntityClientDuck;
import net.darktree.stylishoccult.overlay.PlayerEntityMadnessDuck;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class EyeBlock extends BuildingBlock implements ImpureBlock, FoliageFleshBlock, LookAtEvent {

	private static final BooleanProperty PERSISTENT = BooleanProperty.of("persistent");
	private static final VoxelShape SHAPE = Voxels.box(1, 1, 0, 15, 15, 16).box(0, 1, 1, 16, 15, 15).box(1, 0, 1, 15, 16, 15).build();

	public EyeBlock() {
		super(RegUtil.settings( Material.ORGANIC_PRODUCT, Sounds.FLESH, 1.0F, 1.0F, true ).luminance(6));
		setDefaultState( getDefaultState().with(PERSISTENT, false) );
	}

	@Override
	public void onLookAtTick(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		float value = ((PlayerEntityMadnessDuck) player).stylish_addMadness(0.03f);
		((PlayerEntityClientDuck) player).stylish_startHeartbeatSound();

		// notify the server
		Network.MADNESS.send(value);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PERSISTENT);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (!world.getBlockState(pos).get(PERSISTENT) && direction.getAxis().isVertical()) {
			if (!(world.getBlockState(pos.up()).getBlock() instanceof TentacleBlock) && !(world.getBlockState(pos.down()).getBlock() instanceof TentacleBlock)) {
				return Blocks.AIR.getDefaultState();
			}
		}

		return state;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(PERSISTENT, true);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public int impurityLevel(BlockState state) {
		return 8;
	}

}
