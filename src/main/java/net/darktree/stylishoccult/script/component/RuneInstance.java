package net.darktree.stylishoccult.script.component;

import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.nbt.NbtCompound;

public abstract class RuneInstance {

	public final RuneBlock rune;

	public RuneInstance(RuneBlock rune) {
		this.rune = rune;
	}

	public abstract RuneInstance copy();
	public abstract RuneInstance choose(Script script, RuneInstance instance);

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
