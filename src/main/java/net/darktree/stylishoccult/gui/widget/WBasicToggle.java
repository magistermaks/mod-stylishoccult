package net.darktree.stylishoccult.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class WBasicToggle extends WButton {

	private final Text on, off;
	private boolean state;

	public WBasicToggle(boolean initial, Consumer<Boolean> consumer) {
		on = Utils.guiText("config.on");
		off = Utils.guiText("config.off");
		state = initial;
		setOnClick(() -> {
			state = !state;
			consumer.accept(state);
		});
	}

	@Override
	public void tick() {
		setLabel(state ? on : off);
		super.tick();
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
