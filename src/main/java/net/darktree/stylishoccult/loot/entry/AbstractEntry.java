package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractEntry {

    public abstract ArrayList<ItemStack> getLoot(Random random, LootContext context);

    protected ArrayList<ItemStack> asList( ItemStack stack ) {
        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(stack);
        return list;
    }

}
