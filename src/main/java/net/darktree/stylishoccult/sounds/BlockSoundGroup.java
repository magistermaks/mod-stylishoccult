package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.SoundEvent;

public class BlockSoundGroup {
    public final float volume;
    public final float pitch;

    private SoundEvent breakSound = null;
    private SoundEvent stepSound = null;
    private SoundEvent placeSound = null;
    private SoundEvent hitSound = null;
    private SoundEvent fallSound = null;

    public BlockSoundGroup(float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
    }

    public BlockSoundGroup() {
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    public BlockSoundGroup setBreakSound(SoundEffect effect) {
        setBreakSound(effect.event);
        return this;
    }

    public BlockSoundGroup setBreakSound(SoundEvent event) {
        breakSound = event;
        return this;
    }

    public BlockSoundGroup setStepSound(SoundEffect effect) {
        setStepSound( effect.event);
        return this;
    }

    public BlockSoundGroup setStepSound(SoundEvent event) {
        stepSound = event;
        return this;
    }

    public BlockSoundGroup setPlaceSound(SoundEffect effect) {
        setPlaceSound( effect.event);
        return this;
    }

    public BlockSoundGroup setPlaceSound(SoundEvent event) {
        placeSound = event;
        return this;
    }

    public BlockSoundGroup setHitSound(SoundEffect effect) {
        setHitSound(effect.event);
        return this;
    }

    public BlockSoundGroup setHitSound(SoundEvent event) {
        hitSound = event;
        return this;
    }

    public BlockSoundGroup setFallSound(SoundEffect effect) {
        setFallSound( effect.event);
        return this;
    }

    public BlockSoundGroup setFallSound(SoundEvent event) {
        fallSound = event;
        return this;
    }

    public net.minecraft.sound.BlockSoundGroup build() {
        if( breakSound == null ) throw new RuntimeException("BreakSound not registered!");
        if( stepSound == null ) throw new RuntimeException("StepSound not registered!");
        if( placeSound == null ) throw new RuntimeException("PlaceSound not registered!");
        if( hitSound == null ) throw new RuntimeException("HitSound not registered!");
        if( fallSound == null ) throw new RuntimeException("FallSound not registered!");

        return new net.minecraft.sound.BlockSoundGroup(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
    }
}
