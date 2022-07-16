package net.darktree.stylishoccult.block.property;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.StringIdentifiable;

public enum LavaDemonMaterial implements StringIdentifiable {
    STONE("stone", Blocks.STONE),
    COAL("coal", Blocks.COAL_ORE),
    IRON("iron", Blocks.IRON_ORE),
    LAPIS("lapis", Blocks.LAPIS_ORE),
    REDSTONE("redstone", Blocks.REDSTONE_ORE),
    GOLD("gold", Blocks.GOLD_ORE),
    EMERALD("emerald", Blocks.EMERALD_ORE),
    DIAMOND("diamond", Blocks.DIAMOND_ORE);

    public int getLevel() {
        return switch (this) {
            case STONE -> 1;
            case COAL -> 2;
            case IRON -> 3;
            case LAPIS -> 4;
            case REDSTONE -> 5;
            case GOLD -> 6;
            case EMERALD -> 7;
            default -> 8;
        };
    }

    public static LavaDemonMaterial getFrom( Block block ) {
        if( block == Blocks.STONE ) return STONE;
        if( block == Blocks.COAL_ORE ) return COAL;
        if( block == Blocks.IRON_ORE ) return IRON;
        if( block == Blocks.LAPIS_ORE ) return LAPIS;
        if( block == Blocks.REDSTONE_ORE ) return REDSTONE;
        if( block == Blocks.GOLD_ORE ) return GOLD;
        if( block == Blocks.EMERALD_ORE ) return EMERALD;
        if( block == Blocks.DIAMOND_ORE ) return DIAMOND;
        return null;
    }

    private final String name;
    private final Block block;

    LavaDemonMaterial(String name, Block block) {
        this.name = name;
        this.block = block;
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
}
