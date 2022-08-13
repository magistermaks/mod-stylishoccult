package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.rune.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class IfRuneBlock extends DirectionalRuneBlock {

	public IfRuneBlock(String name) {
		super(name);
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		try {
			if (script.pull(world, pos).value() != 0) {
				return Directions.of(getFacing(world, pos));
			}
		}catch (Exception ignore) {}

		// Make it explode (?)
		return super.getDirections(world, pos, script);
	}
}
