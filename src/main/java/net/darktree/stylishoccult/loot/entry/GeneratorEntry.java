package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public class GeneratorEntry extends AbstractEntry {

	private final Generator generator;

	public GeneratorEntry(Generator generator) {
		this.generator = generator;
	}

	@Override
	public List<ItemStack> getLoot(Random random, LootContext context) {
		return generator.call(random, context);
	}

	public interface Generator {
		List<ItemStack> call(Random random, LootContext context);
	}
}
