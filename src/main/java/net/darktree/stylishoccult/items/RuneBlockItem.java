package net.darktree.stylishoccult.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class RuneBlockItem extends BlockItem {

	public RuneBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public Text getName(ItemStack stack) {
		return getName();
	}

	@Override
	public Text getName() {
		return getBlock().getName();
	}

}
