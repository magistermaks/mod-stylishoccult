package net.darktree.stylishoccult.blocks.occult.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ImpureBlock {

    void cleanse(World world, BlockPos pos, BlockState state);

    default int impurityLevel(BlockState state) {
        return 1;
    }

}
