package net.darktree.stylishoccult.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientModEntities {

    public static void init() {
        EntityRendererRegistry.INSTANCE.register( ModEntities.LAVA_SPARK, (dis, ctx) -> new SparkEntityRenderer(dis) );
    }

}
