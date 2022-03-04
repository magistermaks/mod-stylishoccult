package net.darktree.stylishoccult.script.elements;

import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemElement extends StackElement {

	public final ItemStack stack;

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
		throw RuneException.of(RuneExceptionType.UNMET_EQUIVALENCY);
	}

	@Override
	public List<StackElement> split(int split) {
		ArrayList<StackElement> list = new ArrayList<>();

		int reminder = stack.getCount() % split;
		int count = stack.getCount() / split;

		for (int i = 0; i < split; i ++) {
			ItemStack copy = stack.copy();
			copy.setCount(count + reminder);

			list.add(new ItemElement(copy));

			if (reminder > 0) {
				reminder --;
			}
		}

		return list;
	}

	public boolean equals(StackElement element) {
		if (element instanceof ItemElement itemElement) {
			return itemElement.stack.getItem() == this.stack.getItem();
		}

		return super.equals(element);
	}

	@Override
	public String toString() {
		return "ItemElement " + this.stack.writeNbt(new NbtCompound());
	}

	@Override
	public void drop(World world, BlockPos pos) {
		ItemStack remainder = insert(world, pos);
		Block.dropStack(world, pos, remainder);
	}

	private ItemStack insert(World world, BlockPos pos) {
		for(Direction direction : Direction.values()) {

			Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));

			if (inventory != null) {
				return HopperBlockEntity.transfer(null, inventory, this.stack.copy(), direction.getOpposite());
			}

		}

		return this.stack;
	}

}
