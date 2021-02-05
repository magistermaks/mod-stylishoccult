package net.darktree.stylishoccult.tags;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {

    private static Tag<Block> common( String name ) {
        return TagRegistry.block( new Identifier( "c", name ) );
    }

    private static Tag<Block> internal( String name ) {
        return TagRegistry.block( new ModIdentifier( name ) );
    }

    // lists all rune blocks
    public static Tag<Block> RUNES;

    // lists all flesh blocks, used for corruption level calculation
    public static Tag<Block> FLESH;

    // empty by default, blocks in this list are made corruptible
    public static Tag<Block> CORRUPTIBLE;

    // empty by default, blocks in this list are made incorruptible
    public static Tag<Block> INCORRUPTIBLE;

    // lists all "top_soil" blocks (grass_block, nylium blocks etc.), makes them turn to soil_flesh not default_flesh
    public static Tag<Block> TOP_SOIL;

    public static void init() {
        RUNES = internal("runes");
        FLESH = internal("flesh");
        CORRUPTIBLE = internal("corruptible");
        INCORRUPTIBLE = internal("incorruptible");
        TOP_SOIL = common("top_soil");
    }

}
