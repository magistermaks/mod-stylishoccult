package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LeavesFleshBlock extends LeavesBlock implements ImpureBlock, FoliageFleshBlock {

    public LeavesFleshBlock() {
        super(AbstractBlock.Settings.of(Material.LEAVES)
                .strength(0.2F)
                .slipperiness(0.8f)
                .sounds(BlockSoundGroup.HONEY)
                .nonOpaque()
                .allowsSpawning((a, b, c, d) -> false)
                .suffocates((a, b, c) -> false)
                .blockVision((a, b, c) -> false)
        );
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 20;
    }

    public static BlockState getStateForPosition( World world, BlockPos pos ) {
        BlockState state = world.getBlockState(pos);
        int distance = ( state.getBlock() instanceof LeavesBlock ) ? state.get( DISTANCE ) : 7;
        return state.getBlock().getDefaultState().with(DISTANCE, distance);
    }

}
