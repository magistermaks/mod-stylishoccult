package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class Sounds {

    // TODO: simplify this mess

    public static BlockSoundGroup FLESH;
    public static BlockSoundGroup LAVA_DEMON;
    public static BlockSoundGroup CANDLE;
    public static BlockSoundGroup URN;

    public static void init() {

        SoundManager.loadSound( "lava_demon_wakeup", SoundCategory.BLOCKS );
        SoundManager.loadSound( "lava_demon_die", SoundCategory.BLOCKS );
        SoundManager.loadSound( "urn_broken", SoundCategory.BLOCKS );
        SoundManager.loadSound( "vent", SoundCategory.BLOCKS ).volume( 2.8f ).pitch( 0.8f, 1.1f );
        SoundManager.loadSound( "spore_escapes", SoundCategory.BLOCKS ).volume( 0.05f );

        FLESH = SoundManager.createGroup()
                .setBreakSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK)
                .setFallSound(SoundEvents.BLOCK_HONEY_BLOCK_FALL)
                .setHitSound(SoundEvents.BLOCK_HONEY_BLOCK_HIT)
                .setPlaceSound(SoundEvents.BLOCK_HONEY_BLOCK_PLACE)
                .setStepSound(SoundEvents.BLOCK_HONEY_BLOCK_STEP)
                .build();

        LAVA_DEMON = SoundManager.createGroup()
                .setBreakSound("lava_demon_die")
                .setFallSound(SoundEvents.BLOCK_STONE_FALL)
                .setHitSound(SoundEvents.BLOCK_STONE_HIT)
                .setPlaceSound(SoundEvents.BLOCK_STONE_PLACE)
                .setStepSound(SoundEvents.BLOCK_STONE_STEP)
                .build();

        CANDLE = SoundManager.createGroup()
                .setBreakSound(SoundEvents.BLOCK_STONE_BREAK)
                .setFallSound(SoundEvents.BLOCK_STONE_FALL)
                .setHitSound(SoundEvents.BLOCK_STONE_HIT)
                .setPlaceSound(SoundEvents.BLOCK_STONE_PLACE)
                .setStepSound(SoundEvents.BLOCK_STONE_STEP)
                .build();

        URN = SoundManager.createGroup( 0.85f, 0.95f )
                .setBreakSound("urn_broken")
                .setFallSound(SoundEvents.BLOCK_STONE_FALL)
                .setHitSound(SoundEvents.BLOCK_STONE_HIT)
                .setPlaceSound(SoundEvents.BLOCK_STONE_PLACE)
                .setStepSound(SoundEvents.BLOCK_STONE_STEP)
                .build();
    }

}
