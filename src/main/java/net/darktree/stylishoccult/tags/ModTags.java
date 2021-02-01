package net.darktree.stylishoccult.tags;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public class ModTags {

    public static Tag<Block> RUNES;
    public static Tag<Block> FLESH;
    public static Tag<Block> CORRUPTIBLE;
    public static Tag<Block> INCORRUPTIBLE;

    public void init() {
        RUNES = TagRegistry.block( new ModIdentifier("runes") );
        FLESH = TagRegistry.block( new ModIdentifier("flesh") );
        CORRUPTIBLE = TagRegistry.block( new ModIdentifier("corruptible") );
        INCORRUPTIBLE = TagRegistry.block( new ModIdentifier("incorruptible") );
    }

}
