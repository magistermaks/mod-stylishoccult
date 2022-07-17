package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.script.element.StackElement;

import java.util.function.Consumer;

public abstract class BaseStack {

	protected final int capacity;

	protected BaseStack(int capacity) {
		this.capacity = capacity;
	}

	public abstract void reset(Consumer<StackElement> consumer);

}
