package net.darktree.stylishoccult.entities.renderer;

import net.darktree.interference.render.RenderHelper;
import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SparkEntityRenderer extends EntityRenderer<SparkEntity> {

    private static final Identifier[] TEXTURES = {
            new ModIdentifier("textures/particle/spark_0.png"),
            new ModIdentifier("textures/particle/spark_1.png"),
            new ModIdentifier("textures/particle/spark_2.png"),
            new ModIdentifier("textures/particle/spark_3.png"),
            new ModIdentifier("textures/particle/spark_4.png")
    };

    public SparkEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(SparkEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i) {
        float w = (float) Math.sin( entity.age / 10.0f ) * 0.1f + 0.4f;

        matrices.push();
        matrices.scale(w, w, w);
        matrices.translate(0, 0.3, 0);
        RenderHelper.renderCutoutBillboard(getTexture(entity), matrices, vertexConsumerProvider, getLight(i));
        matrices.pop();

        super.render(entity, f, g, matrices, vertexConsumerProvider, i);
    }

    protected int getLight( int i ) {
        return LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    @Override
    public Identifier getTexture(SparkEntity sparkEntity) {
        return TEXTURES[(sparkEntity.age / 2) % TEXTURES.length];
    }

}