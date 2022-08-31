package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class BounceRuneBlock extends SleepRuneBlock {

	public BounceRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		if (attachment.getScript() == null) {
			attachment.setScript(script.drops(false));
		}
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		setTimeout(world, pos, 1);
	}

	@Override
	protected final Direction getTargetDirection(Script script) {
		return script.direction.getOpposite();
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		// noop
	}

}
