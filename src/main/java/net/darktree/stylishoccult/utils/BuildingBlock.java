package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.loot.BakedLootTable;

public class BuildingBlock extends SimpleBlock {

    public BuildingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BakedLootTable getInternalLootTableId() {
        return LootTables.SIMPLE;
    }
}
