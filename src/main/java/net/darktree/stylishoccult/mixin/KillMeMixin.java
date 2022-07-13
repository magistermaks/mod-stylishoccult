package net.darktree.stylishoccult.mixin;

import net.darktree.interference.render.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;

@Mixin(ParticleHelper.class)
public class KillMeMixin {

	/**
	 * This is to fix an error in the experimental particle
	 * Interference API, otherwise the game gets stuff in a
	 * infinite loop while reloading resource packs
	 */
	@Redirect(method="getSpritesAndForget", at=@At(value="INVOKE", target="Ljava/util/HashMap;remove(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static Object redirectMyPastMistakes(HashMap map, Object identifier) {
		return map.get(identifier);
	}

}
