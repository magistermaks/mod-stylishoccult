package net.darktree.stylishoccult.tags;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {

    // TODO: move to TagFactory, see BLOOD tag

    private static Tag<Block> common(String name) {
        return TagRegistry.block( new Identifier( "c", name ) );
    }

    private static Tag<Block> internal(String name) {
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

    // all fluids that should use the blood overlays
    public static Tag<Fluid> BLOOD = TagFactory.FLUID.create( new ModIdentifier("blood") );

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
