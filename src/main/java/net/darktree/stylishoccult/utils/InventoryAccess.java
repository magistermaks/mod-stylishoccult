package net.darktree.stylishoccult.utils;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class InventoryAccess {

	public static final InventoryAccess DUMMY = new InventoryAccess(new DummyInventory(), null) {
		public ItemStack extract(int count) {
			return ItemStack.EMPTY;
		}
	};

	private final Inventory inventory;
	private final Direction direction;
	private final int[] slots;

	public InventoryAccess(Inventory inventory, Direction direction) {
		this.inventory = inventory;
		this.direction = direction;

		if (inventory instanceof SidedInventory sided) {
			this.slots = sided.getAvailableSlots(direction);
		}else{
			this.slots = IntStream.range(0, inventory.size()).toArray();
		}
	}

	/**
	 * Access inventory at literal position from the requested direction
	 */
	public static InventoryAccess of(World world, BlockPos pos, Direction direction) {
		Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos);
		return inventory == null ? DUMMY : new InventoryAccess(inventory, direction);
	}

	/**
	 * Access inventory pointed to from pos by direction
	 */
	public static InventoryAccess at(World world, BlockPos pos, Direction direction) {
		return of(world, pos.offset(direction), direction.getOpposite());
	}

	/**
	 * Get any item of given count from the inventory
	 * For the sake of simplicity it doesn't combine the stacks
	 * TODO: try combining the stacks
	 */
	public ItemStack extract(int count) {
		for (int slot : slots) {
			ItemStack stack = inventory.getStack(slot);

			if (!stack.isEmpty() && stack.getMaxCount() >= count) {
				if (inventory instanceof SidedInventory sided) {
					if (!sided.canExtract(slot, stack, direction)) {
						continue;
					}
				}

				if (stack.getCount() >= count) {
					return inventory.removeStack(slot, count);
				}
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Simple public dummy inventory
	 */
	public static class DummyInventory extends SimpleInventory implements SidedInventory {

		public DummyInventory() {
			super(0);
		}

		@Override
		public int[] getAvailableSlots(Direction side) {
			return new int[0];
		}

		@Override
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return false;
		}

		@Override
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return false;
		}

	}

}
