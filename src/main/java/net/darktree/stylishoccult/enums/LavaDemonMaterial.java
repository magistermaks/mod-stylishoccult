package net.darktree.stylishoccult.enums;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.StringIdentifiable;

public enum LavaDemonMaterial implements StringIdentifiable {
    STONE("stone"),
    COAL("coal"),
    IRON("iron"),
    LAPIS("lapis"),
    REDSTONE("redstone"),
    GOLD("gold"),
    EMERALD("emerald"),
    DIAMOND("diamond");

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

    LavaDemonMaterial(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
