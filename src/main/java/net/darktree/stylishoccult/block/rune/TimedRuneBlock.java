package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.component.RuneType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class TimedRuneBlock extends RuneBlock {

	private final static int FLAGS = Block.NO_REDRAW | Block.NOTIFY_LISTENERS;
	private final static BooleanProperty WAITING = BooleanProperty.of("waiting");

	public TimedRuneBlock(RuneType type, String name) {
		super(type, name);
		setDefaultState(getDefaultState().with(WAITING, false));
	}

	/**
	 * Must be called from {@link RuneBlock#onDelayEnd}
	 */
	protected final void setTimeout(World world, BlockPos pos, int timeout) {
		world.setBlockState(pos, world.getBlockState(pos).with(WAITING, true), FLAGS);
		world.getBlockTickScheduler().schedule(pos, this, timeout);
	}

	protected abstract void onTimeoutEnd(World world, BlockPos pos);

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WAITING);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(WAITING)) {
			world.setBlockState(pos, state.with(WAITING, false), FLAGS);
			onTimeoutEnd(world, pos);
		} else {
			super.scheduledTick(state, world, pos, random);
		}
	}

	@Override
	public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
		return !state.get(WAITING) && super.canAcceptSignal(state, from);
	}

}
