package net.darktree.stylishoccult.entities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.LeashKnotEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

// It's broken rn, don't spawn sparks i guess
public class SparkEntityRenderer extends EntityRenderer<SparkEntity> {

    // TODO: change when i get this thing working
    private static final Identifier TEXTURE = new Identifier("textures/entity/lead_knot.png");

    public SparkEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    public void render(SparkEntity entity, float q, float w, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);

        VertexConsumer vertices = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        float tickDelta = client.getTickDelta();

        float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;

        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) - vec3d.getX());
        float g = (float)(MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) - vec3d.getY());
        float h = (float)(MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) - vec3d.getZ());
        Quaternion quaternion2 = camera.getRotation();

        Vector3f vector3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f.rotate(quaternion2);

        Vector3f[] vector3fs = new Vector3f[] {
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        for(int k = 0; k < 4; ++k) {
            Vector3f vector3f2 = vector3fs[k];
            vector3f2.rotate(quaternion2);
            vector3f2.scale(1);
            vector3f2.add(f, g, h);
        }

        float l = 0;
        float m = 0;
        float n = 8;
        float o = 8;

        vertices.vertex(vector3fs[0].getX(), vector3fs[0].getY(), vector3fs[0].getZ()).texture(m, o).color(red, green, blue, alpha).light(i).next();
        vertices.vertex(vector3fs[1].getX(), vector3fs[1].getY(), vector3fs[1].getZ()).texture(m, n).color(red, green, blue, alpha).light(i).next();
        vertices.vertex(vector3fs[2].getX(), vector3fs[2].getY(), vector3fs[2].getZ()).texture(l, n).color(red, green, blue, alpha).light(i).next();
        vertices.vertex(vector3fs[3].getX(), vector3fs[3].getY(), vector3fs[3].getZ()).texture(l, o).color(red, green, blue, alpha).light(i).next();

        matrixStack.pop();
        super.render(entity, q, w, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(SparkEntity leashKnotEntity) {
        return TEXTURE;
    }

}