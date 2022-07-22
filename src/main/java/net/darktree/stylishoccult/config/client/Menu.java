package net.darktree.stylishoccult.config.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.gui.screen.SettingsScreen;

public class Menu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> SettingsScreen.open(parent, StylishOccult.CONFIG);
	}

}
