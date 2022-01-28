package net.darktree.stylishoccult.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;

public class BloodParticle {

	@Environment(EnvType.CLIENT)
	public static class FallingFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;

		public FallingFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.ContinuousFalling(clientWorld, d, e, f, Fluids.EMPTY, Particles.BLOOD_SPLASH);
			blockLeakParticle.setColor(0.5f, 0.01f, 0.01f);
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class DrippingFactory implements ParticleFactory<DefaultParticleType> {
		protected final SpriteProvider spriteProvider;

		public DrippingFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlockLeakParticle blockLeakParticle = new BlockLeakParticle.Dripping(clientWorld, d, e, f, Fluids.EMPTY, Particles.BLOOD_FALLING);
			blockLeakParticle.setColor(0.5f, 0.05f, 0.01f);
			blockLeakParticle.setSprite(this.spriteProvider);
			return blockLeakParticle;
		}
	}

}
