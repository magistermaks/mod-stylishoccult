package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.BloodCauldronBlockEntity;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class BloodCauldronBlock extends BlockWithEntity {

	public static final Map<Item, CauldronBehavior> BEHAVIORS = CauldronBehavior.createMap();
	public static final IntProperty LEVEL = IntProperty.of("level", 1, 11);

	protected BloodCauldronBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
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
		return new BloodCauldronBlockEntity(pos, state);
	}

	private BloodCauldronBlockEntity.Storage getStorage(World world, BlockPos pos) {
		return Objects.requireNonNull(BlockUtils.getEntity(BloodCauldronBlockEntity.class, world, pos)).getStorage();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity.isOnFire() && this.isEntityTouchingFluid(state, pos, entity)) {
			BloodCauldronBlockEntity.Storage storage = getStorage(world, pos);

			if (storage.getAmount() >= FluidConstants.INGOT) {
				entity.extinguish();

				if (entity.canModifyAt(world, pos)) {
					try (Transaction transaction = Transaction.openOuter()) {
						storage.extract(ModBlocks.BLOOD_VARIANT, FluidConstants.INGOT, transaction);
						transaction.commit();
					}
				}
			}
		}
	}

	private boolean isEntityTouchingFluid(BlockState state, BlockPos pos, Entity entity) {
		return entity.getY() < (pos.getY() + (4.0 + state.get(LEVEL)) / 16.0) && entity.getBoundingBox().maxY > (double)pos.getY() + 0.25;
	}

	private void playSound(World world, BlockPos pos, SoundEvent event) {
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1, 1);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BloodCauldronBlockEntity.Storage storage = getStorage(world, pos);
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

		CauldronBehavior cauldronBehavior = BEHAVIORS.get(item);
		return cauldronBehavior.interact(state, world, pos, player, hand, stack);
	}

	private void incrementStat(PlayerEntity player, Item item) {
		player.incrementStat(Stats.USE_CAULDRON);
		player.incrementStat(Stats.USED.getOrCreateStat(item));
	}

}
