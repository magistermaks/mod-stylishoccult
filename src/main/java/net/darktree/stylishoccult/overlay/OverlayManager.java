package net.darktree.stylishoccult.overlay;

import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class OverlayManager {

	private static float intensity = 0;
	private static Identifier texture;

	public static final Identifier MADNESS = new Identifier(StylishOccult.NAMESPACE, "textures/misc/madness_outline.png");

	public static void show(Identifier texture, float intensity) {
		OverlayManager.texture = texture;
		OverlayManager.intensity = intensity;
	}

	public static void hide() {
		OverlayManager.texture = null;
	}

	public static float getIntensity() {
		return intensity;
	}

	public static Identifier getTexture() {
		return OverlayManager.texture;
	}

}
