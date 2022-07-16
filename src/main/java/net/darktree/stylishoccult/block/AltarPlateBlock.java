package net.darktree.stylishoccult.block;

import net.darktree.interference.api.DropsItself;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.entity.altar.AltarPlateBlockEntity;
import net.darktree.stylishoccult.block.rune.VerticalRuneLink;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.Utils;
import net.darktree.stylishoccult.utils.Voxels;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class AltarPlateBlock extends BlockWithEntity implements DropsItself, VerticalRuneLink {

	public static final VoxelShape SHAPE = Voxels.box(2, 0, 2, 14, 2, 14).build();

	public AltarPlateBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == ModBlocks.RUNESTONE_TABLE;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add( Utils.tooltip("altar_plate") );
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.2F && world.getBlockEntity(pos) instanceof AltarPlateBlockEntity plate && plate.getCandles().size() > 0) {
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarPlateBlockEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos).use(player.getStackInHand(hand)) ? ActionResult.SUCCESS : ActionResult.PASS;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, BlockEntities.ALTAR_PLATE, (world_, pos_, state_, entity) -> entity.tick());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN) {
			return canPlaceAt(state, world, pos) ? state : Blocks.AIR.getDefaultState();
		}

		return state;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean onEndReached(World world, BlockPos pos, StackElement element) {
		if (element == null && world.getBlockEntity(pos) instanceof AltarPlateBlockEntity plate) {
			return plate.activate();
		}

		return false;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}

		if (world.getBlockEntity(pos) instanceof AltarPlateBlockEntity plate) {
			plate.onBlockRemoved();
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

}
