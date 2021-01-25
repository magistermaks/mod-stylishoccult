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

    public Direction[] directions( World world, BlockPos pos, RunicScript script ) {
        Direction dir = script.getDirection();
        return  dir == null ? new Direction[] {} : new Direction[] { dir };
    }

    public RuneInstance getInstance() {
        return new RuneInstance(this);
    }

}
