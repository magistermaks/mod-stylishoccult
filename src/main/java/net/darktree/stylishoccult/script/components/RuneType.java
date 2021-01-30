package net.darktree.stylishoccult.script.components;

public enum RuneType {
    ENTRY("entry"),
    INPUT("input"),
    ACTOR("output"),
    TRANSFER("transfer"),
    LOGIC("logic");

    final String name;

    RuneType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
