package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.block.rune.ActorRuneBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class RedstoneDigitalOutputRuneBlock extends ActorRuneBlock {

	public RedstoneDigitalOutputRuneBlock(String name) {
		super(name);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(COOLDOWN) != 0 ? 15 : 0;
	}

}
