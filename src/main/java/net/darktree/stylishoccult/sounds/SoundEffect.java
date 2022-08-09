package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SoundEffect {

    public final SoundEvent event;
    public final SoundCategory category;

    private float pitchMin = 1.0f;
    private float pitchMax = 1.0f;
    private float volume = 1.0f;

    public SoundEffect (SoundEvent event, SoundCategory category) {
        this.event = event;
        this.category = category;
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

    public void play(World world, BlockPos pos) {
        SoundManager.playSound(world, pos, this, -1, -1);
    }

}
