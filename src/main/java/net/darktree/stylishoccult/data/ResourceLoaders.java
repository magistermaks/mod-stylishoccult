package net.darktree.stylishoccult.data;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class ResourceLoaders {

	public final static AltarRitualResourceLoader ALTAR_RITUALS = new AltarRitualResourceLoader();

	public static void init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ALTAR_RITUALS);
	}

}
