package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class WormsBlock extends SimpleBlock implements ImpureBlock, FoliageFleshBlock {

    public WormsBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).noCollision().ticksRandomly() );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( RandUtils.getBool(60) && BlockUtils.countInArea(world, pos, WormsBlock.class, 2) < 4) {

            BlockPos target = pos
                    .offset( RandUtils.getEnum( Direction.class ) )
                    .offset( RandUtils.getEnum( Direction.class ) )
                    .offset( RandUtils.getEnum( Direction.class ) );

            BlockState stateTarget = world.getBlockState(target);
            BlockState stateDown = world.getBlockState(target.down());

            if( stateDown.getBlock() instanceof FullFleshBlock && (stateTarget.isAir() || stateTarget.getMaterial().isReplaceable()) ) {
                world.setBlockState( target, getDefaultState() );
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).isAir();
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 2;
    }

}
