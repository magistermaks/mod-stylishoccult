package net.darktree.stylishoccult.entities.renderer;

import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class SparkEntityRenderer extends EntityRenderer<SparkEntity> {

    private static final Identifier TEXTURE = new ModIdentifier( "textures/particle/lava_spark.png" );
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

    public SparkEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    public void render(SparkEntity sparkEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float w = (float) Math.sin( sparkEntity.age / 10.0f ) * 0.1f + 0.4f;

        matrixStack.push();
        matrixStack.scale(w, w, w);
        matrixStack.translate(0, 0.3, 0);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getModel();
        Matrix3f matrix3f = entry.getNormal();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);

        vertex(vertexConsumer, matrix4f, matrix3f, 0.0F, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, 1.0F, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, 1.0F, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, 0.0F, 1, 0, 0);

        matrixStack.pop();
        super.render(sparkEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    /**
     * @see EntityRenderDispatcher drawFireVertex, for the origin of the '240' magic light value.
     */
    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float f, int j, int k, int l) {
        vertexConsumer
                .vertex(matrix4f, f - 0.5F, j - 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(k, l)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(240)
                .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                .next();
    }

    public Identifier getTexture(SparkEntity sparkEntity) {
        return TEXTURE;
    }

}