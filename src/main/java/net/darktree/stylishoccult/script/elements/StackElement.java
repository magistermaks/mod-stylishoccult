package net.darktree.stylishoccult.script.elements;

import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.components.RuneRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
	 * Split this element into a list of N parts
	 */
	public abstract List<StackElement> split(int split);

	/**
	 * Save itself to the given NBT tag
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putShort("id", RuneRegistry.getElementIdentifier(getClass()));
		return nbt;
	}

	/**
	 * Test whether two elements are equal
	 */
	public boolean equals(StackElement element) {
		return value() == element.value();
	}

	/**
	 * Test whether one element is larger than the other
	 */
	public boolean moreThan(StackElement element) {
		return value() > element.value();
	}

	/**
	 * Test whether one element is smaller than the other
	 */
	public boolean lessThan(StackElement element) {
		return value() < element.value();
	}

	/**
	 * Load StackElement from NBT tag
	 */
	public static StackElement from(NbtCompound nbt) {
		return RuneRegistry.getElementFactory(nbt.getShort("id")).create(nbt);
	}

	/**
	 * Notifies that the element was dropped from stack at the given pos
	 */
	public void drop(World world, BlockPos pos) {
		// by default do nothing about it
	}

	public <T extends StackElement> T cast(Class<T> clazz) {
		if( clazz.isInstance(this) ) {
			return clazz.cast(this);
		}

		throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_TYPE);
	}

	@FunctionalInterface
	public interface Factory {
		StackElement create(NbtCompound nbt);
	}

}
