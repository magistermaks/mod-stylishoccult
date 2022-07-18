package net.darktree.stylishoccult.block.rune.client;

import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.darktree.stylishoccult.utils.Utils;

import java.util.function.Consumer;

public class WSimpleToggle extends WToggleButton {

	public WSimpleToggle(String key, Consumer<Boolean> consumer) {
		super(Utils.guiText(key));
		setOnToggle(consumer);
	}

}
