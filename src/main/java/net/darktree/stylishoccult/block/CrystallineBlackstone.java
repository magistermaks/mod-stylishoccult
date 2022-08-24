package net.darktree.stylishoccult.block;

import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;

public class CrystallineBlackstone extends BuildingBlock {

	public CrystallineBlackstone(Settings settings) {
		super(settings);
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.CRYSTALLINE_BLACKSTONE;
	}

}
