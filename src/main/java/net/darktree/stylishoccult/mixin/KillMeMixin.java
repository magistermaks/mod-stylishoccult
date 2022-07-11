package net.darktree.stylishoccult.mixin;

import net.darktree.interference.render.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;

@Mixin(ParticleHelper.class)
public class KillMeMixin {

	@Redirect(method="getSpritesAndForget", at=@At(value="INVOKE", target="Ljava/util/HashMap;remove(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static Object redirectMyPastMistakes(HashMap map, Object identifier) {
		return map.get(identifier);
	}

}
