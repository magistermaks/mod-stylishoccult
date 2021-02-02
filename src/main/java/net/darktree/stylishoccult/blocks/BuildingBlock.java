package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.SimpleBlock;

public class BuildingBlock extends SimpleBlock {

    public BuildingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.SIMPLE;
    }

}
