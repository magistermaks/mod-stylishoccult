package net.darktree.stylishoccult.particles;

import net.darktree.interference.render.RenderHelper;
import net.darktree.interference.render.RenderedParticle;
import net.darktree.interference.render.ShapeRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AttackParticle extends RenderedParticle {

	private static final Random RANDOM = new Random();
	private static final float separation = 0.005f;
	private static final float DURATION = 15;

	private double tx = 0, ty = 0, tz = 0;

	public AttackParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
	}

	public void setTarget(double x, double y, double z) {
		this.tx = x;
		this.ty = y;
		this.tz = z;
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
		renderArc(matrices, buffer, this.age, delta, 0, 0, 0, (float) (x - tx), (float) (ty - y), (float) (z - tz), f);
	}

	private void renderArc(MatrixStack matrices, VertexConsumer buffer, long tick, float delta, float x1, float y1, float z1, float x2, float y2, float z2, float alpha) {
		final float s = getDefaultSegmentation();
		final float r = 0.65f, g = 0.2f, b = 0.1f;
		renderLayeredArcLine(matrices, buffer, 3, this.seed, separation, s, x1, y1, z1, x2, y2, z2, r, g, b, 0.5f * alpha);
	}

	private void renderLayeredArcLine(MatrixStack matrices, VertexConsumer buffer, int depth, long seed, float separation, float segmentation, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
		final float dx = x2 - x1;
		final float dy = y2 - y1;
		final float dz = z2 - z1;
		final int segments = Math.max((int) (segmentation * MathHelper.sqrt(dx * dx + dy * dy + dz * dz)), 1);

		float radius = 0.002f;

		for(int i = 0; i < depth; i ++) {
			RANDOM.setSeed(seed);
			renderArcLine(matrices, buffer, RANDOM, segments, 4, 0.15f, radius, true, r, g, b, a, x1, y1, z1, x2, y2, z2);
			radius += separation;
		}
	}

	private void renderArcLine(MatrixStack matrices, VertexConsumer buffer, Random random, int segments, int count, float jaggedness, float radius, boolean noise, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2) {
		float sx = (x2 - x1) / segments;
		float sy = (y2 - y1) / segments;
		float sz = (z2 - z1) / segments;

		float fx = x1, fy = y1, fz = z1;
		float tx, ty, tz;

		for(int i = 1; i < segments; i ++) {
			tx = x1 + sx * i + (random.nextFloat() - 0.5f) * jaggedness;
			ty = y1 + sy * i + (random.nextFloat() - 0.5f) * jaggedness;
			tz = z1 + sz * i + (random.nextFloat() - 0.5f) * jaggedness;

			ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, tx, ty, tz, ShapeRenderer.NO_OFFSET);

			// split into sub-arcs
			if(noise && i != 1 && random.nextInt(8) == 0) {
				int j = random.nextBoolean() ? 5 : -5;

				float ttx = tx + sx * j + (random.nextFloat() - 0.5f) * jaggedness * 6;
				float tty = ty + sy * j + (random.nextFloat() - 0.5f) * jaggedness * 6;
				float ttz = tz + sz * j + (random.nextFloat() - 0.5f) * jaggedness * 6;

				renderArcLine(matrices, buffer, random, 3, count, jaggedness * 0.84f, radius * 0.6f, true, r * 1.3f, g, b, a * 0.6f, fx, fy, fz, ttx, tty, ttz);
			}

			fx = tx;
			fy = ty;
			fz = tz;
		}

		ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, x2, y2, z2, ShapeRenderer.NO_OFFSET);
	}

	private float getDefaultSegmentation() {
		return RenderHelper.shouldRenderDetails() ? 6.666f : 4.666f;
	}

}

