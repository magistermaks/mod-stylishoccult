package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.loot.LootTable;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class ConditionEntry extends AbstractEntry {

    private final Condition condition;
    private final LootTable table, elseTable;

    public ConditionEntry( Condition condition ) {
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
    public ArrayList<ItemStack> getLoot(Random random, LootContext context) {
        if( condition.call(random, context) ) {
            return this.table.getLoot(random, context);
        }else{
            return this.elseTable.getLoot(random, context);
        }
    }
}
