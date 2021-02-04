package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.BlockUtils;
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

public class FleshBlock extends SimpleBlock implements ImpureBlock {

    public FleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, true ).slipperiness(0.8f).ticksRandomly() );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        OccultHelper.corruptAround(world, pos, random);

        if( random.nextInt( 256 ) == 0 && (BlockUtils.countInArea(world, pos, ModBlocks.BONE_FLESH, 4) < 3) ) {
            if( !BlockUtils.touchesAir(world, pos) ) {
                world.setBlockState(pos, ModBlocks.BONE_FLESH.getDefaultState());
            }
        }
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 30;
    }

}
