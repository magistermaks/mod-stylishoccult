package net.darktree.stylishoccult.script.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.Rune;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScatterRune extends Rune {

    public ScatterRune(String name) {
        super(name);
    }

    public Direction[] directions( World world, BlockPos pos, RunicScript script ) {
        return Direction.values();
    }

}
