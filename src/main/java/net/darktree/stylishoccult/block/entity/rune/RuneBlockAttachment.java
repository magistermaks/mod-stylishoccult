package net.darktree.stylishoccult.block.entity.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.nbt.NbtCompound;

public final class RuneBlockAttachment {

	private Script script;
	private NbtCompound nbt;
	private final RuneBlockEntity entity;

	RuneBlockAttachment(RuneBlockEntity entity) {
		this.entity = entity;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
		entity.markDirty();
	}

	public NbtCompound getNbt() {
		return nbt;
	}

	public void setNbt(NbtCompound nbt) {
		this.nbt = nbt;
		entity.markDirty();
	}

	public void clear() {
		setNbt(null);
		setScript(null);
	}

	boolean isEmpty() {
		return script == null && nbt == null;
	}

	NbtCompound writeNbt(NbtCompound tag) {
		if(script != null) tag.put("script", script.writeNbt(new NbtCompound()));
		if(nbt != null) tag.put("tag", nbt);
		return tag;
	}

	void readNbt(NbtCompound nbt) {
		try {
			if (nbt.contains("script")) this.script = Script.fromNbt(nbt.getCompound("script"));
			if (nbt.contains("tag")) this.nbt = nbt.getCompound("tag");
		} catch (Exception exception) {
			StylishOccult.LOGGER.error("Failed to deserialize rune block data attachment from NBT!", exception);
		}
	}

}
