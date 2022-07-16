package net.darktree.stylishoccult.particles;

import net.darktree.interference.render.RenderHelper;
import net.darktree.interference.render.RenderedParticle;
import net.darktree.interference.render.ShapeRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class SwirlyParticle extends RenderedParticle {

	private ShapeRenderer.Segment segments[];
	float speed;

	public SwirlyParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		this.segments = new ShapeRenderer.Segment[100];

		for (int i = 0; i < 100; i ++) {
			this.segments[i] = new ShapeRenderer.Segment(0, 0);
		}

		this.maxAge = 20*100;
		this.speed = world.random.nextFloat() * 0.3f + 0.1f;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge) {
			this.markDead();
		}
	}

	@Override
	public void render(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, float delta) {
		VertexConsumer buffer = immediate.getBuffer(RenderLayer.getLightning());

		float t = (this.age + delta) / 10;
		float f = Math.min(this.age, 80) / 20f;

		int i = 0;

		for (ShapeRenderer.Segment segment : segments) {
			segment.ox = (MathHelper.sin(t)) * MathHelper.sin(i/100f * MathHelper.PI) * 0.2f;
			segment.oz = (MathHelper.cos(t)) * MathHelper.sin(i/100f * MathHelper.PI) * 0.2f;
			t += this.speed;
			i ++;
		}

		renderPrismAlong(matrices, buffer, 4, 0.01f * f, 0, 1.0f, 0.2f, 0.1f, 1.0f, 0f, 0f, 0f, 0f, 8f, 0f, segments);
	}

	public static void renderPrism(Matrix4f matrix, VertexConsumer buffer, int count, float height, float radius, float angle, float r, float g, float b, float a, ShapeRenderer.Segment[] segments) {
		float step = height / segments.length;

		float x = 0, y = 0;

		float p = MathHelper.PI / segments.length;

		for(int i = 0; i < segments.length; i ++) {
			ShapeRenderer.renderPrism(matrix, buffer, count, step, radius * MathHelper.sin(i * p), angle, r, g, b, a, x, y, segments[i].ox, i * step, segments[i].oz);
			x = segments[i].ox;
			y = segments[i].oz;
		}
	}

	public static void renderPrismAlong(MatrixStack matrices, VertexConsumer buffer, int count, float radius, float angle, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2, ShapeRenderer.Segment[] segments) {
		matrices.push();
		float length = RenderHelper.lookAlong(matrices, x1, y1, z1, x2, y2, z2);
		renderPrism(matrices.peek().getModel(), buffer, count, length, radius, angle, r, g, b, a, segments);
		matrices.pop();
	}

}
