package net.darktree.stylishoccult.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ScreenHelper {

	public static WGridPanel getRoot(LightweightGuiDescription gui, int w, int h, int grid) {
		WGridPanel root = new WGridPanel(grid);
		gui.setRootPanel(root);
		root.setSize(w * 18, h * 18);
		root.setInsets(Insets.ROOT_PANEL);

		return root;
	}


}
