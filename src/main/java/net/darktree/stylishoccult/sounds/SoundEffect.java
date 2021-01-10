package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.Random;

public class SoundEffect {

    public SoundEvent soundEvent;
    public SoundCategory soundCategory;

    private float pitchMin = 1.0f;
    private float pitchMax = 1.0f;
    private float volume = 1.0f;

    public SoundEffect (SoundEvent soundEvent, SoundCategory soundCategory) {
        this.soundEvent = soundEvent;
        this.soundCategory = soundCategory;
    }

    public SoundEffect pitch( float min, float max ) {
        this.pitchMin = min;
        this.pitchMax = max;
        return this;
    }

    public SoundEffect volume( float volume ) {
        this.volume = volume;
        return this;
    }

    public float getPitch(Random random) {
        return random.nextFloat() * (pitchMax - pitchMin) + pitchMin;
    }

    public float getVolume() {
        return volume;
    }

}
