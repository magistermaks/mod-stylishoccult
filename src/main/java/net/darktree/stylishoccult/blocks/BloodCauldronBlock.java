package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.OccultCauldronBlockEntity;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.sounds.SoundManager;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class BloodCauldronBlock extends LeveledCauldronBlock {

	public static final Map<Item, CauldronBehavior> BLOOD_BEHAVIOURS = CauldronBehavior.createMap();

	protected BloodCauldronBlock(Settings settings) {
		super(settings, precipitation -> false, BLOOD_BEHAVIOURS);
	}

	@Override
	protected boolean canBeFilledByDripstone(Fluid fluid) {
		return false;
	}

	private void playSound(World world, BlockPos pos, SoundEvent event) {
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1, 1);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int level = state.get(LEVEL);
		ItemStack stack = player.getStackInHand(hand);
		Item item = stack.getItem();

		if (item == Items.BUCKET && level == 3) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.BLOOD_BUCKET)));
			playSound(world, pos, SoundEvents.ITEM_BUCKET_FILL);
			incrementStat(player, item);
			setStateForLevel(world, pos, 0);
			return ActionResult.SUCCESS;
		}

		if (item == Items.GLASS_BOTTLE && level >= 1) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.BLOOD_BOTTLE)));
			playSound(world, pos, SoundEvents.ITEM_BUCKET_FILL);
			incrementStat(player, item);
			setStateForLevel(world, pos, level - 1);
			return ActionResult.SUCCESS;
		}

		if (item == ModItems.BLOOD_BOTTLE && level < 3) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
			playSound(world, pos, SoundEvents.ITEM_BOTTLE_EMPTY);
			incrementStat(player, item);
			setStateForLevel(world, pos, level + 1);
			return ActionResult.SUCCESS;
		}

		if (item == ModItems.OCCULT_STAFF) {
			SoundManager.playSound(world, pos, "spell");
			incrementStat(player, item);
			world.setBlockState(pos, ModBlocks.OCCULT_CAULDRON.getDefaultState());
			BlockUtils.getEntity(OccultCauldronBlockEntity.class, world, pos).getStorage().insert(FluidConstants.BOTTLE * level);
			return ActionResult.SUCCESS;
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	private void incrementStat(PlayerEntity player, Item item) {
		player.incrementStat(Stats.USE_CAULDRON);
		player.incrementStat(Stats.USED.getOrCreateStat(item));
	}

	private void setStateForLevel(World world, BlockPos pos, int level) {
		world.setBlockState(pos, level == 0 ? Blocks.CAULDRON.getDefaultState() : ModBlocks.BLOOD_CAULDRON.getDefaultState().with(LEVEL, level));
	}

}
