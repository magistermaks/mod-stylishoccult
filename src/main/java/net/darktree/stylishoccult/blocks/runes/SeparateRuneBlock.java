package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.script.elements.StackElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SeparateRuneBlock extends RuneBlock {

	public SeparateRuneBlock(String name) {
		super(RuneType.LOGIC, name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		int split = (int) script.pull(world, pos).value();

		if (split > 16) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT);
		}

		for (StackElement element : script.stack.pull().split(split)) {
			script.stack.push(element);
		}
	}
}
