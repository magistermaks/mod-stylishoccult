package net.darktree.stylishoccult.script.element;

import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class NumericElement extends StackElement {

	public static final NumericElement TRUE = new NumericElement(1);
	public static final NumericElement FALSE = new NumericElement(0);

	final double value;

	public NumericElement(double value) {
		this.value = value;
	}

	public NumericElement(NbtCompound nbt) {
		this.value = nbt.getDouble("v");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putDouble("v", value);
		return super.writeNbt(nbt);
	}

	@Override
	public double value() {
		return value;
	}

	@Override
	public StackElement copy() {
		// this is allowed as state non-mutable
		return this;
	}

	@Override
	public List<StackElement> split(int split) {
		ArrayList<StackElement> list = new ArrayList<>();

		double val = value / split;

		for (int i = 0; i < split; i ++) {
			list.add(new NumericElement(val));
		}

		return list;
	}

	@Override
	public String toString() {
		return "NumericElement " + value;
	}
}
