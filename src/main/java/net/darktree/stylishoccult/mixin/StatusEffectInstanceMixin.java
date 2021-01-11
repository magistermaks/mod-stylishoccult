package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.effects.CorruptedBloodEffectInstance;
import net.darktree.stylishoccult.effects.InstanceAwareStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {

    private InstanceAwareStatusEffect getExtendedEffect() {
        return (InstanceAwareStatusEffect) type;
    }

    private StatusEffectInstance getSelf() {
        return (StatusEffectInstance) (Object) this;
    }

    @Shadow @Final
    private StatusEffect type;

    @Inject(at=@At("HEAD"), method="update(Lnet/minecraft/entity/LivingEntity;Ljava/lang/Runnable;)Z")
    public void update(LivingEntity entity, Runnable runnable, CallbackInfoReturnable<Boolean> cir) {
        if( type instanceof InstanceAwareStatusEffect ) {
            getExtendedEffect().instanceUpdate( entity, getSelf() );
        }
    }

    @Inject(at=@At("HEAD"), method="copyFrom(Lnet/minecraft/entity/effect/StatusEffectInstance;)V")
    public void copyFrom(StatusEffectInstance that, CallbackInfo ci) {
        if (((StatusEffectInstance) (Object) this) instanceof CorruptedBloodEffectInstance) {
            if (that instanceof CorruptedBloodEffectInstance) {
                CorruptedBloodEffectInstance inst = (CorruptedBloodEffectInstance) that;
                ((CorruptedBloodEffectInstance) (Object) this).setInfectionAmplifier( inst.getInfectionAmplifier() );
                return;
            }
        }

        StylishOccult.LOGGER.error("CorruptedBloodEffectInstance object expected!");
    }

}
