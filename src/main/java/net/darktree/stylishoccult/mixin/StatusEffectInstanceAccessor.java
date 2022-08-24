package net.darktree.stylishoccult.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceAccessor {

	@Accessor("showParticles")
	void stylish_setStoredParticlesFlag(boolean value );

}
