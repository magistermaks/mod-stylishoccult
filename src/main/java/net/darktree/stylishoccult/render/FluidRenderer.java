package net.darktree.stylishoccult.render;

import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class FluidRenderer {

	public static void drawCauldronFluidQuad(MatrixStack matrices, VertexConsumerProvider buffers, FluidVariant fluid, long amount, int light) {
		if (amount > 0) {
			VertexConsumer buffer = buffers.getBuffer(RenderLayer.getSolid());

			matrices.translate(0.5f, OccultCauldronBlockEntity.getLevel(amount) - 0.5f, 0.5f);
			matrices.scale(0.99f, 1, 0.99f);
			drawFlatFluidQuad(matrices,buffer, fluid.getFluid(), light);
		}
	}

	private static void drawFlatFluidQuad(MatrixStack matrices, VertexConsumer buffer, Fluid fluid, int light) {
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
