package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.elements.StackElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public final class Ring {

	private final StackElement[] buffer;
	private int offset;

	public Ring(int capacity) {
		this.buffer = new StackElement[capacity];
		this.offset = 0;
	}

	private void move(int distance) {
		this.offset = (this.offset + distance) % this.buffer.length;
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
			throw RuneExceptionType.INVALID_STATE.get();
		}

		return element;
	}

	/**
	 * Serialize the ring to {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		for(int i = 0; i < buffer.length; i ++) {
			NbtCompound entry = new NbtCompound();

			if(buffer[i] != null) {
				buffer[i].writeNbt(entry);
			}

			nbt.put(String.valueOf(i), entry);
		}

		nbt.putShort("i", (short) offset);
		return nbt;
	}

	/**
	 * Deserialize the ring from {@link NbtCompound}
	 */
	public void readNbt(NbtCompound nbt) {
		try {
			for(int i = 0; i < buffer.length; i ++) {
				NbtCompound entry = nbt.getCompound(String.valueOf(i));
				buffer[offset] = entry.isEmpty() ? null : StackElement.from(entry);
			}

			offset = nbt.getShort("i") % buffer.length;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
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
	public void reset(Consumer<StackElement> consumer) {
		for(StackElement element : buffer) {
			if(element != null) consumer.accept(element);
		}
	}

}
