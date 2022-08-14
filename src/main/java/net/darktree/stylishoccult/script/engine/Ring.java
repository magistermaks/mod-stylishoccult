package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.StackElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public final class Ring extends BaseStack {

	private final StackElement[] buffer;
	private int offset;

	public Ring(int capacity) {
		super(capacity);
		this.buffer = new StackElement[capacity];
		this.offset = 0;
	}

	private void move(int distance) {
		this.offset = (this.offset + distance) % this.buffer.length;

		if (this.offset < 0) {
			this.offset = this.buffer.length - 1;
		}
	}

	/**
	 * Push one element onto the ring
	 */
	public StackElement push(StackElement element, World world, BlockPos pos) {
		if( buffer[offset] != null ) {
			buffer[offset].drop(world, pos);
		}

		buffer[offset] = element;
		move(1);

		return element;
	}

	/**
	 * Take one element from the ring
	 */
	public StackElement pull() {
		move(-1);
		StackElement element = buffer[offset];
		buffer[offset] = null;

		if(element == null) {
			throw RuneException.of(RuneExceptionType.NOTHING_TO_RETURN);
		}

		return element;
	}

	/**
	 * Serialize the ring to {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList list = new NbtList();

		for (StackElement element : buffer) {
			list.add(element != null ? element.writeNbt(new NbtCompound()) : new NbtCompound());
		}

		nbt.put("r", list);
		nbt.putShort("i", (short) offset);
		return nbt;
	}

	/**
	 * Deserialize the ring from {@link NbtCompound}
	 */
	public void readNbt(NbtCompound nbt) {
		NbtList list = nbt.getList("r", NbtElement.COMPOUND_TYPE);
		int size = Math.min(list.size(), capacity);

		for (int i = 0; i < size; i ++) {
			NbtCompound entry = (NbtCompound) list.get(i);
			buffer[i] = entry.isEmpty() ? null : StackElement.from(entry);
		}

		offset = nbt.getShort("i") % buffer.length;
	}

	/**
	 * Copy elements from other stack
	 */
	public void from(Ring ring) {
		for(int i = 0; i < buffer.length; i ++) {
			buffer[i] = ring.buffer[i] == null ? null : ring.buffer[i].copy();
		}

		this.offset = ring.offset;
	}

	/**
	 * Reset this ring, notifies the consumer of every dropped element
	 */
	@Override
	public void reset(Consumer<StackElement> consumer) {
		for(StackElement element : buffer) {
			if(element != null) consumer.accept(element);
		}
	}

}
