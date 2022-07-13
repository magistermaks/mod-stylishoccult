package net.darktree.stylishoccult.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ThrownItemEntity extends ItemEntity {

	private int mergeDelay;

	public ThrownItemEntity(World world, double x, double y, double z, ItemStack stack, double vx, double vy, double vz, int mergeDelay) {
		super(world, x, y, z, stack, vx, vy, vz);
		this.mergeDelay = mergeDelay;
	}

	@Override
	public void tick() {
		super.tick();

		if (mergeDelay > 0) {
			mergeDelay --;
		}
	}

	public boolean canMerge() {
		return super.canMerge() && mergeDelay == 0;
	}

}
