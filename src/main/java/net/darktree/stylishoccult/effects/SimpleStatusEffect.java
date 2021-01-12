package net.darktree.stylishoccult.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleStatusEffect extends StatusEffect {

    private final boolean instant;

    protected SimpleStatusEffect(StatusEffectType type, boolean instant, int color) {
        super(type, color);
        this.instant = instant;
    }

    /**
     * Called every tick for non-instant effect
     */
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        onUpdate( entity, amplifier );
    }

    /**
     * Called only for instant effects
     */
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        onUpdate( target, amplifier );
    }

    /**
     * Called at least once for every effect
     */
    public void onUpdate(LivingEntity entity, int amplifier) {

    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public boolean isInstant() {
        return instant;
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

}
