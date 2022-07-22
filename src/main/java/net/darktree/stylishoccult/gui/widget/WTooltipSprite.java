package net.darktree.stylishoccult.gui.widget;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class WTooltipSprite extends WSprite {

	private final BiConsumer<TooltipBuilder, Boolean> appender;

	public WTooltipSprite(Identifier image, BiConsumer<TooltipBuilder, Boolean> appender) {
		super(image);
		this.appender = appender;
	}

	@Override
	public void addTooltip(TooltipBuilder builder) {
		appender.accept(builder, MinecraftClient.getInstance().options.advancedItemTooltips);
	}

}
