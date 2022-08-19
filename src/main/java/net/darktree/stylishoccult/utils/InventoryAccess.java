package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InventoryAccess {

	public static final InventoryAccess DUMMY = new InventoryAccess(new DummyInventory(), null) {
		@Override
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
	 * Access inventory pointed to, from pos, by direction
	 */
	public static InventoryAccess at(World world, BlockPos pos, Direction direction) {
		return of(world, pos.offset(direction), direction.getOpposite());
	}

	/**
	 * Get any item stack of given count from the inventory
	 */
	public ItemStack extract(int count) {
		List<ItemStack> combined = new ArrayList<>();

		for (int slot : slots) {
			ItemStack stack = inventory.getStack(slot);

			if (!stack.isEmpty() && stack.getMaxCount() >= count) {

				// verify that we can take this stack
				if (inventory instanceof SidedInventory sided) {
					if (!sided.canExtract(slot, stack, direction)) {
						continue;
					}
				}

				// if the stack is valid conclude the search early
				if (stack.getCount() >= count) {
					return inventory.removeStack(slot, count);
				}

				boolean merged = false;

				// try merging the stack into one of the stacks in the list
				for (ItemStack entry : combined) {
					if (areStacksEqualIgnoreCount(entry, stack)) {

						int free = entry.getMaxCount() - entry.getCount();
						entry.increment(Math.min(stack.getCount(), free));

						// if the merged stack is valid conclude the search
						if (entry.getCount() >= count) {
							entry.setCount(count);

							return extractMatching(entry);
						}

						merged = true;
						break;
					}
				}

				// if it couldn't be merged anywhere add it as a new entry
				if (!merged) {
					combined.add(stack.copy());
				}
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Try to extract the given stack from the inventory
	 * The given stack must exist within the inventory
	 */
	private ItemStack extractMatching(ItemStack needle) {
		int count = needle.getCount();

		// should not happen normally
		if (needle.isEmpty()) {
			return needle;
		}

		for (int slot : slots) {
			ItemStack stack = inventory.getStack(slot);

			// verify that we can take this stack
			if (inventory instanceof SidedInventory sided) {
				if (!sided.canExtract(slot, stack, direction)) {
					continue;
				}
			}

			// try taking the items until we get the desired number of them
			if (areStacksEqualIgnoreCount(needle, stack)) {
				count -= inventory.removeStack(slot, count).getCount();

				if (count <= 0) {
					break;
				}
			}
		}

		// success, the requested stack was extracted
		if (count <= 0) {
			return needle;
		}

		// something went wrong, the requested stack was not present in the inventory
		StylishOccult.LOGGER.error("Item extraction failed for the requested stack! Some items could have been deleted!");
		return ItemStack.EMPTY;
	}

	/**
	 * Check if two stacks are equal (except for the size)
	 */
	public static boolean areStacksEqualIgnoreCount(ItemStack left, ItemStack right) {
		return left.isItemEqual(right) && ItemStack.areNbtEqual(left, right);
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
