package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public interface Valve {
    ArrayList<ItemStack> call(ArrayList<ItemStack> stacks, Random random, LootContext context);
}
