package net.darktree.stylishoccult.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
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

			if (age % 2 == 0) {
				world.getServer().getPlayerManager().sendToAround(null, prevX, prevY, prevZ, 32, world.getRegistryKey(), new ParticleS2CPacket(ParticleTypes.SMOKE, false, prevX, prevY, prevZ, 0, 0, 0, 0, 1));
			}
		}
	}

	public boolean canMerge() {
		return super.canMerge() && mergeDelay == 0;
	}

}
