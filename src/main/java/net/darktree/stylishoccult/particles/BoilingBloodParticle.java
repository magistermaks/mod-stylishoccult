package net.darktree.stylishoccult.particles;

import net.darktree.stylishoccult.utils.RandUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class BoilingBloodParticle extends AnimatedParticle {

	protected BoilingBloodParticle(ClientWorld world, double x, double y, double z, SpriteProvider sprites) {
		super(world, x, y, z, sprites, 1);
		setSprite( sprites.getSprite(world.random) );

		maxAge = RandUtils.rangeInt(10, 30);
		velocityX = (world.random.nextFloat() - 0.5f) * 0.04;
		velocityY = (world.random.nextFloat() + 1) * 0.08;
		velocityZ = (world.random.nextFloat() - 0.5f) * 0.04;
		scale = (random.nextFloat() * 0.5F + 0.55F) * 0.2F;

		updatePosition();

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		setColor(0x931b15);
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		updatePosition();

		if (this.age ++ > this.maxAge) this.markDead();
	}

	private void updatePosition() {
		this.x += this.velocityX;
		this.y += this.velocityY;
		this.z += this.velocityZ;

		this.velocityY -= 0.03f * (age / (float) maxAge);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public Factory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			return new BoilingBloodParticle(world, x, y, z, sprites);
		}
	}

}

