package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.blocks.occult.ImpureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

}
