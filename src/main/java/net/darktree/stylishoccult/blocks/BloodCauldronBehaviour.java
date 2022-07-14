package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.BloodCauldronBlockEntity;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BloodCauldronBehaviour {

	private static final List<Map<Item, CauldronBehavior>> MAPS = new ArrayList<>();

	private static final CauldronBehavior BUCKET_FILL_WITH_BLOOD = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient) {
			placeBloodCauldron(world, pos, player, hand, stack, FluidConstants.BUCKET, SoundEvents.ITEM_BUCKET_EMPTY);
		}

		return ActionResult.success(world.isClient);
	};

	private static final CauldronBehavior BOTTLE_FILL_WITH_BLOOD = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient) {
			placeBloodCauldron(world, pos, player, hand, stack, FluidConstants.BOTTLE, SoundEvents.ITEM_BOTTLE_EMPTY);
		}

		return ActionResult.success(world.isClient);
	};

	private static void placeBloodCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, long amount, SoundEvent event) {
		Item item = stack.getItem();
		player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
		player.incrementStat(Stats.USE_CAULDRON);
		player.incrementStat(Stats.USED.getOrCreateStat(item));
		world.setBlockState(pos, ModBlocks.BLOOD_CAULDRON.getDefaultState());
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1.0f, 1.0f);
		BlockUtils.getEntity(BloodCauldronBlockEntity.class, world, pos).getStorage().insert(amount);
	}

	public static void init() {
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(ModItems.BLOOD_BOTTLE, BloodCauldronBehaviour.BOTTLE_FILL_WITH_BLOOD);
		CauldronBehavior.registerBucketBehavior(BloodCauldronBlock.BEHAVIORS);

		for (Map<Item, CauldronBehavior> map : MAPS) {
			map.put(ModItems.BLOOD_BUCKET, BloodCauldronBehaviour.BUCKET_FILL_WITH_BLOOD);
		}
	}

	public static void addBehaviourMap(Map<Item, CauldronBehavior> map) {
		MAPS.add(map);
	}

}
