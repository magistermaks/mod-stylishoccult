package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class GlowFleshBlock extends SoilFleshBlock {

    public GlowFleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.GLASS, 0.8F, 0.8F, true ).slipperiness(0.7f).ticksRandomly().luminance(14) );
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.GLOW_FLESH;
    }

}
