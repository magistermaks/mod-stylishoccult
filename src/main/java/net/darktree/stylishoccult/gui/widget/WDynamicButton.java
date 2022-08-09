package net.darktree.stylishoccult.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class WDynamicButton extends WButton {

	private final Supplier<Boolean> supplier;

	public WDynamicButton(Text text, Supplier<Boolean> supplier) {
		super(text);
		this.supplier = supplier;
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		setEnabled(this.supplier.get());
		super.paint(matrices, x, y, mouseX, mouseY);
	}

}
