package net.darktree.stylishoccult.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

abstract public class SimpleBlockWithEntity extends SimpleBlock implements BlockEntityProvider {

    protected SimpleBlockWithEntity(Block.Settings settings) {
        super(settings);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

}
