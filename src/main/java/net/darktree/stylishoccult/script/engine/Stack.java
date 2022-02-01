package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.elements.StackElement;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class Stack {

	private final int capacity;
	private final ArrayList<StackElement> stack = new ArrayList<>();

	public Stack(int capacity) {
		this.capacity = capacity;
	}

	public StackElement peek(int i) {
		return stack.get(stack.size() - i - 1);
	}

	/**
	 * pull (a, b, c -> a, b) => c
	 */
	public StackElement pull() {
		return stack.remove(stack.size() - 1);
	}

	/**
	 * push c (a, b -> a, b, c) => void
	 */
	public void push(StackElement element) {
		stack.add(element);
	}

	/**
	 * swap (a, b, c -> a, c, b) => void
	 */
	public void swap() {
		push(stack.remove(stack.size() - 2));
	}

	/**
	 * duplicate (a, b -> a, b, b) => void
	 */
	public void duplicate() throws RuneException {
		push(peek(0).copy());
	}

	/**
	 * Check if the stack hasn't grown too large
	 */
	public void validate() {
		if(stack.size() > this.capacity) {
			throw RuneExceptionType.STACK_TOO_BIG.get();
		}
	}

	/**
	 * Reset this stack, notifies the consumer of every dropped element
	 */
	public void reset(Consumer<StackElement> consumer) {
		int length = stack.size();

		for(int i = 0; i < length; i ++) {
			consumer.accept(pull());
		}
	}

	/**
	 * Get stack size
	 */
	@Deprecated
	public int size() {
		return stack.size();
	}

	/**
	 * Serialize the stack to {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		int length = stack.size();

		for(int i = 0; i < length; i ++) {
			NbtCompound entry = new NbtCompound();
			peek(i).writeNbt(entry);
			nbt.put(String.valueOf(i), entry);
		}

		return nbt;
	}

	/**
	 * Deserialize the stack from {@link NbtCompound}
	 */
	public void readNbt(NbtCompound nbt) {
		try {
			int size = nbt.getSize();

			for( int i = 0; i < size; i ++ ) {
				stack.add(StackElement.from(nbt.getCompound(String.valueOf(i))));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Copy elements from other stack
	 */
	public void from(Stack stack) {
		for(StackElement element : stack.stack) {
			this.stack.add(element.copy());
		}
	}

}
