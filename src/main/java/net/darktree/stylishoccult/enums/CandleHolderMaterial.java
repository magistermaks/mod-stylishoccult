package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;

public enum CandleHolderMaterial implements StringIdentifiable {
    OAK("oak"),
    SPRUCE("spruce"),
    DARK_OAK("dark_oak"),
    ACACIA("acacia"),
    JUNGLE("jungle"),
    BIRCH("birch");

    private final String name;

    CandleHolderMaterial(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
