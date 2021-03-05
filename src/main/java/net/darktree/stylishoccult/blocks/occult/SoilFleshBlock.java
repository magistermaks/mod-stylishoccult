package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SoilFleshBlock extends SimpleBlock implements ImpureBlock, FullFleshBlock {

    public SoilFleshBlock() {
        this(RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, true ).slipperiness(0.7f).ticksRandomly());
    }

    public SoilFleshBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        OccultHelper.corruptAround(world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 32;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.GENERIC_FLESH;
    }
}
