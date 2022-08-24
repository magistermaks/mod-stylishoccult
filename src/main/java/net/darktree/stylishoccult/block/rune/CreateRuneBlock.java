package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreateRuneBlock extends TransferRuneBlock {

	private final Item item;

	public CreateRuneBlock(String name, Item item) {
		super(name);
		this.item = item;
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		script.stack.push(new ItemElement(new ItemStack(item)));
	}

}
