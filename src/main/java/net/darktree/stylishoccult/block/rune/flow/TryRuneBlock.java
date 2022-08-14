package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.rune.TransferRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TryRuneBlock extends TransferRuneBlock {

	public TryRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		script.enableSafeMode();
	}

}
