package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneAnalogInputRuneBlock extends InputRuneBlock {

    public RedstoneAnalogInputRuneBlock(String name) {
        super(name);
    }

    @Override
    public void apply(RunicScript script, World world, BlockPos pos) {
        script.getStack().put( world.getReceivedRedstonePower(pos) );
    }

}
