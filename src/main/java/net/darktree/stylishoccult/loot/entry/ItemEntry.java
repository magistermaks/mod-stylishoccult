package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public class ItemEntry extends AbstractEntry {

	private final float chance;
	private final ItemStack stack;
	private final byte amountMin;
	private final byte amountMax;

	public ItemEntry(ItemStack item, float chance, byte amountMin, byte amountMax) {
		this.chance = chance;
		this.stack = item;
		this.amountMin = amountMin;
		this.amountMax = amountMax;
	}

	@Override
	public List<ItemStack> getLoot(Random random, LootContext context) {
		if (RandUtils.getBool(this.chance, random)) {
			ItemStack stack = this.stack.copy();
			stack.setCount(random.nextInt( (amountMax - amountMin) + 1 ) + amountMin);
			return asList(stack);
		}

		return LootManager.getEmpty();
	}

}
