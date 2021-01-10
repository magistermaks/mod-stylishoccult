package net.darktree.stylishoccult.loot;

import net.minecraft.util.Identifier;

public class BakedLootTable {

    private final Identifier identifier;
    private final LootTable table;

    public static BakedLootTable bake( Identifier id, LootTable table ) {
        LootManager.addLootTable( id, table );
        return new BakedLootTable( id, table );
    }

    private BakedLootTable(Identifier identifier, LootTable table) {
        this.identifier = identifier;
        this.table = table;
    }

    public Identifier identifier() {
        return identifier;
    }

    public LootTable table() {
        return table;
    }

}
