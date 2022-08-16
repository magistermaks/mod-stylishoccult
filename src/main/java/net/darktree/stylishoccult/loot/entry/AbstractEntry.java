package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractEntry {

	public abstract List<ItemStack> getLoot(Random random, LootContext context);

	protected List<ItemStack> asList(ItemStack stack) {
		return Collections.singletonList(stack);
	}

}
