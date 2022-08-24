package net.darktree.stylishoccult.block.property;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.StringIdentifiable;

public enum LavaDemonMaterial implements StringIdentifiable {
	STONE("stone", Blocks.STONE, 1),
	COAL("coal", Blocks.COAL_ORE, 2),
	IRON("iron", Blocks.IRON_ORE, 3),
	LAPIS("lapis", Blocks.LAPIS_ORE, 4),
	REDSTONE("redstone", Blocks.REDSTONE_ORE, 4),
	GOLD("gold", Blocks.GOLD_ORE, 7),
	EMERALD("emerald", Blocks.EMERALD_ORE, 8),
	DIAMOND("diamond", Blocks.DIAMOND_ORE, 10);

	public static LavaDemonMaterial getFrom(Block block) {
		for (LavaDemonMaterial material : values()) {
			if (material.block == block) return material;
		}

		return null;
	}

	private final String name;
	private final Block block;
	private final int level;

	LavaDemonMaterial(String name, Block block, int level) {
		this.name = name;
		this.block = block;
		this.level = level;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public Block asBlock() {
		return this.block;
	}

	public int getLevel() {
		return level;
	}

}
