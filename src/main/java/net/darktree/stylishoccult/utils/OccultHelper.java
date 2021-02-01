package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.ImpureBlock;
import net.darktree.stylishoccult.tags.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OccultHelper {

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

        if( !state.isAir() && hardnessCheck(hardness) && requiredCheck(block) && (block.isIn(ModTags.CORRUPTIBLE) || canCorrupt(state)) ) {
            spawnCorruption(world, target, state);
        }
    }

    public static boolean canCorrupt(BlockState state) {
        Material material = state.getMaterial();
        return material.isBurnable() || material.isReplaceable() || material == Material.ORGANIC_PRODUCT || material == Material.SOLID_ORGANIC || RandUtils.getBool(30.0f);
    }

    public static boolean hardnessCheck( float hardness ) {
        if( hardness < 0 || hardness > 1000 ) return false;
        if( hardness < 1.0 ) return RandUtils.getBool(93.0f);
        if( hardness < 1.5 ) return RandUtils.getBool(50.0f);
        if( hardness < 2.0 ) return RandUtils.getBool(5.5f);
        if( hardness < 2.5 ) return RandUtils.getBool(0.1f);
        return RandUtils.getBool(0.06f);
    }

    public static boolean requiredCheck( Block block ) {
        return !(block instanceof ImpureBlock) && !block.isIn(ModTags.INCORRUPTIBLE);
    }

    private static void spawnCorruption(World world, BlockPos target, BlockState state) {
        world.setBlockState(target, ModBlocks.FLESH.getDefaultState());
        // TODO: USE STATE TO SELECT BETWEEN DIFFERENT FLESH TYPES (USING BLOCK TAGS)
    }

}
