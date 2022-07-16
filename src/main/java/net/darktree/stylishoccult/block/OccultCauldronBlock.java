package net.darktree.stylishoccult.block;

import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntity;
import net.darktree.stylishoccult.block.fluid.ModFluids;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * For now this can only store blood, but it would be a good idea
 * to consider other fluids as well (with fluid variant)
 */
public class OccultCauldronBlock extends BlockWithEntity {

	public static final BooleanProperty BOILING = BooleanProperty.of("boiling");

	protected OccultCauldronBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(BOILING, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BOILING);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AbstractCauldronBlock.OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return AbstractCauldronBlock.RAYCAST_SHAPE;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OccultCauldronBlockEntity(pos, state);
	}

	public static void set(World world, BlockPos pos, boolean sure, boolean boiling) {
		if (sure || world.getBlockState(pos).getBlock() == ModBlocks.OCCULT_CAULDRON) {
			world.setBlockState(pos, ModBlocks.OCCULT_CAULDRON.getDefaultState().with(BOILING, boiling));
		}
	}

	private OccultCauldronBlockEntity getEntity(World world, BlockPos pos) {
		return Objects.requireNonNull(BlockUtils.getEntity(OccultCauldronBlockEntity.class, world, pos));
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		OccultCauldronBlockEntity blockEntity = getEntity(world, pos);

		if (!world.isClient && entity.isOnFire() && this.isEntityTouchingFluid(blockEntity, pos, entity)) {
			OccultCauldronBlockEntity.Storage storage = blockEntity.getStorage();

			if (storage.getAmount() >= FluidConstants.INGOT) {
				entity.extinguish();

				if (entity.canModifyAt(world, pos)) {
					try (Transaction transaction = Transaction.openOuter()) {
						storage.extract(ModFluids.BLOOD_VARIANT, FluidConstants.INGOT, transaction);
						transaction.commit();
					}
				}
			}
		}
	}

	private boolean isEntityTouchingFluid(OccultCauldronBlockEntity blockEntity, BlockPos pos, Entity entity) {
		return entity.getY() < (pos.getY() + blockEntity.getLevel()) && entity.getBoundingBox().maxY > (double)pos.getY() + 0.25;
	}

	private void playSound(World world, BlockPos pos, SoundEvent event) {
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1, 1);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		OccultCauldronBlockEntity.Storage storage = getEntity(world, pos).getStorage();
		ItemStack stack = player.getStackInHand(hand);
		Item item = stack.getItem();

		if (item == Items.BUCKET && storage.extract(FluidConstants.BUCKET)) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.BLOOD_BUCKET)));
			playSound(world, pos, SoundEvents.ITEM_BUCKET_FILL);
			incrementStat(player, item);
			return ActionResult.SUCCESS;
		}

		if (item == Items.GLASS_BOTTLE && storage.extract(FluidConstants.BOTTLE)) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.BLOOD_BOTTLE)));
			playSound(world, pos, SoundEvents.ITEM_BUCKET_FILL);
			incrementStat(player, item);
			return ActionResult.SUCCESS;
		}

		if (item == ModItems.BLOOD_BUCKET && storage.insert(FluidConstants.BUCKET)) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
			playSound(world, pos, SoundEvents.ITEM_BOTTLE_EMPTY);
			incrementStat(player, item);
			return ActionResult.SUCCESS;
		}

		if (item == ModItems.BLOOD_BOTTLE && storage.insert(FluidConstants.BOTTLE)) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
			playSound(world, pos, SoundEvents.ITEM_BOTTLE_EMPTY);
			incrementStat(player, item);
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	private void incrementStat(PlayerEntity player, Item item) {
		player.incrementStat(Stats.USE_CAULDRON);
		player.incrementStat(Stats.USED.getOrCreateStat(item));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(BOILING) && random.nextInt(4) == 0) {
			if (getEntity(world, pos ).getStorage().getAmount() > 0) {
				world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Sounds.BOIL.soundEvent, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, BlockEntities.OCCULT_CAULDRON, (world_, pos_, state_, entity) -> entity.tick());
	}
}
