package net.darktree.stylishoccult.block;

import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.sounds.Sounds;
import net.minecraft.block.LeveledCauldronBlock;
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
			placeBloodCauldron(world, pos, player, hand, stack, 3, SoundEvents.ITEM_BUCKET_EMPTY);
		}

		return ActionResult.success(world.isClient);
	};

	private static final CauldronBehavior BOTTLE_FILL_WITH_BLOOD = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient) {
			placeBloodCauldron(world, pos, player, hand, stack, 1, SoundEvents.ITEM_BOTTLE_EMPTY);
		}

		return ActionResult.success(world.isClient);
	};

	private static final CauldronBehavior ENCHANT = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient) {
			Item item = stack.getItem();
			incrementStats(player, item);
			Sounds.SPELL.play(world, pos);
			boolean boil = OccultCauldronBlock.shouldBoil(world.getBlockState(pos.down()));
			world.setBlockState(pos, ModBlocks.OCCULT_CAULDRON.getDefaultState().with(OccultCauldronBlock.BOILING, boil));
		}

		return ActionResult.success(world.isClient);
	};

	private static void incrementStats(PlayerEntity player, Item item) {
		player.incrementStat(Stats.USE_CAULDRON);
		player.incrementStat(Stats.USED.getOrCreateStat(item));
	}

	private static void placeBloodCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, int amount, SoundEvent event) {
		Item item = stack.getItem();
		player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
		incrementStats(player, item);
		world.setBlockState(pos, ModBlocks.BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, amount));
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1.0f, 1.0f);
	}

	public static void init() {
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(ModItems.BLOOD_BOTTLE, BOTTLE_FILL_WITH_BLOOD);
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(ModItems.OCCULT_STAFF, ENCHANT);
		CauldronBehavior.registerBucketBehavior(BloodCauldronBlock.BLOOD_BEHAVIOURS);

		for (Map<Item, CauldronBehavior> map : MAPS) {
			map.put(ModItems.BLOOD_BUCKET, BUCKET_FILL_WITH_BLOOD);
		}
	}

	public static void addBehaviourMap(Map<Item, CauldronBehavior> map) {
		MAPS.add(map);
	}

}
