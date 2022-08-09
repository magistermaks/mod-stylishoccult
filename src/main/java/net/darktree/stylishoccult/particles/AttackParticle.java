package net.darktree.stylishoccult.particles;

import net.darktree.interference.render.RenderedParticle;
import net.darktree.stylishoccult.render.ArcConfig;
import net.darktree.stylishoccult.render.ArcRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class AttackParticle extends RenderedParticle {

	private static final float DURATION = 15;

	private double tx = 0, ty = 0, tz = 0;
	private boolean altar;

	public AttackParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
	}

	public void setTarget(double x, double y, double z, boolean altar) {
		this.tx = x;
		this.ty = y;
		this.tz = z;
		this.altar = altar;
	}

	@Override
	public void tick() {
		if (this.age++ >= DURATION) {
			this.markDead();
		}
	}

	@Override
	public void render(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, float delta) {
		float f = MathHelper.cos(this.age / DURATION * MathHelper.HALF_PI);
		VertexConsumer buffer = immediate.getBuffer(RenderLayer.getLightning());

		ArcConfig cfg = altar ? ArcConfig.ALTAR_ARC : ArcConfig.ARCANE_SPELL;
		int tick = altar ? this.age : this.seed;

		ArcRenderer.renderArc(matrices, buffer, cfg, tick, altar ? delta : 0, 0, 0, 0, (float) (x - tx), (float) (ty - y), (float) (z - tz), f);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public Factory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
			return new AttackParticle(world, x, y, z, vx, vy, vz);
		}
	}

}

