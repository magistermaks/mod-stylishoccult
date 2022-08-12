package net.darktree.stylishoccult.script.element;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.view.ElementView;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.Block;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
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
			copy.setCount(count);

			if (reminder > 0) {
				reminder --;
				copy.increment(1);
			}

			list.add(new ItemElement(copy));
		}

		return list;
	}

	@Override
	public ElementView view() {
		Item item = stack.getItem();
		String text = stack.getCount() + "x " + I18n.translate(item.getTranslationKey());

		return ElementView.of("item", ElementView.ITEM_ICON, text, Registry.ITEM.getId(item).toString());
	}

	public boolean equals(StackElement element) {
		if (element instanceof ItemElement itemElement) {
			return itemElement.stack.getItem() == this.stack.getItem();
		}

		return super.equals(element);
	}

	@Override
	public void drop(World world, BlockPos pos) {
		ItemStack remainder = insert(world, pos);
		Block.dropStack(world, pos, remainder);
	}

	private ItemStack insert(World world, BlockPos pos) {
		for (Direction direction : Directions.ALL) {

			Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));

			if (inventory != null) {
				return HopperBlockEntity.transfer(null, inventory, this.stack.copy(), direction.getOpposite());
			}
		}

		return this.stack;
	}

}
