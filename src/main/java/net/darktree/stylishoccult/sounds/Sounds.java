package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class Sounds {

    public static BlockSoundGroup FLESH;
    public static BlockSoundGroup LAVA_DEMON;
    public static BlockSoundGroup CANDLE;
    public static BlockSoundGroup URN;

    public static SoundEffect HEARTBEAT = SoundManager.loadSound("heartbeat", SoundCategory.AMBIENT);
    public static SoundEffect LAVA_DEMON_WAKEUP = SoundManager.loadSound("lava_demon_wakeup", SoundCategory.BLOCKS);
    public static SoundEffect LAVA_DEMON_DIE = SoundManager.loadSound("lava_demon_die", SoundCategory.BLOCKS);
    public static SoundEffect URN_BROKEN = SoundManager.loadSound("urn_broken", SoundCategory.BLOCKS);
    public static SoundEffect VENT = SoundManager.loadSound("vent", SoundCategory.BLOCKS).volume(2.8f).pitch(0.8f, 1.1f);
    public static SoundEffect SPORE_ESCAPES = SoundManager.loadSound("spore_escapes", SoundCategory.BLOCKS).volume(0.05f);
    public static SoundEffect SPARK = SoundManager.loadSound("spark", SoundCategory.BLOCKS).pitch(0.8f, 1.1f);
    public static SoundEffect ARC = SoundManager.loadSound("arc", SoundCategory.BLOCKS).pitch(1.0f, 1.8f);
    public static SoundEffect SPELL = SoundManager.loadSound("spell", SoundCategory.BLOCKS).pitch(1.0f, 1.8f);
    public static SoundEffect BOOM = SoundManager.loadSound("boom", SoundCategory.BLOCKS).pitch(1.0f, 1.3f);
    public static SoundEffect TRANSMUTE = SoundManager.loadSound("transmute", SoundCategory.BLOCKS).pitch(1.0f, 1.2f).volume(0.9f);
    public static SoundEffect VOICE = SoundManager.loadSound("voice", SoundCategory.BLOCKS).pitch(0.8f, 1.4f).volume(0.55f);
    public static SoundEffect BOIL = SoundManager.loadSound("boil", SoundCategory.BLOCKS);
    public static SoundEffect CHISEL = SoundManager.loadSound("chisel", SoundCategory.BLOCKS);
    public static SoundEffect ACTIVATE = SoundManager.loadSound("activate", SoundCategory.BLOCKS);
    public static SoundEffect INSPECT = SoundManager.loadSound("inspect", SoundCategory.BLOCKS);

    public static void init() {

        FLESH = SoundManager.createGroup()
                .setBreakSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK)
                .setFallSound(SoundEvents.BLOCK_HONEY_BLOCK_FALL)
                .setHitSound(SoundEvents.BLOCK_HONEY_BLOCK_HIT)
                .setPlaceSound(SoundEvents.BLOCK_HONEY_BLOCK_PLACE)
                .setStepSound(SoundEvents.BLOCK_HONEY_BLOCK_STEP)
                .build();

        LAVA_DEMON = SoundManager.createGroup()
                .setBreakSound(LAVA_DEMON_DIE)
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

        URN = SoundManager.createGroup(0.85f, 0.95f)
                .setBreakSound(URN_BROKEN)
                .setFallSound(SoundEvents.BLOCK_STONE_FALL)
                .setHitSound(SoundEvents.BLOCK_STONE_HIT)
                .setPlaceSound(SoundEvents.BLOCK_STONE_PLACE)
                .setStepSound(SoundEvents.BLOCK_STONE_STEP)
                .build();
    }

}
