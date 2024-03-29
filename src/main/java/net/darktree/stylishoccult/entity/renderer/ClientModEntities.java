package net.darktree.stylishoccult.entity.renderer;

import net.darktree.stylishoccult.entity.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientModEntities {

	public static void init() {
		EntityRendererRegistry.register(ModEntities.SPARK, SparkEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.SPORE, SporeEntityRenderer::new);
	}

}
