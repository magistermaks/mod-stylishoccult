package net.darktree.stylishoccult.blocks.entities.client;

import net.darktree.interference.render.RenderHelper;
import net.darktree.stylishoccult.blocks.entities.AltarPlateBlockEntity;
import net.darktree.stylishoccult.blocks.entities.parts.AltarRingItemStack;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Objects;

public class AltarPlateBlockEntityRenderer implements BlockEntityRenderer<AltarPlateBlockEntity> {

	private static final Identifier FIRE = new ModIdentifier("textures/particle/spark_0.png");

	private final BlockRenderManager blockRenderer;
	private final ItemRenderer itemRenderer;

	public AltarPlateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.blockRenderer = ctx.getRenderManager();
		this.itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	}

	@Override
	public void render(AltarPlateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		double time = Objects.requireNonNull(entity.getWorld()).getTime() + tickDelta;
		double offset = (Math.sin(time / 12.0) + 1) / 12.0;

		// render center item
		matrices.push();
		matrices.translate(0.5, 0.2, 0.5);
		matrices.translate(0, offset, 0);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) time * 4));
		itemRenderer.renderItem(entity.getCenter(), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 42);
		matrices.pop();

		List<AltarRingItemStack> ring = entity.getCandles();
		int length = ring.size();

		float rotation = (float) time * -0.01f;
		float step = MathHelper.TAU / length;

		for (int i = 0; i < length; i ++) {
			AltarRingItemStack stack = ring.get(i);

			float angle = i * step + rotation;
			float flame = 0.2f - ((float) ((time * stack.offset + stack.offset * 5) / 400) % 0.13f + 0.02f);
			float radius = (float) (Math.sin(time * 0.03f + 3 * stack.offset * MathHelper.TAU) / 20.0f) + (length * 0.022f) + 0.5f;

			double x = Math.sin(angle) * radius;
			double y = (Math.sin(time * 0.1f + 2 * stack.offset * MathHelper.TAU) + 1) / 10.0 + 0.2f;
			double z = Math.cos(angle) * radius;

			// render candle
			matrices.push();
			matrices.translate(x, y, z);
			blockRenderer.renderBlockAsEntity(stack.getState(), matrices, vertexConsumers, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);

			// render flame sprite
			matrices.translate(0.5, 0.48, 0.5);
			matrices.scale(flame, flame, flame);
			RenderHelper.renderCutoutBillboard(FIRE, matrices, vertexConsumers, LightmapTextureManager.MAX_LIGHT_COORDINATE);
			matrices.pop();
		}
	}

}
