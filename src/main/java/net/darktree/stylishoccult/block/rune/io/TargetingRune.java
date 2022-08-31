package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TargetingRune {

	int RANGE = 64;

	default BlockPos getTarget(Script script, World world, BlockPos pos) {
		int x = (int) Math.round(script.pull(world, pos).value());
		int y = (int) Math.round(script.pull(world, pos).value());
		int z = (int) Math.round(script.pull(world, pos).value());

		BlockPos target = pos.add(x, y, z);

		if (!target.isWithinDistance(pos, RANGE)) {
			throw RuneException.of(RuneExceptionType.OUT_OF_RANGE);
		}

		return target;
	}

}
