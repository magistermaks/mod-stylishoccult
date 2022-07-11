package net.darktree.stylishoccult.blocks;

import net.darktree.interference.api.DropsItself;
import net.darktree.stylishoccult.blocks.entities.AltarPlateBlockEntity;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.blocks.runes.VerticalRuneLink;
import net.darktree.stylishoccult.script.elements.StackElement;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.Voxels;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

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
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
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

}
