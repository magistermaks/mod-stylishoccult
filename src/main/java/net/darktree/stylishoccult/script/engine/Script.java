package net.darktree.stylishoccult.script.engine;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneInstance;
import net.darktree.stylishoccult.script.component.SafeMode;
import net.darktree.stylishoccult.script.element.StackElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class Script {

	private RuneInstance instance = null;
	private SafeMode safe = SafeMode.DISABLED;

	public final Ring ring = new Ring(6);
	public final Stack stack = new Stack(32);
	public Direction direction = Direction.NORTH;

	/**
	 * Deserialize the script state from the given {@link NbtCompound}
	 */
	public static Script fromNbt(NbtCompound nbt) {
		Script script = new Script();

		if(nbt.contains("i")) script.instance = RuneInstance.from(nbt.getCompound("i"));
		script.direction = Direction.byId(nbt.getByte("d"));
		script.safe = SafeMode.from(nbt.getByte("f"));
		script.stack.readNbt(nbt.getCompound("s"));
		script.ring.readNbt(nbt.getCompound("r"));

		return script;
	}

	/**
	 * Serializes the script state into the given {@link NbtCompound}
	 */
	public NbtCompound writeNbt(NbtCompound nbt) {
		if(instance != null) nbt.put("i", instance.writeNbt(new NbtCompound()));
		nbt.putByte("d", (byte) direction.getId());
		nbt.putByte("f", (byte) safe.ordinal());
		nbt.put("s", stack.writeNbt(new NbtCompound()));
		nbt.put("r", ring.writeNbt(new NbtCompound()));

		return nbt;
	}

	/**
	 * Execute the rune at given position
	 */
	public void apply(RuneBlock rune, World world, BlockPos pos) {
		safe = this.safe.advance();
		instance = instance == null ? rune.getInstance() : instance.choose(this, rune.getInstance());
		rune.apply(this, world, pos);
		stack.validate();
		Criteria.TRIGGER.trigger(world, pos, rune);
	}

	/**
	 * Copy the script for given direction
	 */
	public Script copyFor(Direction direction) {
		Script script = new Script();
		script.stack.from(stack);
		script.ring.from(ring);
		script.instance = this.instance == null ? null : this.instance.copy();
		return script.with(direction);
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
		this.instance = null;
		this.safe = SafeMode.DISABLED;
	}

	/**
	 * Use this to enter safe mode (no exceptions) for the next rune
	 */
	public void enableSafeMode() {
		this.safe = SafeMode.SCHEDULED;
	}

	/**
	 * This method is used to handle {@link RuneException}s
	 */
	public void handle(RuneException exception, World world, BlockPos pos) {
		exception.apply(world, pos, this.safe);
		this.reset(world, pos);
	}

}
