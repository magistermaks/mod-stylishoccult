package net.darktree.stylishoccult.particles;

import net.darktree.interference.render.RenderHelper;
import net.darktree.interference.render.RenderedParticle;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class VortexParticle extends RenderedParticle {

	private static final Identifier TEXTURE = new ModIdentifier("textures/particle/spore.png");
	private final float MAGIC = MathHelper.HALF_PI + MathHelper.PI;
	float tx = 0, ty = 0, tz = 0, radius = 0;

	public VortexParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		this.maxAge = 40;
	}

	public void setTarget(float x, float y, float z) {
		this.tx = x;
		this.ty = y;
		this.tz = z;

		float dx = (float) this.x - this.tx;
		float dz = (float) this.z - this.tz;

		radius = MathHelper.sqrt(dx * dx + dz * dz);
		angle = MAGIC - (float) MathHelper.atan2(dx, dz);
	}

	@Override
	public void render(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, float delta) {
		RenderHelper.renderCutoutBillboard(TEXTURE, matrices, immediate, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (radius > 0) radius -= 0.05f;
		angle += 1.001f * (age / (float) maxAge);
		float r = radius * (1 - age / (float) maxAge);

		this.x = this.tx - r * MathHelper.cos(angle);
		this.z = this.tz - r * MathHelper.sin(angle);

		if (radius <= 0 || this.age ++ > this.maxAge) this.markDead();
	}

}

