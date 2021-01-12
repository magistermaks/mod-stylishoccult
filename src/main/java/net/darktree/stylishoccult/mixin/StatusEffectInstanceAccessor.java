package net.darktree.stylishoccult.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceAccessor {

    @Accessor("duration")
    void setStoredDuration( int value );

    @Accessor("showParticles")
    void setStoredParticlesFlag( boolean value );

    @Accessor("showIcon")
    void setStoredIconFlag( boolean value );

}
