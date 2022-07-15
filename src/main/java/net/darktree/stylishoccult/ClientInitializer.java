package net.darktree.stylishoccult;

import net.darktree.interference.MessageInjector;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.blocks.entities.client.AltarPlateBlockEntityRenderer;
import net.darktree.stylishoccult.blocks.entities.client.OccultCauldronBlockEntityRenderer;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ClientInitializer implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModBlocks.clientInit();
		Network.clientInit();
		ModItems.clientInit();
		Particles.clientInit();
		ModEntities.clientInit();

		BlockEntityRendererRegistry.register(BlockEntities.ALTAR_PLATE, AltarPlateBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntities.OCCULT_CAULDRON, OccultCauldronBlockEntityRenderer::new);

		MessageInjector.injectPlain("Down with JSON!");
		MessageInjector.injectPlain("Tiny potato!");
		MessageInjector.injectPlain("Stylish nightmare!");
		MessageInjector.injectPlain("The nightmare before breakfast!");
		MessageInjector.injectPlain("The wheels of progress frozen motionless...");
		MessageInjector.injectPlain("By the power of The Mixin Subsystem!");

		StylishOccult.LOGGER.info("Additional sound effects from https://www.zapsplat.com");
	}

}
