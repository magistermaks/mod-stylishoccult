package net.darktree.stylishoccult.block.entity.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class RuneBlockEntity extends SimpleBlockEntity {

	private final RuneBlockAttachment attachment;
	private Script script;

	public RuneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.RUNESTONE, pos, state);

		script = null;
		attachment = new RuneBlockAttachment(this::markDirty);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		if (script != null) nbt.put("s", script.writeNbt(new NbtCompound()));
		if (!attachment.isEmpty()) nbt.put("a", attachment.writeNbt(new NbtCompound()));
		return super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		try {
			if (nbt.contains("s")) script = Script.fromNbt(nbt.getCompound("s"));
			if (nbt.contains("a")) attachment.readNbt(nbt.getCompound("a"));
		} catch (Exception exception) {
			StylishOccult.LOGGER.error("Failed to deserialize rune block entity from NBT!", exception);
		}
		super.readNbt(nbt);
	}

	public void store(Script script) {
		this.script = script;
		markDirty();
	}

	public void clear() {
		script = null;
		markDirty();
	}

	public Script getScript() {
		return script;
	}

	public boolean hasScript() {
		return script != null;
	}

	public RuneBlockAttachment getAttachment() {
		return attachment;
	}

}
