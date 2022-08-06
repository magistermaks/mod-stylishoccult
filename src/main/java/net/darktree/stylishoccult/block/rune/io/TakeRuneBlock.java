package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.block.rune.InputRuneBlock;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.InventoryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TakeRuneBlock extends InputRuneBlock {

	public TakeRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		ItemStack stack = fetch(world, pos, (int) script.pull(world, pos).value());
		script.stack.push(new ItemElement(stack));
	}

	private static ItemStack fetch(World world, BlockPos pos, int count) {
		for (Direction direction : Direction.values()) {
			ItemStack stack = InventoryAccess.at(world, pos, direction).extract(count);

			if (!stack.isEmpty()) {
				return stack;
			}

			// TODO: try extracting fluids (?)
		}

		return ItemStack.EMPTY;
	}

}
