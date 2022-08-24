package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.block.rune.InputRuneBlock;
import net.darktree.stylishoccult.script.element.NumericElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneAnalogInputRuneBlock extends InputRuneBlock {

	public RedstoneAnalogInputRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		script.stack.push(new NumericElement(world.getReceivedRedstonePower(pos)));
	}

}
