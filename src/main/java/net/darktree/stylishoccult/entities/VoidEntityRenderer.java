package net.darktree.stylishoccult.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class VoidEntityRenderer<T extends MobEntity> extends MobEntityRenderer<T, VoidEntityModel<T>> {

    public VoidEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new VoidEntityModel<>(), 0);
    }

    @Override
    public Identifier getTexture(T entity) {
        return new Identifier("missingno");
    }

}