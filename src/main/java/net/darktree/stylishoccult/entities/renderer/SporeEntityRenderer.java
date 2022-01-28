package net.darktree.stylishoccult.entities.renderer;

import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class SporeEntityRenderer extends SparkEntityRenderer {

    private static final Identifier TEXTURE = new ModIdentifier( "textures/particle/spore.png" );
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

    public SporeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getLight(int i) {
        return i;
    }

    @Override
    public RenderLayer getLayer(SparkEntity entity) {
        return LAYER;
    }

    @Override
    public Identifier getTexture(SparkEntity sparkEntity) {
        return TEXTURE;
    }

}
