package net.darktree.stylishoccult.gui.screen;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.config.Config;
import net.darktree.stylishoccult.gui.ScreenHelper;
import net.darktree.stylishoccult.gui.widget.WAlignedDynamicLabel;
import net.darktree.stylishoccult.gui.widget.WBasicToggle;
import net.darktree.stylishoccult.gui.widget.WDynamicButton;
import net.darktree.stylishoccult.gui.widget.WSimpleLabel;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class SettingsScreen<T> extends CottonClientScreen {

	private final Config<T> config;

	public SettingsScreen(GuiDescription description, Config<T> config) {
		super(description);
		this.config = config;
	}

	public static <T> SettingsScreen<T> open(Screen parent, Config<T> config) {
		return new SettingsScreen<>(new Gui<>(parent, config), config);
	}

	@Override
	public void removed() {
		config.discardAllChanges(); // if there were any changes made there are now applied anyway
		StylishOccult.SETTING = StylishOccult.CONFIG.getConfigured(); // update current configuration
	}

	public static class Gui<T> extends LightweightGuiDescription {

		public Gui(Screen parent, Config<T> config) {
			Set<Map.Entry<String, List<Config.Property<T>>>> groups = config.getGroups();

			// create root
			WGridPanel root = ScreenHelper.getRoot(this, 14, 13, 3);

			// create property list
			WGridPanel list = new WGridPanel(3);
			int i = 0;

			for (Map.Entry<String, List<Config.Property<T>>> group : groups) {

				// add group title
				list.add(getGroupLabel(group.getKey()), 0, i, 84, 6);
				i += 8;

				// add all properties from this group
				for (Config.Property<T> property : group.getValue()) {
					list.add(getPropertyPanel(property), 0, i, 24, 6);
					i += 8;
				}
			}

			// scroll the list of properties
			WScrollPanel scroll = new WScrollPanel(list);
			scroll.setScrollingHorizontally(TriState.FALSE);
			scroll.setScrollingVertically(TriState.TRUE);
			root.add(scroll, 0, 0, 84, 72);

			// unsaved warning label
			WAlignedDynamicLabel info = new WAlignedDynamicLabel(() -> Utils.guiText("config.unsaved", config.getChangedCount()).getString());
			root.add(info, 0, 78, 24, 6);

			// cancel and quit button
			WButton cancel = new WButton(Utils.guiText("config.done"));
			cancel.setOnClick(() -> MinecraftClient.getInstance().setScreen(parent));
			root.add(cancel, 47, 78, 18, 6);

			// save button
			WDynamicButton save = new WDynamicButton(Utils.guiText("config.save"), () -> config.getChangedCount() > 0);
			save.setOnClick(config::applyAllChanges);
			root.add(save, 66, 78, 18, 6);

			root.validate(this);

		}

		private WPanel getPropertyPanel(Config.Property<T> property) {
			WGridPanel option = new WGridPanel(3);

			// property title
			WSimpleLabel title = new WSimpleLabel("config." + property.key + ".title", HorizontalAlignment.LEFT);
			title.setVerticalAlignment(VerticalAlignment.CENTER);
			title.setTooltip((builder, advanced) -> {
				Arrays.stream(Utils.guiText("config." + property.key + ".description").getString().split("\n")).map(LiteralText::new).forEach(builder::add);

				if (property.requiresRestart()) {
					builder.add(Utils.guiText("config.restart").formatted(Formatting.RED));
				}

				if (advanced) {
					builder.add(new LiteralText(property.key).formatted(Formatting.DARK_GRAY));
				}
			});
			option.add(title, 0, 0, 48, 6);

			// reset button
			WDynamicButton reset = new WDynamicButton(Utils.guiText("config.reset"), property::isNotDefault);

			// input filed
			if (property.type == Boolean.class) {
				WBasicToggle button = new WBasicToggle((Boolean) property.get(), property::set);
				option.add(button, 54, 0, 12, 6);

				reset.setOnClick(() -> {
					property.restore();
					button.setState((Boolean) property.get());
				});
			} else {
				WTextField input = new WTextField();
				input.setText(property.get().toString());
				input.setTextPredicate(property::parse);
				option.add(input, 54, 0, 12, 6);

				reset.setOnClick(() -> {
					property.restore();
					input.setText(property.get().toString());
				});
			}

			// add reset button
			option.add(reset, 67, 0, 12, 6);

			return option;
		}

		private WLabel getGroupLabel(String group) {
			WSimpleLabel label = new WSimpleLabel("config." + group, HorizontalAlignment.CENTER);
			label.setVerticalAlignment(VerticalAlignment.CENTER);
			label.setColor(Formatting.DARK_GRAY);

			return label;
		}

	}

}