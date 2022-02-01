package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.nbt.NbtCompound;

public abstract class RuneInstance {

    public final RuneBlock rune;

    public RuneInstance(RuneBlock rune) {
        this.rune = rune;
    }

    public abstract RuneInstance copy();
    public abstract boolean push(Script script, RuneInstance instance);

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putString("rune", rune.name);
        return tag;
    }

    public void readNbt(NbtCompound tag) {

    }

    public static RuneInstance from(NbtCompound nbt) {
        RuneBlock rune = RuneRegistry.getRune(nbt.getString("rune"));

        if(rune != null) {
            RuneInstance instance = rune.getInstance();

            if(instance != null) {
                instance.readNbt(nbt);
            }

            return instance;
        }
        return null;
    }

}
