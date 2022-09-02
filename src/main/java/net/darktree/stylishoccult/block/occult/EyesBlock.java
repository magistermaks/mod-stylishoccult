package net.darktree.stylishoccult.block.occult;

import net.darktree.interference.Voxels;
import net.darktree.interference.api.FluidReplaceable;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class EyesBlock extends SimpleBlock implements ImpureBlock, FoliageFleshBlock, FluidReplaceable {

	public static final IntProperty SIZE = IntProperty.of("size", 1, 3);
	private static final VoxelShape OUTLINE_SHAPE = Voxels.shape(1, 0, 1, 15, 2, 15);

	public EyesBlock() {
		super( RegUtil.settings( Material.ORGANIC_PRODUCT, Sounds.FLESH, 0.8F, 0.8F, false ).ticksRandomly() );
		setDefaultState( getDefaultState().with(SIZE, 1) );
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int size = state.get(SIZE);

		if ((size == 3 || RandUtils.nextBool(40, random)) && BlockUtils.countInArea(world, pos, EyesBlock.class, 3) <= getMaxGroupSize()) {
			BlockPos target = pos.offset(RandUtils.pickFromEnum(Direction.class, random) );

			if (RandUtils.nextBool(75, random) ) {
				target = target.offset(RandUtils.pickFromEnum(Direction.class, random));
			}

			BlockState stateTarget = world.getBlockState(target);
			BlockState stateDown = world.getBlockState(target.down());

			if (stateDown.getBlock() instanceof FullFleshBlock && (stateTarget.isAir() || stateTarget.getMaterial().isReplaceable())) {
				world.setBlockState(target, getDefaultState().with(SIZE, RandUtils.nextInt(1, 3, random)));
			}
		}
	}

	private int getMaxGroupSize() {
		return this == ModBlocks.WARTS_FLESH ? 5 : 3;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SIZE);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int size = state.get(SIZE);
		ItemStack stack = player.getStackInHand(hand);

		if (player.getAbilities().allowModifyWorld && size < 3 && stack.getItem() == asItem()) {
			world.setBlockState( pos, state.with(SIZE, size + 1) );

			if (!player.isCreative()) {
				stack.decrement(1);
			}

			world.playSound( null, pos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS, 1, 1 );
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public int impurityLevel(BlockState state) {
		return state.get(SIZE) + 2;
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.EYES_BlOCK;
	}
}
