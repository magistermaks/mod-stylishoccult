package net.darktree.stylishoccult.script.runes;

import net.darktree.stylishoccult.script.components.Rune;

public class Runes {

    public static final int COLOR_0 = 0x4d0000;
    public static final int COLOR_1 = 0x660000;
    public static final int COLOR_2 = 0x800000;
    public static final int COLOR_3 = 0x990000;

    public static final Rune NOOP_RUNE = new EmptyRune("noop");
    public static final Rune SCATTER_RUNE = new ScatterRune("scatter");
    public static final Rune REDIRECT_RUNE = new RedirectRune("redirect");
    public static final Rune FORK_RUNE = new ForkRune("fork");

    public static void init() {
        // load class
    }

}
