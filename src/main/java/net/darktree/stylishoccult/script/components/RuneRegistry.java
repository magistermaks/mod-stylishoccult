package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;

import java.util.HashMap;

public class RuneRegistry {

    private static final HashMap<String, RuneBlock> runes = new HashMap<>();

    public static RuneBlock put(String name, RuneBlock rune ) {
        return runes.put( name, rune );
    }

    public static RuneBlock get( String name ) {
        return runes.get( name );
    }

}
