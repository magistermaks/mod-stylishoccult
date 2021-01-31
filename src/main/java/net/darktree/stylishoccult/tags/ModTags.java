package net.darktree.stylishoccult.tags;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public class ModTags {

    public static Tag<Block> RUNES;
    public static Tag<Block> CORRUPTIBLE;

    public void init() {
        RUNES = TagRegistry.block( new ModIdentifier("runes") );
        CORRUPTIBLE = TagRegistry.block( new ModIdentifier("corruptible") );
    }

}
