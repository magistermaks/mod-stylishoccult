package net.darktree.stylishoccult.block.rune.client;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.script.element.view.ElementView;
import net.darktree.stylishoccult.script.engine.BaseStack;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Utils;
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

	// TODO: cleanup required asap

	public DebugRuneScreen(GuiDescription description) {
		super(description);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public static void open(BlockPos pos, NbtCompound nbt) {
		MinecraftClient.getInstance().setScreen(new DebugRuneScreen(new Gui(pos, nbt)));
	}

	public static class Gui extends LightweightGuiDescription {

		private WToggleButton numericalToggle, dropToggle;

		private final BlockPos pos;

		public Gui(BlockPos pos, NbtCompound nbt) {
			this.pos = pos;
			Script script = Script.fromNbt(nbt);

			WGridPanel root = new WGridPanel();
			setRootPanel(root);
			root.setSize(14 * 18, 13 * 18);
			root.setInsets(Insets.ROOT_PANEL);

			WLabel title = new WLabel(Utils.guiText("debug.title"));
			title.setHorizontalAlignment(HorizontalAlignment.CENTER);
			root.add(title, 0, 0, 14, 1);

			WLabel subtitle = new WLabel(Utils.guiText("debug.snapshot"));
			subtitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
			subtitle.setColor(Formatting.DARK_GRAY.getColorValue());
			root.add(subtitle, 0, 1, 14, 1);

			WGridPanel right = new WGridPanel();
			right.setInsets(new Insets(0, 4));

			WCardPanel cards = new WCardPanel();

			numericalToggle = new WToggleButton(Utils.guiText("debug.toggle.numerical"));
			right.add(numericalToggle, 0, 0, 7, 1);

			dropToggle = new WToggleButton(Utils.guiText("debug.toggle.stack"));
			dropToggle.setOnToggle(on -> {
				cards.setSelectedIndex(on ? 1 : 0);
			});

			right.add(dropToggle, 0, 1, 7, 1);

			WDynamicLabel cardLabel = new WDynamicLabel(() -> {
				return dropToggle.getToggle() ? Utils.guiText("debug.tab.drop").getString() : Utils.guiText("debug.tab.stack").getString();
			});
			cardLabel.setAlignment(HorizontalAlignment.CENTER);
			root.add(cardLabel, 0, 2, 7, 1);

			cards.add(getElementListPanel(script.stack));
			cards.add(getElementListPanel(script.ring));

			root.add(cards, 0, 3, 7, 9);

			root.add(right, 7, 3, 7, 9);

			root.validate(this);
		}
		private WPanel getElementListPanel(BaseStack stack) {
			WGridPanel panel = new WGridPanel(3);

			AtomicInteger offset = new AtomicInteger();
			stack.reset(element -> {
				panel.add(getElementPanel(element), 0, offset.getAndAdd(7));
			});

			if (offset.get() == 0) {
				WLabel label = new WLabel(Utils.guiText("debug.empty"));
				label.setHorizontalAlignment(HorizontalAlignment.CENTER);
				label.setColor(Formatting.GRAY.getColorValue());

				panel.add(label, 0, 0, 42, 6);
			}

			WScrollPanel scrollPanel = new WScrollPanel(panel);
			scrollPanel.setScrollingHorizontally(TriState.FALSE);
			scrollPanel.setScrollingVertically(TriState.TRUE);

			return scrollPanel;
		}

		private WGridPanel getElementPanel(StackElement element) {
			ElementView view = element.view();

			WSprite icon = new WSprite(view.getIcon()) {
				@Override
				public void addTooltip(TooltipBuilder tooltip) {
					String tip = view.getTooltip();

					if (tip != null) {
						tooltip.add(new LiteralText(view.getBody()));

						if (MinecraftClient.getInstance().options.advancedItemTooltips) {
							tooltip.add(new LiteralText(view.getTooltip()).formatted(Formatting.GRAY));
						}
					}
				}
			};

			WLabel label = new WLabel(view.getHead());
			WDynamicLabel body = new WDynamicLabel(() -> {
				return numericalToggle.getToggle() ? String.valueOf(element.value()) : view.getBody();
			}, Formatting.DARK_GRAY.getColorValue());

			WGridPanel panel = new WGridPanel(3);

			panel.add(icon, 0, 0, 6, 6);
			panel.add(label, 7, 0, 36, 3);
			panel.add(body, 7, 3, 36, 3);

			return panel;
		}

	}

}
