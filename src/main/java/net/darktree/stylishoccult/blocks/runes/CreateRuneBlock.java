package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.elements.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreateRuneBlock extends TransferRuneBlock {

	private final Item item;

	public CreateRuneBlock(String name, Item item) {
		super(name);
		this.item = item;
	}

	@Override
	public void apply(Script script) {
		script.stack.push(new ItemElement(new ItemStack(item)));
	}

}
