package net.darktree.stylishoccult;

import net.darktree.interference.MessageInjector;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.entity.altar.AltarPlateBlockEntityRenderer;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntityRenderer;
import net.darktree.stylishoccult.entity.ModEntities;
import net.darktree.stylishoccult.item.ModItems;
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

		MessageInjector.injectPlain("May require a sacrifice in blood!");
		MessageInjector.injectPlain("May include tiny potatoes!");
		MessageInjector.injectPlain("Stylish nightmares!");
		MessageInjector.injectPlain("By the Mad Arab, Abdul Alhazred!");
		MessageInjector.injectPlain("The wheels of progress frozen motionless...");
		MessageInjector.injectPlain("Power of The Mixin Subsystem compels you!");
		MessageInjector.injectPlain("With strange aeons even death may die.");
		MessageInjector.injectPlain("SplashScreenOutOfBoundsException!");
		MessageInjector.injectPlain("Now with two poems!");

		StylishOccult.LOGGER.info("Some sound effects for Stylish Occult are sourced from https://www.zapsplat.com");
	}

}
