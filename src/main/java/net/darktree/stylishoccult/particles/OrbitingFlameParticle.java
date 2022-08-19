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
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class OrbitingFlameParticle extends AnimatedParticle {

	private final float radius;
	private float rad;
	private float bop;
	private final Vec3f origin;

	protected OrbitingFlameParticle(ClientWorld world, double x, double y, double z, SpriteProvider sprites) {
		super(world, x, y, z, sprites, 1);
		setSprite(sprites.getSprite(world.random));

		maxAge = RandUtils.rangeInt(50, 70, world.random);
		velocityZ = world.random.nextFloat() * 6.2830;
		scale = 0.1F * (random.nextFloat() * 0.5F + 0.55F) * 1.3F;
		origin = new Vec3f((float) x, (float) y, (float) z);

		rad = random.nextFloat() * 6.2830f;
		radius = RandUtils.rangeInt(30, 90, world.random) * 0.01f;
		bop = random.nextFloat();

		updatePosition();

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
	}

	@Override
	public float getSize(float tickDelta) {
		float g = ((float) this.age + tickDelta);
		float s = ((float) Math.sin(g/4 + this.velocityZ) + 1) / (4 + (float) age / 32);

		float f = (g / ((float)this.maxAge + (float) this.velocityZ)) + s;
		return this.scale * (1.0F - f * f * 0.3F);
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		updatePosition();

		rad += 0.1f;
		bop += 0.1f;

		if (this.age ++ > this.maxAge) this.markDead();
	}

	private void updatePosition() {
		this.x = origin.getX() + radius * Math.cos(rad);
		this.y = origin.getY() + Math.sin(bop) / 3;
		this.z = origin.getZ() + radius * Math.sin(rad);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public Factory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
			return new OrbitingFlameParticle(world, x, y, z, sprites);
		}
	}

}
