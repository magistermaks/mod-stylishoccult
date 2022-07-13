package net.darktree.stylishoccult.blocks.entities.parts;

import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * This class is used to represent the candles in the outer altar ring
 */
public class AltarRingItemStack {
	public final ItemStack stack;
	public final float offset;

	public AltarRingItemStack(ItemStack stack, float offset) {
		if (!(stack.getItem() instanceof BlockItem)) {
			throw new RuntimeException("Invalid argument, given candle is not a BlockItem!");
		}

		this.stack = stack;
		this.offset = offset;
	}

	public static AltarRingItemStack create(ItemStack stack) {
		return new AltarRingItemStack(stack, RandUtils.RANDOM.nextFloat());
	}

	public static AltarRingItemStack fromNbt(NbtCompound nbt) {
		return new AltarRingItemStack(ItemStack.fromNbt(nbt), nbt.getFloat("offset"));
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putFloat("offset", this.offset);
		return this.stack.writeNbt(nbt);
	}

	public BlockState getState() {
		return ((BlockItem) this.stack.getItem()).getBlock().getDefaultState().with(CandleBlock.LIT, true);
	}
}
