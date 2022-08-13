package net.darktree.stylishoccult.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.PlayerManager;
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
				PlayerManager manager = world.getServer().getPlayerManager();
				ParticleS2CPacket packet = new ParticleS2CPacket(ParticleTypes.SMOKE, false, prevX, prevY, prevZ, 0, 0, 0, 0, 1);
				manager.sendToAround(null, prevX, prevY, prevZ, 32, world.getRegistryKey(), packet);
			}
		}
	}

	@Override
	public boolean canMerge() {
		return super.canMerge() && mergeDelay == 0;
	}

}
