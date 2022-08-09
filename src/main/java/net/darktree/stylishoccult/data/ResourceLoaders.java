package net.darktree.stylishoccult.data;

import net.darktree.stylishoccult.network.Network;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class ResourceLoaders {

	public final static AltarRitualResourceLoader ALTAR_RITUALS = new AltarRitualResourceLoader();

	public static void init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ALTAR_RITUALS);

		// sync altar rituals to the player
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			Network.ALTAR_SYNC.send(sender);
		});
	}

}
