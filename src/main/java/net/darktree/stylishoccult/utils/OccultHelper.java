package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.*;
import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.tags.ModTags;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class OccultHelper {

    public static void corruptAround(ServerWorld world, BlockPos pos, Random random, boolean far) {
        BlockPos target = pos.offset( RandUtils.getEnum(Direction.class, random) );

        if(far) {
            int i = 0;

            while (random.nextInt(4) == 0 && (i < 5)) {
                target = target.offset(RandUtils.getEnum(Direction.class, random));
                i++;
            }
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

        if (block instanceof ImpureBlock impurity) {
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
        boolean corruptible = ModTags.CORRUPTIBLE.contains(block);

        if( !state.isAir() ) {
            if(corruptible || (canCorrupt(state, hardness) && requiredCheck(block, hardness))) {
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
        if( hardness < 1.5 ) return RandUtils.getBool(51.0f);
        if( hardness < 2.0 ) return RandUtils.getBool(5.5f);
        if( hardness < 2.5 ) return RandUtils.getBool(1.0f);
        return RandUtils.getBool(0.1f);
    }

    private static boolean requiredCheck( Block block, float hardness ) {
        return !(block instanceof ImpureBlock) && !ModTags.INCORRUPTIBLE.contains(block) && hardness >= 0 && hardness <= 1000;
    }

    public static boolean touchesSource(World world, BlockPos pos) {
        for( Direction dir : Direction.values() ) {
            if( world.getBlockState(pos.offset(dir)).getBlock() instanceof FullFleshBlock) {
                return true;
            }
        }

        return false;
    }

    private static boolean standsOnSource(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof FullFleshBlock || world.getBlockState(pos.down()).getBlock() instanceof FullFleshBlock;
    }

    private static void spawnCorruption(World world, BlockPos target, BlockState state) {
        BlockState corruption = getCorruptionForBlock( world, target, state );
        if( corruption != null ) {
            world.setBlockState(target, corruption);
        }
    }

    private static BlockState getCorruptionForBlock( World world, BlockPos pos, BlockState state ) {
        if( state.isAir() || state.getBlock() instanceof PlantBlock ) {
            if (RandUtils.getBool(33.3f) && shouldSpawnFoliage(world, pos) ) {
                int type = RandUtils.rangeInt(0, 8);

                if( type == 0 && validTentacleSpot(world, pos) ) {
                    return ModBlocks.TENTACLE.getDefaultState().with(TentacleBlock.SIZE, RandUtils.rangeInt(3, 6));
                }

                if( type == 1 && validDownSpot(world, pos) ) {
                    return ModBlocks.EYES_FLESH.getDefaultState().with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3));
                }

                if( type == 2 && validDownSpot(world, pos) ) {
                    return ModBlocks.WARTS_FLESH.getDefaultState().with(EyesBlock.SIZE, RandUtils.rangeInt(1, 3));
                }

                if( type == 3 && RandUtils.getBool(55.6f) ) {
                    if( BlockUtils.countInArea(world, pos, VentBlock.class, 5) == 0 ) {
                        return ((VentBlock) ModBlocks.SPORE_VENT).getStateToFit(world, pos);
                    }
                }

                if( type == 4 || type == 5 ) {
                    return ((ThinFleshBlock) ModBlocks.GROWTH).getStateToFit(world, pos);
                }

                if( type >= 6 && validDownSpot(world, pos) ) {
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
                if( state.getLuminance() > 3 && state.isFullCube(world, pos) ) return ModBlocks.GLOW_FLESH.getDefaultState();
                if( BlockTags.LEAVES.contains(block) ) return LeavesFleshBlock.getStateForPosition(world, pos);
                if( ModTags.TOP_SOIL.contains(block) && RandUtils.getBool(80) ) return ModBlocks.SOIL_FLESH.getDefaultState();

                return ModBlocks.FLESH_PASSIVE.getDefaultState().with(PassiveFleshBlock.BLOODY, RandUtils.getBool(StylishOccult.SETTINGS.fleshBloodChance));
            }
        }

        return null;
    }

    public static void cleanseFlesh(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, state.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1, 1);
        world.setBlockState( pos, Blocks.AIR.getDefaultState() );
    }

    private static boolean shouldSpawnFoliage( World world, BlockPos pos ) {
        return BlockUtils.countInArea(world, pos, FoliageFleshBlock.class, 1) <= 1;
    }

    private static boolean validTentacleSpot( World world, BlockPos pos ) {
        return (world.getBlockState(pos.up()).getBlock() instanceof FullFleshBlock) || (world.getBlockState(pos.down()).getBlock() instanceof FullFleshBlock);
    }

    private static boolean validDownSpot( World world, BlockPos pos ) {
        return (world.getBlockState(pos.down()).getBlock() instanceof FullFleshBlock);
    }

    public static void sacrifice(World world, BlockPos pos, LivingEntity entity) {
        if(entity instanceof SparkEntity) {
            return;
        }

        if( standsOnSource(world, pos) ) {
            int count = world.random.nextInt(4) + 1;

            for(int i = 0; i < count; i ++) {
                corruptAround((ServerWorld) world, pos, world.getRandom(), false);
            }

            ascendFlesh(world, pos);
        }
    }

    public static void ascendFlesh(World world, BlockPos center) {
        final BlockPos.Mutable pos = new BlockPos.Mutable();
        final int min = -3, max = 3;
        final int cx = center.getX(), cy = center.getY(), cz = center.getZ();

        for(int x = min; x < max; x ++) {
            for(int y = min; y < max; y ++) {
                for(int z = min; z < max; z ++) {
                    if( RandUtils.getBool(Math.max(MathHelper.fastInverseSqrt(x * x + y * y + z * z) * 50 - 8, 1), world.random) ) {
                        pos.set(cx + x, cy + y, cz + z);

                        if(world.getBlockState(pos).getBlock() == ModBlocks.FLESH_PASSIVE) {
                            world.setBlockState(pos, ModBlocks.DEFAULT_FLESH.getDefaultState());
                        }
                    }
                }
            }
        }
    }

}
