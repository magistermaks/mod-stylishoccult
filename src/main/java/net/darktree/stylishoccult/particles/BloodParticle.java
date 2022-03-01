package net.darktree.stylishoccult.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;

public class BloodParticle {

	@Environment(EnvType.CLIENT)
	public static class FallingFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public FallingFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle particle = new BlockLeakParticle.ContinuousFalling(clientWorld, d, e, f, Fluids.EMPTY, Particles.BLOOD_SPLASH);
			particle.setColor(0.5f, 0.01f, 0.01f);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class DrippingFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DrippingFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle particle = new BlockLeakParticle.Dripping(clientWorld, d, e, f, Fluids.EMPTY, Particles.BLOOD_FALLING);
			particle.setColor(0.5f, 0.05f, 0.01f);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class SuspendFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public SuspendFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			WaterSuspendParticle particle = new WaterSuspendParticle(clientWorld, this.spriteProvider, d, e, f);
			particle.setColor(0.7f, 0.2f, 0.2f);
			return particle;
		}
	}

}
