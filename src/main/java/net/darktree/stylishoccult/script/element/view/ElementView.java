package net.darktree.stylishoccult.script.element.view;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class ElementView {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.0#####");

	public static final Identifier NUMBER_ICON = ModIdentifier.of("textures/rune/element/number.png");
	public static final Identifier ITEM_ICON = ModIdentifier.of("textures/rune/element/item.png");
	public static final Identifier FLUID_ICON = ModIdentifier.of("textures/rune/element/fluid.png");

	private final String title;
	private final String body;
	private final Identifier icon;
	private final String tooltip;

	protected ElementView(String title, Identifier icon, String body, @Nullable String tooltip) {
		this.title = title;
		this.body = body;
		this.tooltip = tooltip;
		this.icon = icon;
	}

	public static ElementView of(String name, Identifier icon, String text, @Nullable String tooltip) {
		return new ElementView("script." + StylishOccult.NAMESPACE + ".element." + name, icon, text, tooltip);
	}

	/**
	 * Get a numerical string of the given element
	 */
	public static String numerical(StackElement element) {
		return FORMAT.format(element.value());
	}

	/**
	 * Get the name of this component
	 */
	public Text getHead() {
		return new TranslatableText(this.title);
	}

	/**
	 * Get a translated description
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * Get an advanced tooltip, or null if no tooltip should be shown
	 */
	@Nullable
	public String getTooltip() {
		return this.tooltip;
	}

	/**
	 * Get the identifier of the element texture
	 */
	public Identifier getIcon() {
		return this.icon;
	}

}
