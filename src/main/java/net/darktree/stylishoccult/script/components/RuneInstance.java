package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.nbt.NbtCompound;

public class RuneInstance {

    public final RuneBlock rune;

    public RuneInstance(RuneBlock rune) {
        this.rune = rune;
    }

    public boolean push( RunicScript script, RuneInstance instance ) {
        return false;
    }

    public NbtCompound toTag( NbtCompound tag ) {
        tag.putString("rune", rune.name);
        return tag;
    }

    public void fromTag( NbtCompound tag ) {

    }

}
