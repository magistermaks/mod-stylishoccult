package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.elements.NumericElement;
import net.darktree.stylishoccult.script.elements.StackElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class Script {

	private RuneInstance instance = null;

	public final Ring ring = new Ring(8);
	public final Stack stack = new Stack(32);
	public StackElement value = NumericElement.FALSE;
	public Direction direction;

	/**
	 * Deserialize the script state from the given {@link NbtCompound}
	 */
	public static Script fromNbt(NbtCompound nbt) {
		Script script = new Script();

		if(nbt.contains("d")) script.direction = Direction.byId(nbt.getByte("d"));
		if(nbt.contains("i")) script.instance = RuneInstance.from(nbt.getCompound("i"));
		script.value = StackElement.from(nbt.getCompound("v"));
		script.stack.readNbt(nbt.getCompound("s"));
		script.ring.readNbt(nbt.getCompound("r"));

		return script;
	}

	/**
	 * Serializes the script state into the given {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		if( direction != null) nbt.putByte("d", (byte) direction.getId());
		if( instance != null ) nbt.put("i", instance.writeNbt(new NbtCompound()));
		nbt.put("v", value.writeNbt(new NbtCompound()));
		nbt.put("s", stack.writeNbt(new NbtCompound()));
		nbt.put("r", ring.writeNbt(new NbtCompound()));

		return nbt;
	}

	/**
	 * Execute the rune at given position
	 */
	public void apply(RuneBlock rune, World world, BlockPos pos) {
		try {
			push(rune);
			rune.apply(this, world, pos);
			stack.validate();
		} catch (RuneException exception) {
			exception.apply(world, pos);
			reset(world, pos);
		}
	}

	/**
	 * Copy the script for given direction
	 */
	public Script copyFor(Direction direction) {
		Script script = new Script();
		script.direction = direction;
		script.value = value.copy();
		script.stack.from(stack);
		script.ring.from(ring);
		script.instance = this.instance == null ? null : this.instance.copy();
		return script;
	}

	/**
	 * Helper method for setting the direction
	 */
	public Script with(Direction direction) {
		this.direction = direction;
		return this;
	}

	/**
	 * Copy the newest value from the drop stack back onto the stack
	 */
	public void ascend() {
		stack.push(ring.pull());
	}

	/**
	 * 'Pull & drop', get the newest value from stack, return it, and add to drop stack
	 */
	public StackElement pull(World world, BlockPos pos) {
		return ring.push(stack.pull(), world, pos);
	}

	/**
	 * Drop all elements from this script, called when execution concludes, with
	 * exception or by reaching the end of line.
	 */
	public void reset(World world, BlockPos pos) {
		this.stack.reset(element -> element.drop(world, pos));
		this.ring.reset(element -> element.drop(world, pos));
		this.value.drop(world, pos);
		this.value = NumericElement.FALSE;
		this.instance = null;
	}

	private void push(RuneBlock rune) {
		RuneInstance inst = rune.getInstance();

		if (instance == null) {
			instance = inst;
		} else {
			if(!instance.push(this, inst)) {
				instance = inst;
			}
		}
	}

}
