package net.darktree.stylishoccult.block.entity.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class RuneBlockEntity extends SimpleBlockEntity {

	private final RuneBlockAttachment attachment;
	private Script script;
	private Direction from;

	public RuneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.RUNESTONE, pos, state);

		script = null;
		attachment = new RuneBlockAttachment(this::markDirty);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		if (script != null) nbt.put("s", script.writeNbt(new NbtCompound()));
		if (from != null) nbt.putByte("f", (byte) from.getId());
		if (!attachment.isEmpty()) nbt.put("a", attachment.writeNbt(new NbtCompound()));
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		try {
			if (nbt.contains("s")) script = Script.fromNbt(nbt.getCompound("s"));
			if (nbt.contains("f")) from = Direction.byId(nbt.getByte("f"));
			if (nbt.contains("a")) attachment.readNbt(nbt.getCompound("a"));
		} catch (Exception exception) {
			StylishOccult.LOGGER.error("Failed to deserialize rune block entity from NBT!", exception);
		}
		super.readNbt(nbt);
	}

	public void store(Script script, @Nullable Direction from) {
		this.script = script;
		this.from = from;
		markDirty();
	}

	public void clear() {
		script = null;
		from = null;
		markDirty();
	}

	public Script getScript() {
		return script;
	}

	public boolean hasScript() {
		return script != null;
	}

	public Direction getDirection() {
		return from;
	}

	public RuneBlockAttachment getAttachment() {
		return attachment;
	}

}
