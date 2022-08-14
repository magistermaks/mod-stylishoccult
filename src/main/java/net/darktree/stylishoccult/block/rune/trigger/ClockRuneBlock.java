package net.darktree.stylishoccult.block.rune.trigger;

import net.darktree.stylishoccult.block.rune.TimedRuneBlock;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ClockRuneBlock extends TimedRuneBlock {

	public ClockRuneBlock(String name) {
		super(RuneType.ENTRY, name);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (oldState.getBlock() != this) onDelayEnd(world, pos);
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		setTimeout(world, pos, 3);
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return Directions.ALL;
	}

	@Override
	public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
		return false;
	}

	@Override
	protected void onTimeoutEnd(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (super.canAcceptSignal(state, null)) {
			execute(world, pos, state, new Script());
		}
	}

}
