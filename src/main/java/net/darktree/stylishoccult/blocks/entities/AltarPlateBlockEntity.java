package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.entities.parts.AltarRingItemStack;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AltarPlateBlockEntity extends SimpleBlockEntity {

	List<AltarRingItemStack> candles = new ArrayList<>();
	ItemStack center = ItemStack.EMPTY;

	public AltarPlateBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.ALTAR_PLATE, pos, state);
	}

	public boolean use(ItemStack stack) {
		if (world != null && stack.isEmpty()) {
			if (!center.isEmpty()) {
				Block.dropStack(world, pos, center);
				center = ItemStack.EMPTY;
				markDirty();
				return true;
			}

			if (!candles.isEmpty()) {
				ItemStack taken = candles.remove(0).stack;
				Block.dropStack(world, pos, taken);
				markDirty();
				return true;
			}

			return false;
		}

		if (stack.isIn(ItemTags.CANDLES)) {
			ItemStack taken = stack.copy();
			taken.setCount(1);
			candles.add(AltarRingItemStack.create(taken));
			stack.decrement(1);
			markDirty();
			return true;
		}

		if (center.isEmpty()) {
			center = stack.copy();
			stack.setCount(0);
			markDirty();
			return true;
		}

		return false;
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("c", this.center.writeNbt(new NbtCompound()));
		nbt.putInt("l", this.candles.size());

		for (int i = 0; i < this.candles.size(); i ++) {
			nbt.put("i" + i, this.candles.get(i).writeNbt(new NbtCompound()));
		}

		return super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		try {
			this.center = ItemStack.fromNbt(nbt.getCompound("c"));

			int l = nbt.getInt("l");

			for (int i = 0; i < l; i ++) {
				candles.add(AltarRingItemStack.fromNbt(nbt.getCompound("i" + i)));
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		super.readNbt(nbt);
	}

	public ItemStack getCenter() {
		return center;
	}

	public List<AltarRingItemStack> getCandles() {
		return candles;
	}

}
