package net.darktree.stylishoccult.effects;

import net.darktree.stylishoccult.mixin.StatusEffectInstanceAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface InstanceAwareStatusEffect {

    default int getStoredDuration(StatusEffectInstance instance) {
        return ((StatusEffectInstanceAccessor) instance).getStoredDuration();
    }

    /**
     * Called every update for non-instant effect, instance-aware, requires duration check
     */
    default void instanceUpdate(LivingEntity entity, StatusEffectInstance instance) {

    }

    /**
     * Called when the effect is added to the entity
     */
    default void instanceOnAdded(LivingEntity entity, StatusEffectInstance instance) {

    }

    /**
     * Called when the effect is removed from the entity
     */
    default void instanceOnRemoved(LivingEntity entity, StatusEffectInstance instance) {

    }

}
