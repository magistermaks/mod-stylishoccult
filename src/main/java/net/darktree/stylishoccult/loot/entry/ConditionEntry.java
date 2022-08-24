package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootTable;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public class ConditionEntry extends AbstractEntry {

	private final Condition condition;
	private final LootTable table, elseTable;

	public static boolean hasSilkTouch(Random random, LootContext context) {
		return context.toolHasEnchantment(Enchantments.SILK_TOUCH);
	}

	public static boolean hasFortune(Random random, LootContext context) {
		return context.toolHasEnchantment(Enchantments.FORTUNE);
	}

	public ConditionEntry(Condition condition) {
		this.condition = condition;
		this.table = new LootTable();
		this.elseTable = new LootTable();
	}

	public LootTable getTable() {
		return this.table;
	}

	public LootTable getElseTable() {
		return this.elseTable;
	}

	@Override
	public List<ItemStack> getLoot(Random random, LootContext context) {
		if (condition.call(random, context)) {
			return this.table.getLoot(random, context);
		} else {
			return this.elseTable.getLoot(random, context);
		}
	}

	public interface Condition {
		boolean call(Random random, LootContext context);
	}
}
