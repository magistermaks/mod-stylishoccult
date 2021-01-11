package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.effects.InstanceAwareStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private InstanceAwareStatusEffect getStatusEffect( StatusEffectInstance instance ) {
        StatusEffect effect = instance.getEffectType();

        if( effect instanceof InstanceAwareStatusEffect) {
            return (InstanceAwareStatusEffect) effect;
        }

        return null;
    }

    private void callOnApplied( StatusEffectInstance instance ) {
        InstanceAwareStatusEffect effect = getStatusEffect( instance );

        if( effect != null ) {
            effect.instanceOnAdded( (LivingEntity) (Object) this, instance );
        }
    }

    private void callOnRemoved( StatusEffectInstance instance ) {
        InstanceAwareStatusEffect effect = getStatusEffect( instance );

        if( effect != null ) {
            effect.instanceOnRemoved( (LivingEntity) (Object) this, instance );
        }
    }

    @Inject(at=@At("HEAD"), method="onStatusEffectApplied(Lnet/minecraft/entity/effect/StatusEffectInstance;)V")
    public void onStatusEffectApplied(StatusEffectInstance effect, CallbackInfo ci) {
        callOnApplied(effect);
    }

    @Inject(at=@At("HEAD"), method="onStatusEffectUpgraded(Lnet/minecraft/entity/effect/StatusEffectInstance;Z)V")
    public void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, CallbackInfo ci) {
        if( reapplyEffect ) {
            callOnRemoved(effect);
            callOnApplied(effect);
        }
    }

    @Inject(at=@At("HEAD"), method="onStatusEffectRemoved(Lnet/minecraft/entity/effect/StatusEffectInstance;)V")
    public void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        callOnRemoved(effect);
    }



}
