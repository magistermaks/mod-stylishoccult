package net.darktree.stylishoccult.entities;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class VoidEntityModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart body;

    public VoidEntityModel() {
        this(0.0F);
    }

    public VoidEntityModel(float scale) {
        this.body = new ModelPart(this, 0, 0);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
    }

}
