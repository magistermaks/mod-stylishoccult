package net.darktree.stylishoccult.item;

import net.darktree.stylishoccult.particles.Particles;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
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

			// this entity exists only on the server side, but let's verify it anyway
			// in case something else caused it to spawn
			if (age % 2 == 0 && !world.isClient) {
				Particles.spawn(world, ParticleTypes.SMOKE, prevX, prevY, prevZ, 1);
			}
		}
	}

	@Override
	public boolean canMerge() {
		return super.canMerge() && mergeDelay == 0;
	}

}
