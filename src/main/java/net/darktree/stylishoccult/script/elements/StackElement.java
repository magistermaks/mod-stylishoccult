package net.darktree.stylishoccult.script.elements;

import net.darktree.stylishoccult.script.components.RuneRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class StackElement {

	/**
	 * Get the numeric value of this element
	 */
	public abstract double value();

	/**
	 * Get a copy of this element, or throw an exception if duplicating this element is not allowed
	 */
	public abstract StackElement copy();

	/**
	 * Save itself to the given tag
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putShort("id", RuneRegistry.getElementIdentifier(getClass()));
		return nbt;
	}

	/**
	 * Whether two elements are equal
	 */
	public boolean equals(StackElement element) {
		return value() == element.value();
	}

	/**
	 * Whether one element is bigger than the other
	 */
	public boolean moreThan(StackElement element) {
		return value() > element.value();
	}

	/**
	 * Whether one element is smaller than the other
	 */
	public boolean lessThan(StackElement element) {
		return value() < element.value();
	}

	/**
	 * Load StackElement from NBT data
	 */
	public static StackElement from(NbtCompound nbt) {
		return RuneRegistry.getElementFactory(nbt.getShort("id")).create(nbt);
	}

	/**
	 * Notifies that the stack was dropped from stack at the given pos
	 */
	public void drop(World world, BlockPos pos) {
		// by default do nothing about it
	}

	@FunctionalInterface
	public interface Factory {
		StackElement create(NbtCompound nbt);
	}

}
