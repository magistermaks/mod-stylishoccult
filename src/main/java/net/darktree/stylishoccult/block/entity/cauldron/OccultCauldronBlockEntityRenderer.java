package net.darktree.stylishoccult.block.entity.cauldron;

import net.darktree.stylishoccult.render.FluidRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class OccultCauldronBlockEntityRenderer implements BlockEntityRenderer<OccultCauldronBlockEntity> {

	public OccultCauldronBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		// noop
	}

	@Override
	public void render(OccultCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		OccultCauldronBlockEntity.Storage storage = entity.getStorage();
		FluidRenderer.drawCauldronFluidQuad(matrices, vertexConsumers, storage.getResource(), storage.getAmount(), light);
	}

}
