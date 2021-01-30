package net.darktree.stylishoccult.loot;

@Deprecated
public class BakedLootTable {

    private final LootTable table;

    public static BakedLootTable bake(LootTable table ) {
        return new BakedLootTable( table );
    }

    private BakedLootTable(LootTable table) {
        this.table = table;
    }

    public LootTable table() {
        return table;
    }

}
