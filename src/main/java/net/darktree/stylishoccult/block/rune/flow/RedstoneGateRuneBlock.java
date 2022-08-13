package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.rune.TransferRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RedstoneGateRuneBlock extends TransferRuneBlock {

	public RedstoneGateRuneBlock(String name) {
		super(name);
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return world.isReceivingRedstonePower(pos) ? super.getDirections(world, pos, script) : Directions.NONE;
	}

}
