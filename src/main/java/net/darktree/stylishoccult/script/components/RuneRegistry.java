package net.darktree.stylishoccult.script.components;

import java.util.HashMap;

public class RuneRegistry {

    private static final HashMap<String, Rune> runes = new HashMap<>();

    public static Rune put( String name, Rune rune ) {
        return runes.put( name, rune );
    }

    public static Rune get( String name ) {
        return runes.get( name );
    }

}
