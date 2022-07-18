package net.darktree.stylishoccult.block.rune.client;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class WSimpleLabel extends WLabel {

	public WSimpleLabel(String key, HorizontalAlignment alignment, Formatting formatting) {
		super(Utils.guiText(key));
		setHorizontalAlignment(alignment);
		setColor(formatting);
	}

	public WSimpleLabel(String key, HorizontalAlignment alignment) {
		super(Utils.guiText(key));
		setHorizontalAlignment(alignment);
	}

	public void setColor(Formatting formatting) {
		super.setColor(Objects.requireNonNull(formatting.getColorValue()));
	}

}
