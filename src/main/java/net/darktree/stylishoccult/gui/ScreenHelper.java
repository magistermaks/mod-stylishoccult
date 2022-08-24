package net.darktree.stylishoccult.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ScreenHelper {

	public static WGridPanel getRoot(LightweightGuiDescription gui, int w, int h, int grid) {
		WGridPanel root = new WGridPanel(grid);
		gui.setRootPanel(root);
		root.setSize(w * 18, h * 18);
		root.setInsets(Insets.ROOT_PANEL);

		return root;
	}

	public static WToggleButton toggle(String key, Consumer<Boolean> consumer) {
		WToggleButton toggle = new WToggleButton(text(key));
		toggle.setOnToggle(consumer);

		return toggle;
	}

	public static MutableText text(String text, Object... args) {
		return new TranslatableText("gui." + StylishOccult.NAMESPACE + "." + text, args);
	}

}
