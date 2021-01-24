package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class Rune {

    public final String name;

    public Rune( String name ) {
        this.name = name;
    }

    public void apply(RunicScript script, World world, BlockPos pos) {
        apply(script);
    }

    public void apply(RunicScript script) {

    }

    public Direction[] directions( RunicScript script ) {
        return new Direction[] { script.getDirection() };
    }

    public RuneInstance getInstance() {
        return new RuneInstance(this);
    }

}
