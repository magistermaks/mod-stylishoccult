package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RuneInstance {

    protected final Rune rune;

    public RuneInstance(Rune rune) {
        this.rune = rune;
    }

    public void update( RunicScript script, Rune rune, World world, BlockPos pos ) {

    }

    public boolean push( RuneInstance instance ) {
        return false;
    }

    public CompoundTag toTag() {
        return new CompoundTag();
    }

    public void fromTag( CompoundTag tag ) {
        tag.putString("rune", rune.name);
    }

}
