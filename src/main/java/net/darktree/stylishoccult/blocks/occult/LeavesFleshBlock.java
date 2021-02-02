package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LeavesFleshBlock extends LeavesBlock implements ImpureBlock {

    public LeavesFleshBlock() {
        super(AbstractBlock.Settings.of(Material.LEAVES)
                .strength(0.2F)
                .ticksRandomly()
                .sounds(BlockSoundGroup.HONEY)
                .nonOpaque()
                .allowsSpawning((a, b, c, d) -> false)
                .suffocates((a, b, c) -> false)
                .blockVision((a, b, c) -> false)
        );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        OccultHelper.corruptAround(world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);
        world.setBlockState(pos, ModBlocks.ARCANE_ASH.getDefaultState());
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 20;
    }

}
