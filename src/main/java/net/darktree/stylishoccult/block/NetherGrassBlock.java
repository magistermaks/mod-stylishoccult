package net.darktree.stylishoccult.block;

import net.darktree.interference.api.DropsItself;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SproutsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class NetherGrassBlock extends SproutsBlock implements DropsItself {

    public NetherGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.getBlock() == Blocks.NETHERRACK || super.canPlantOnTop(floor, world, pos);
    }

}

