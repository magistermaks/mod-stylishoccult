package net.darktree.stylishoccult.sounds;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class SoundManager {

    private static final Random random = new Random();
    private static final HashMap<String, SoundEffect> soundEffectHashMap = new HashMap<>();

    public static SoundEffect loadSound(String name, SoundCategory soundCategory) {
        Identifier identifier = new ModIdentifier( name );
        SoundEvent soundEvent = Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
        SoundEffect effect = new SoundEffect( soundEvent, soundCategory );
        soundEffectHashMap.put(name, effect);
        return effect;
    }

    public static SoundEffect getSound( String name ) {
        return soundEffectHashMap.get(name);
    }

    public static BlockSoundGroup createGroup( float volume, float pitch ) {
        return new BlockSoundGroup( volume, pitch );
    }

    public static BlockSoundGroup createGroup() {
        return new BlockSoundGroup();
    }

    public static void playSound(World world, BlockPos pos, String name, float volume, float pitch) {
        SoundEffect effect = soundEffectHashMap.get(name);
        volume = (volume == -1 ? effect.getVolume() : volume);
        pitch = (pitch == -1 ? effect.getPitch(random) : pitch);

        if( !world.isClient ) {
            world.playSound(
                    null,
                    pos,
                    effect.soundEvent,
                    effect.soundCategory,
                    volume,
                    pitch
            );
        }
    }

    public static void playSound(World world, BlockPos pos, String name) {
        playSound( world, pos, name, -1, -1 );
    }

}
