package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.StackElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class Stack extends BaseStack {

	private final ArrayList<StackElement> stack = new ArrayList<>();

	public Stack(int capacity) {
		super(capacity);
	}

	public StackElement peek(int i) throws IndexOutOfBoundsException {
		return stack.get(stack.size() - i - 1);
	}

	/**
	 * pull (a, b, c -> a, b) => c
	 */
	public StackElement pull() {
		try {
			return stack.remove(stack.size() - 1);
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * push c (a, b -> a, b, c) => void
	 */
	public void push(StackElement element) {
		try {
			stack.add(element);
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * swap (a, b, c -> a, c, b) => void
	 */
	public void swap() {
		try {
			push(stack.remove(stack.size() - 2));
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * duplicate (a, b -> a, b, b) => void
	 */
	public void duplicate() {
		try {
			push(peek(0).copy());
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * over (a, b -> a, b, a) => void
	 */
	public void over() {
		try {
			push(stack.remove(stack.size() - 3));
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * rotate (a, b, c -> b, c, a) => void
	 */
	public void rotate() {
		try {
			push(stack.remove(0));
		} catch (Exception exception) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
		}
	}

	/**
	 * Check if the stack hasn't grown too large
	 */
	public void validate() {
		if(stack.size() > this.capacity) {
			throw RuneException.of(RuneExceptionType.STACK_TOO_LONG);
		}
	}

	/**
	 * Reset this stack, notifies the consumer of every dropped element
	 */
	@Override
	public void reset(Consumer<StackElement> consumer) {
		int length = stack.size();

		for (int i = 0; i < length; i ++) {
			consumer.accept(pull());
		}
	}

	/**
	 * Serialize the stack to {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList list = new NbtList();

		for (StackElement element : stack) {
			list.add(element.writeNbt(new NbtCompound()));
		}

		nbt.put("s", list);
		return nbt;
	}

	/**
	 * Deserialize the stack from {@link NbtCompound}
	 */
	public void readNbt(NbtCompound nbt) {
		NbtList list = nbt.getList("s", NbtElement.COMPOUND_TYPE);
		int size = Math.min(list.size(), capacity);

		for (int i = 0; i < size; i ++) {
			NbtCompound entry = (NbtCompound) list.get(i);
			stack.add(StackElement.from(entry));
		}
	}

	/**
	 * Copy elements from other stack
	 */
	public void from(Stack stack) {
		for (StackElement element : stack.stack) {
			this.stack.add(element.copy());
		}
	}

}
