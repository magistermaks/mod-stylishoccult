package net.darktree.stylishoccult.blocks.runes.io;

import net.darktree.stylishoccult.blocks.runes.InputRuneBlock;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.elements.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
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
			Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));

			if (inventory == null) {
				continue;
			}

			for (int i = 0; i < inventory.size(); i++) {
				if(!inventory.getStack(i).isEmpty()){
					return inventory.removeStack(i, count);
				}
			}
		}

		return ItemStack.EMPTY;
	}

}
