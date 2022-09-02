package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.block.rune.InputRuneBlock;
import net.darktree.stylishoccult.script.element.NumericElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestRuneBlock extends InputRuneBlock implements TargetingRune {

	public TestRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		BlockPos target = getTarget(script, world, pos);
		boolean air = world.getBlockState(target).isAir();
		script.stack.push(NumericElement.bool(!air));
	}

}
