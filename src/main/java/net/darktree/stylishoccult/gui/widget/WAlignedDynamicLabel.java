package net.darktree.stylishoccult.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Supplier;

public class WAlignedDynamicLabel extends WDynamicLabel {

	public WAlignedDynamicLabel(Supplier<String> text) {
		super(text);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		super.paint(matrices, x, y + this.height / 2 - 9/2, mouseX, mouseY);
	}

}
