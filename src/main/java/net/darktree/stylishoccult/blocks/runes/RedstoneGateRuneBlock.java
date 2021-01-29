package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RedstoneGateRuneBlock extends TransferRuneBlock {

    public RedstoneGateRuneBlock(String name) {
        super(name);
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, RunicScript script) {
        if( world.isReceivingRedstonePower(pos) ) {
            return super.getDirections(world, pos, script);
        }else{
            return new Direction[] {};
        }
    }

}
