package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class FleshBlock extends SimpleBlock implements ImpureBlock {

    public FleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, true ).slipperiness(0.8f).ticksRandomly() );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos target = pos.offset( RandUtils.getEnum(Direction.class, random) );

        if( random.nextInt( 6 ) == 0 ) {
            target = target.offset( RandUtils.getEnum(Direction.class, random) );
        }

        if( random.nextInt( 32 ) == 0 ) {
            target = target.offset( RandUtils.getEnum(Direction.class, random) );
        }

        if( random.nextInt( 128 ) == 0 ) {
            progress(world, pos);
        }

        OccultHelper.corrupt(world, target);
    }

    public void progress(World world, BlockPos pos) {
        StylishOccult.LOGGER.warn("Scary!");
        // TODO SPAWN OTHER FORMS
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);
        world.setBlockState(pos, ModBlocks.ARCANE_ASH.getDefaultState());
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 30;
    }

}
