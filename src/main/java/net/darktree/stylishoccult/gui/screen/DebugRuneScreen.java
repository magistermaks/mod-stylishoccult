package net.darktree.stylishoccult.gui.screen;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.gui.ScreenHelper;
import net.darktree.stylishoccult.gui.widget.WSimpleLabel;
import net.darktree.stylishoccult.gui.widget.WTooltipSprite;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.script.element.view.ElementView;
import net.darktree.stylishoccult.script.engine.BaseStack;
import net.darktree.stylishoccult.script.engine.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class DebugRuneScreen extends CottonClientScreen {

	public DebugRuneScreen(GuiDescription description) {
		super(description);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	public static void open(BlockPos pos, NbtCompound nbt) {
		MinecraftClient.getInstance().setScreen(new DebugRuneScreen(new Gui(pos, nbt)));
	}

	public static class Gui extends LightweightGuiDescription {

		private boolean numericalView;

		public Gui(BlockPos pos, NbtCompound nbt) {
			StylishOccult.debug("Displaying debug information for rune at: [" + pos.toShortString() + "]");

			// create root
			WGridPanel root = ScreenHelper.getRoot(this, 14, 13, 18);

			// title
			WSimpleLabel title = new WSimpleLabel("debug.title", HorizontalAlignment.CENTER);
			root.add(title, 0, 0, 14, 1);

			// subtitle
			WSimpleLabel subtitle = new WSimpleLabel("debug.snapshot", HorizontalAlignment.CENTER, Formatting.DARK_GRAY);
			root.add(subtitle, 0, 1, 14, 1);

			// create panels
			WCardPanel cards = getStackCards(Script.fromNbt(nbt));
			WPanel settings = getSettingsPanel(cards);

			// append panels
			root.add(cards, 0, 2, 7, 10);
			root.add(settings, 7, 3, 7, 9);

			root.validate(this);
		}

		/**
		 * Get the panel representing the settings
		 * @param cards the WCardPanel widget to be controlled
		 */
		private WPanel getSettingsPanel(WCardPanel cards) {
			WGridPanel panel = new WGridPanel();
			panel.setInsets(new Insets(0, 4));

			WToggleButton numerical = ScreenHelper.toggle("debug.toggle.numerical", on -> numericalView = on);
			panel.add(numerical, 0, 0, 7, 1);

			WToggleButton stack = ScreenHelper.toggle("debug.toggle.stack", on -> cards.setSelectedIndex(on ? 1 : 0));
			panel.add(stack, 0, 1, 7, 1);

			return panel;
		}

		/**
		 * Get the stack cards panel
		 */
		private WCardPanel getStackCards(Script script) {
			WCardPanel cards = new WCardPanel();

			cards.add(getStackPanel(script.stack, "debug.tab.stack"));
			cards.add(getStackPanel(script.ring, "debug.tab.drop"));

			return cards;
		}

		/**
		 * Get a panel representing the given stack
		 */
		private WPanel getStackPanel(BaseStack stack, String key) {
			WGridPanel panel = new WGridPanel();

			// add title
			WLabel title = new WSimpleLabel(key, HorizontalAlignment.CENTER);
			panel.add(title, 0, 0, 7, 1);

			// create element list
			WScrollPanel list = new WScrollPanel(getStackEntries(stack));
			list.setScrollingHorizontally(TriState.FALSE);
			list.setScrollingVertically(TriState.TRUE);
			panel.add(list, 0, 1, 7, 9);

			return panel;
		}

		/**
		 * Get a list of stack entries, or an "empty" message
		 */
		private WGridPanel getStackEntries(BaseStack stack) {
			WGridPanel entries = new WGridPanel(3);

			AtomicInteger offset = new AtomicInteger();
			stack.reset(element -> {
				entries.add(getStackElement(element), 0, offset.getAndAdd(7));
			});

			// add message if there were no entries
			if (offset.get() == 0) {
				WSimpleLabel label = new WSimpleLabel("debug.empty", HorizontalAlignment.CENTER, Formatting.GRAY);
				entries.add(label, 0, 0, 42, 6);
			}

			return entries;
		}

		/**
		 * Get a panel representing a single stack entry generated using ElementView
		 */
		private WGridPanel getStackElement(StackElement element) {
			ElementView view = element.view();
			WGridPanel panel = new WGridPanel(3);

			// add icon with tooltip
			WTooltipSprite icon = new WTooltipSprite(view.getIcon(), (builder, advanced) -> {
				String tip = view.getTooltip();

				if (tip != null) {
					builder.add(new LiteralText(view.getBody()));

					if (advanced) {
						builder.add(new LiteralText(tip).formatted(Formatting.GRAY));
					}
				}
			});
			panel.add(icon, 0, 0, 6, 6);

			// add entry title
			WLabel title = new WLabel(view.getHead());
			panel.add(title, 7, 0, 36, 3);

			// add entry description
			WDynamicLabel body = new WDynamicLabel(() -> numericalView ? ElementView.numerical(element) : view.getBody(), Formatting.DARK_GRAY.getColorValue());
			panel.add(body, 7, 3, 36, 3);

			return panel;
		}

	}

}
