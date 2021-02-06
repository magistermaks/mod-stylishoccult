package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.EyesBlock;
import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.GooFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.blocks.occult.TentacleBlock;
import net.darktree.stylishoccult.tags.ModTags;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class OccultHelper {

    public static void corruptAround(ServerWorld world, BlockPos pos, Random random) {
        BlockPos target = pos.offset( RandUtils.getEnum(Direction.class, random) );

        if( random.nextInt( 6 ) == 0 ) {
            target = target.offset( RandUtils.getEnum(Direction.class, random) );
        }

        if( random.nextInt( 32 ) == 0 ) {
            target = target.offset( RandUtils.getEnum(Direction.class, random) );
        }

        corrupt(world, target);
    }

    public static boolean cleanseAround(World world, BlockPos pos, int ra, int rb, int power) {
        int x = RandUtils.rangeInt(-ra, ra);
        int y = RandUtils.rangeInt(-rb, rb);
        int z = RandUtils.rangeInt(-ra, ra);

        BlockPos target = pos.add(x, y, z);
        BlockState state = world.getBlockState(target);
        Block block = state.getBlock();

        if (block instanceof ImpureBlock) {
            ImpureBlock impurity = (ImpureBlock) block;

            int level = impurity.impurityLevel(state);

            if (world.random.nextInt(level) < world.random.nextInt(power)) {
                impurity.cleanse(world, target, state);
                return true;
            }
        }

        return false;
    }

    public static void corrupt(World world, BlockPos target) {
        BlockState state = world.getBlockState(target);
        Block block = state.getBlock();
        float hardness = state.getHardness(world, target);
        boolean corruptible = block.isIn(ModTags.CORRUPTIBLE);

        if( !state.isAir() ) {
            if( (corruptible || (canCorrupt(state, hardness) && requiredCheck(block, hardness))) && RandUtils.getBool(30.0f)) {
                spawnCorruption(world, target, state);
            }
        }else{
            if( touchesSource(world, target) ) {
                spawnCorruption(world, target, state);
            }
        }
    }

    private static boolean canCorrupt(BlockState state, float hardness) {
        Material material = state.getMaterial();
        return hardnessCheck(hardness) || ((material.isBurnable() || material.isReplaceable() || material == Material.ORGANIC_PRODUCT || material == Material.SOLID_ORGANIC) && RandUtils.getBool(30.0f));
    }

    private static boolean hardnessCheck( float hardness ) {
        if( hardness < 1.0 ) return RandUtils.getBool(93.0f);
        if( hardness < 1.5 ) return RandUtils.getBool(50.0f);
        if( hardness < 2.0 ) return RandUtils.getBool(5.5f);
        if( hardness < 2.5 ) return RandUtils.getBool(1.0f);
        return RandUtils.getBool(0.1f);
    }

    private static boolean requiredCheck( Block block, float hardness ) {
        return !(block instanceof ImpureBlock) && !block.isIn(ModTags.INCORRUPTIBLE) && hardness >= 0 && hardness <= 1000;
    }

    private static boolean touchesSource(World world, BlockPos pos) {
        for( Direction dir : Direction.values() ) {
            if( world.getBlockState(pos.offset(dir)).getBlock() instanceof FullFleshBlock ) {
                return true;
            }
        }

        return false;
    }

    private static void spawnCorruption(World world, BlockPos target, BlockState state) {
        BlockState corruption = getCorruptionForBlock( world, target, state );
        if( corruption != null ) {
            world.setBlockState(target, corruption);
        }
    }

    private static BlockState getCorruptionForBlock( World world, BlockPos pos, BlockState state ) {
        if( state.isAir() ) {
            if (RandUtils.getBool(10.0f) && shouldSpawnFoliage(world, pos) ) {
                int type = RandUtils.rangeInt(0, 5);

                if( type == 0 && validTentacleSpot(world, pos) ) {
                    return ModBlocks.TENTACLE.getDefaultState().with(TentacleBlock.SIZE, RandUtils.rangeInt(3, 6));
                }

                if( type == 1 && validDownSpot(world, pos) ) {
                    return ModBlocks.EYES_FLESH.getDefaultState().with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3));
                }

                if( type == 2 && validDownSpot(world, pos) ) {
                    return ModBlocks.WARTS_FLESH.getDefaultState().with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3));
                }

                if( type >= 3 && validDownSpot(world, pos) ) {
                    return ModBlocks.WORMS_FLESH.getDefaultState();
                }
            }
        }else{
            Block block = state.getBlock();

            if( block instanceof FluidBlock ) {
                if( RandUtils.getBool(25.0f) ) {
                    return ModBlocks.GOO_FLESH.getDefaultState().with(GooFleshBlock.TOP, world.getBlockState(pos.up()).isAir());
                }
            }else{
                if( block.isIn(BlockTags.LEAVES) ) return ModBlocks.LEAVES_FLESH.getDefaultState().with(LeavesBlock.DISTANCE, 1);
                if( block.isIn(ModTags.TOP_SOIL) && RandUtils.getBool(80) ) return ModBlocks.SOIL_FLESH.getDefaultState();
                return ModBlocks.DEFAULT_FLESH.getDefaultState();
            }
        }

        return null;
    }

    public static void cleanseFlesh(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, state.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1, 1);
        world.setBlockState( pos, Blocks.AIR.getDefaultState() );
    }

    private static boolean shouldSpawnFoliage( World world, BlockPos pos ) {
        return BlockUtils.countInArea(world, pos, FoliageFleshBlock.class, 4) == 0;
    }

    private static boolean validTentacleSpot( World world, BlockPos pos ) {
        return (world.getBlockState(pos.up()).getBlock() instanceof FullFleshBlock) != (world.getBlockState(pos.down()).getBlock() instanceof FullFleshBlock);
    }

    private static boolean validDownSpot( World world, BlockPos pos ) {
        return (world.getBlockState(pos.down()).getBlock() instanceof FullFleshBlock);
    }

}
