package net.darktree.stylishoccult.block.entity.cauldron;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class OccultCauldronBlockEntityRenderer implements BlockEntityRenderer<OccultCauldronBlockEntity> {

	public OccultCauldronBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		// noop
	}

	@Override
	public void render(OccultCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		OccultCauldronBlockEntity.Storage storage = entity.getStorage();

		if (storage.getAmount() != 0) {
			matrices.translate(0.5f, entity.getLevel() - 0.5f, 0.5f);
			matrices.scale(0.99f, 1, 0.99f);
			drawFlatFluidQuad(matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), storage.getResource().getFluid(), light);
		}
	}

	private void drawFlatFluidQuad(MatrixStack matrices, VertexConsumer buffer, Fluid fluid, int light) {
		FluidState state = fluid.getDefaultState();
		FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
		Sprite sprite = handler.getFluidSprites(null, null, state)[0];
		int rgb = handler.getFluidColor(null, null, state);

		int r = (rgb & 0xFF0000) >> 16;
		int g = (rgb & 0x00FF00) >> 8;
		int b = (rgb & 0x0000FF);

		float u1 = sprite.getMinU();
		float v1 = sprite.getMinV();
		float u2 = sprite.getMaxU();
		float v2 = sprite.getMaxV();

		MatrixStack.Entry entry = matrices.peek();
		Matrix4f position = entry.getModel();
		Matrix3f normal = entry.getNormal();

		buffer.vertex(position,  0.5f, 0.5f, -0.5f).color(r, g, g, 255).texture(u1, v1).light(light).normal(normal, 0, 1, 0).next();
		buffer.vertex(position, -0.5f, 0.5f, -0.5f).color(r, g, g, 255).texture(u2, v1).light(light).normal(normal, 0, 1, 0).next();
		buffer.vertex(position, -0.5f, 0.5f,  0.5f).color(r, g, g, 255).texture(u2, v2).light(light).normal(normal, 0, 1, 0).next();
		buffer.vertex(position,  0.5f, 0.5f,  0.5f).color(r, g, b, 255).texture(u1, v2).light(light).normal(normal, 0, 1, 0).next();
	}

}
