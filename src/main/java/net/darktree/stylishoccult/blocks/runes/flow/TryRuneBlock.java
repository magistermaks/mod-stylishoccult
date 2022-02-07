package net.darktree.stylishoccult.blocks.runes.flow;

import net.darktree.stylishoccult.blocks.runes.TransferRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;

public class TryRuneBlock extends TransferRuneBlock {

	public TryRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script) {
		script.enableSafeMode();
	}
}
