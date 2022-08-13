package net.darktree.stylishoccult.gui.widget;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.darktree.stylishoccult.gui.ScreenHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.function.BiConsumer;

public class WSimpleLabel extends WLabel {

	private BiConsumer<TooltipBuilder, Boolean> appender;

	public WSimpleLabel(String key, HorizontalAlignment alignment, Formatting formatting) {
		super(ScreenHelper.text(key));
		setHorizontalAlignment(alignment);
		setColor(formatting);
	}

	public WSimpleLabel(String key, HorizontalAlignment alignment) {
		super(ScreenHelper.text(key));
		setHorizontalAlignment(alignment);
	}

	public void setColor(Formatting formatting) {
		super.setColor(Objects.requireNonNull(formatting.getColorValue()));
	}

	@Override
	public void addTooltip(TooltipBuilder builder) {
		if (appender != null) {
			appender.accept(builder, MinecraftClient.getInstance().options.advancedItemTooltips);
		}
	}

	public void setTooltip(BiConsumer<TooltipBuilder, Boolean> appender) {
		this.appender = appender;
	}

}
