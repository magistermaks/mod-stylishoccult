package net.darktree.stylishoccult.script.elements;

import net.darktree.stylishoccult.script.components.RuneException;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemElement extends StackElement {

	final ItemStack stack;

	public ItemElement(ItemStack stack) {
		this.stack = stack;
	}

	public ItemElement(NbtCompound nbt) {
		this.stack = ItemStack.fromNbt(nbt.getCompound("v"));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("v", this.stack.writeNbt(new NbtCompound()));
		return super.writeNbt(nbt);
	}

	@Override
	public double value() {
		return this.stack.getCount();
	}

	@Override
	public StackElement copy() {
		throw new RuneException("operation not permitted!");
	}

	public boolean equals(StackElement element) {
		if(element instanceof ItemElement itemElement) {
			return itemElement.stack.getItem() == this.stack.getItem();
		}

		return super.equals(element);
	}

	@Override
	public String toString() {
		return "ItemElement " + this.stack.writeNbt(new NbtCompound()).toString();
	}

	@Override
	public void drop(World world, BlockPos pos) {
		Block.dropStack(world, pos, this.stack);
	}

}
