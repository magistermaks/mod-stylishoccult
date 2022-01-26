package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;

public class FleshStone extends BuildingBlock {

	public FleshStone(Settings settings) {
		super(settings);
	}

	@Override
	public LootTable getInternalLootTableId() {
		return LootTables.STONE_FLESH;
	}

}
