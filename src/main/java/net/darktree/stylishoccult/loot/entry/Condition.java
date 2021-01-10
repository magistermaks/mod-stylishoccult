package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;

import java.util.Random;

public interface Condition {
    boolean call(Random random, LootContext context);
}
