package net.darktree.stylishoccult.sounds;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class Sounds {

	public static BlockSoundGroup FLESH;
	public static BlockSoundGroup LAVA_DEMON;

	public static SoundEffect HEARTBEAT = SoundEffect.load("heartbeat", SoundCategory.AMBIENT);
	public static SoundEffect LAVA_DEMON_WAKEUP = SoundEffect.load("lava_demon_wakeup", SoundCategory.BLOCKS);
	public static SoundEffect LAVA_DEMON_DIE = SoundEffect.load("lava_demon_die", SoundCategory.BLOCKS);
	public static SoundEffect VENT = SoundEffect.load("vent", SoundCategory.BLOCKS).volume(2.8f).pitch(0.8f, 1.1f);
	public static SoundEffect SPORE_ESCAPES = SoundEffect.load("spore_escapes", SoundCategory.HOSTILE).volume(0.05f);
	public static SoundEffect SPARK = SoundEffect.load("spark", SoundCategory.BLOCKS).pitch(0.8f, 1.1f);
	public static SoundEffect ARC = SoundEffect.load("arc", SoundCategory.BLOCKS).pitch(1.0f, 1.8f);
	public static SoundEffect SPELL = SoundEffect.load("spell", SoundCategory.BLOCKS).pitch(1.0f, 1.8f);
	public static SoundEffect BOOM = SoundEffect.load("boom", SoundCategory.BLOCKS).pitch(1.0f, 1.3f);
	public static SoundEffect TRANSMUTE = SoundEffect.load("transmute", SoundCategory.BLOCKS).pitch(1.0f, 1.2f).volume(0.9f);
	public static SoundEffect VOICE = SoundEffect.load("voice", SoundCategory.BLOCKS).pitch(0.8f, 1.4f).volume(0.55f);
	public static SoundEffect BOIL = SoundEffect.load("boil", SoundCategory.AMBIENT);
	public static SoundEffect CHISEL = SoundEffect.load("chisel", SoundCategory.BLOCKS);
	public static SoundEffect ACTIVATE = SoundEffect.load("activate", SoundCategory.BLOCKS);
	public static SoundEffect INSPECT = SoundEffect.load("inspect", SoundCategory.BLOCKS);
	public static SoundEffect DISPOSE = SoundEffect.load("dispose", SoundCategory.AMBIENT);

	public static void init() {
		FLESH = new BlockSoundGroup(1, 1,
				SoundEvents.BLOCK_HONEY_BLOCK_BREAK,
				SoundEvents.BLOCK_HONEY_BLOCK_STEP,
				SoundEvents.BLOCK_HONEY_BLOCK_PLACE,
				SoundEvents.BLOCK_HONEY_BLOCK_HIT,
				SoundEvents.BLOCK_HONEY_BLOCK_FALL
		);

		LAVA_DEMON = new BlockSoundGroup(1, 1,
				LAVA_DEMON_DIE.event,
				SoundEvents.BLOCK_STONE_STEP,
				SoundEvents.BLOCK_STONE_PLACE,
				SoundEvents.BLOCK_STONE_HIT,
				SoundEvents.BLOCK_STONE_FALL
		);
	}

}
