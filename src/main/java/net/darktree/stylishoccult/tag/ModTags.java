package net.darktree.stylishoccult.tag;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {

    // lists all rune blocks
    public static Tag<Block> RUNES = TagFactory.BLOCK.create(ModIdentifier.of("runes"));

    // lists all flesh blocks
    public static Tag<Block> FLESH = TagFactory.BLOCK.create(ModIdentifier.of("flesh"));

    // empty by default, blocks in this list are made corruptible
    public static Tag<Block> CORRUPTIBLE = TagFactory.BLOCK.create(ModIdentifier.of("corruptible"));

    // empty by default, blocks in this list are made incorruptible
    public static Tag<Block> INCORRUPTIBLE = TagFactory.BLOCK.create(ModIdentifier.of("incorruptible"));

    // block that can be found in boulder features
    public static Tag<Block> BOULDER_FILLER = TagFactory.BLOCK.create(ModIdentifier.of("boulder_filler"));

    // all fluids that should use the blood overlays
    public static Tag<Fluid> BLOOD = TagFactory.FLUID.create(ModIdentifier.of("blood"));

    // lists all "top_soil" blocks (grass_block, nylium blocks etc.), makes them turn to soil_flesh not default_flesh
    public static Tag<Block> TOP_SOIL = TagFactory.BLOCK.create(new Identifier("c", "top_soil"));

    // lists all heat emitting blocks (fire, campfires, lava) if the block has a LIT property is must be set to true
    public static Tag<Block> HEAT_SOURCE = TagFactory.BLOCK.create(new Identifier("c", "heat_source"));

    public static void init() {
        // noop
    }

}
