package net.darktree.stylishoccult.entities.renderer;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

public class BrainEntityRenderer extends SlimeEntityRenderer {

    private static final Identifier TEXTURE = new ModIdentifier("textures/entity/brain/brain.png");

    public BrainEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(SlimeEntity slimeEntity) {
        return TEXTURE;
    }

}
