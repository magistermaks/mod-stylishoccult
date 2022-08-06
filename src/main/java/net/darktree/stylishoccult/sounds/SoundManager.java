package net.darktree.stylishoccult.sounds;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Random;

public class SoundManager {

    private static final Random random = new Random();

    public static SoundEffect loadSound(String name, SoundCategory soundCategory) {
        Identifier identifier = new ModIdentifier(name);
        SoundEvent soundEvent = Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
        return new SoundEffect(soundEvent, soundCategory);
    }

    public static BlockSoundGroup createGroup( float volume, float pitch ) {
        return new BlockSoundGroup(volume, pitch);
    }

    public static BlockSoundGroup createGroup() {
        return new BlockSoundGroup();
    }

    @Deprecated
    public static void playSound(World world, BlockPos pos, SoundEffect effect, float volume, float pitch) {
        volume = (volume == -1 ? effect.getVolume() : volume);
        pitch = (pitch == -1 ? effect.getPitch(random) : pitch);

        if (!world.isClient) {
            world.playSound(
                    null,
                    pos,
                    effect.event,
                    effect.category,
                    volume,
                    pitch
            );
        }
    }

}
