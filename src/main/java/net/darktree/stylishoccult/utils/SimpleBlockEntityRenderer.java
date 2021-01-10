package net.darktree.stylishoccult.utils;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class SimpleBlockEntityRenderer<T extends SimpleBlockEntity> extends BlockEntityRenderer<T> {

    public SimpleBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        entity.render(tickDelta, matrices, vertexConsumers, light, overlay);
    }

}
