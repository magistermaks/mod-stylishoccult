package net.darktree.stylishoccult.effects;

import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class CorruptedBloodEffectInstance extends StatusEffectInstance {

    private int infectionAmplifier;

    public CorruptedBloodEffectInstance(StatusEffect type, int duration, int amplifier) {
        super(type, duration, amplifier);
    }

    public int getInfectionAmplifier() {
        return infectionAmplifier;
    }

    public void setInfectionAmplifier( int value ) {
        infectionAmplifier = value;
    }

}
