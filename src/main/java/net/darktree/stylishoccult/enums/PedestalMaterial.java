package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;

public enum PedestalMaterial implements StringIdentifiable {
    GRANITE("granite"),
    DIORITE("diorite"),
    ANDESITE("andesite");

    private final String name;

    PedestalMaterial(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() { return this.name; }
}
