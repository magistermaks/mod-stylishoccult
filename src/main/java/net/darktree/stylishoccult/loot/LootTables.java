package net.darktree.stylishoccult.loot;

import net.darktree.stylishoccult.block.ArcaneAshBlock;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.EyesBlock;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.loot.entry.ConditionEntry;
import net.darktree.stylishoccult.loot.entry.ValveEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collections;

public class LootTables {

	public static final LootTable SIMPLE = LootManager.create()
			.addBlockItem()
			.build();

	public static final LootTable SIMPLE_RESISTANT = LootManager.create()
			.ignoreExplosions()
			.addBlockItem()
			.build();

	public static final LootTable ASH = LootManager.create()
			.addCondition((rng, ctx) -> ctx.getState().get(ArcaneAshBlock.PERSISTENT))
				.addBlockItem()
				.pop()
			.build();

	public static final LootTable CRYSTALLINE_BLACKSTONE = LootManager.create()
			.addCondition(ConditionEntry::hasFortune)
				.addValve(ValveEntry::fortune)
					.addItem(Items.QUARTZ)
					.dropExperience(1, 3)
				.pop()
			.pop()
			.addElse()
				.addBlockItem()
			.pop()
			.build();

	public static final LootTable GROWTH = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addBlockItem()
			.pop()
			.addElse()
				.addValve(ValveEntry::fortune)
					.addItem(ModItems.VEINS, 77.0f)
					.addItem(ModItems.VEINS, 23.0f)
					.addItem(ModItems.VEINS, 10.0f)
					.minimum(1)
				.pop()
			.pop()
			.build();

	public static final LootTable LAVA_DEMON = LootManager.create()
			.addCondition((rng, ctx) -> ctx.getBlock() == ModBlocks.LAVA_DEMON && ctx.getState().get(LavaDemonBlock.PART) == LavaDemonPart.HEAD)
				.addItem(ModItems.LAVA_HEART)
			.pop()
			.addElse()
				.addCondition(ConditionEntry::hasSilkTouch)
					.addItem(ModItems.LAVA_STONE)
				.pop()
				.addElse()
					.addValve(ValveEntry::fortune)
						.addValve(ValveEntry::singular)
							.addItem(Items.DIAMOND, 1.9f)
							.addItem(Items.EMERALD, 20.0f)
							.addItem(Items.LAPIS_LAZULI, 7.0f)
							.addItem(Items.COAL, 40.4f)
							.addItem(Items.RAW_GOLD, 12.4f)
							.addItem(Items.RAW_IRON, 25.4f)
							.addItem(Items.RAW_COPPER, 28.4f)
							.minimum(1)
						.pop()
					.pop()
				.pop()
			.pop()
			.build();

	public static final LootTable STONE_FLESH = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addBlockItem()
			.pop()
			.addElse()
				.addItem(Items.COBBLESTONE)
				.addValve(ValveEntry::fortune)
					.addItem(new ItemStack(ModItems.VEINS), 100, 1, 2)
				.pop()
			.pop()
			.build();

	public static final LootTable GENERIC_FLESH = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addBlockItem()
			.pop()
			.addElse()
				.addItem(new ItemStack( ModItems.FLESH ), 100.0f, 1, 4)
			.pop()
			.build();

	public static final LootTable BONE_FLESH = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addBlockItem()
			.pop()
			.addElse()
				.addItem(ModItems.FLESH)
				.addItem(new ItemStack(ModItems.TWISTED_BONE), 100.0f, 1, 4)
				.dropExperience( 0, 3 )
			.pop()
			.build();

	public static final LootTable GLOW_FLESH = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addBlockItem()
			.pop()
			.addElse()
				.addItem(ModItems.FLESH)
				.addItem(new ItemStack(ModItems.GLOWGROWTH_SHARD), 100.0f, 1, 2)
			.pop()
			.build();

	public static final LootTable EYES_BlOCK = LootManager.create()
			.addGenerator((rng, ctx) -> {
				return Collections.singletonList(new ItemStack(ctx.getBlock(), ctx.getState().get(EyesBlock.SIZE)));
			})
			.build();

	public static final LootTable SPARK_VENT = LootManager.create()
			.addCondition(ConditionEntry::hasSilkTouch)
				.addItem(ModItems.SPARK_VENT)
			.pop()
			.addElse()
				.addItem(Items.NETHERRACK)
			.pop()
			.build();

	public static void init() {
		// load class
	}

}
