package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;

public enum LavaDemonPart implements StringIdentifiable {
    BODY("body"),
    HEAD("head"),
    EMITTER("emitter");

    private final String name;

    LavaDemonPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
