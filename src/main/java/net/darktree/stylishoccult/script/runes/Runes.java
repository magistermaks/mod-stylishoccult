package net.darktree.stylishoccult.script.runes;

import net.darktree.stylishoccult.script.components.Rune;

public class Runes {

    public static Rune NOOP_RUNE = new EmptyRune("noop");
    public static Rune SCATTER_RUNE = new ScatterRune("scatter");

    public static void init() {

    }

}
