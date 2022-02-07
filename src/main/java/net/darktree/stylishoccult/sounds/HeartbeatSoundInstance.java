package net.darktree.stylishoccult.sounds;

import net.darktree.stylishoccult.overlay.PlayerEntityDuck;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;

public class HeartbeatSoundInstance extends MovingSoundInstance {

	PlayerEntityDuck player;

	public HeartbeatSoundInstance(PlayerEntityDuck player) {
		super(Sounds.HEARTBEAT.soundEvent, SoundCategory.AMBIENT);
		this.player = player;
	}

	@Override
	public void tick() {
		if(player.stylish_getMadness() <= 0) {
			this.setDone();
		}
	}

	@Override
	public float getVolume() {
		return player.stylish_getMadness() * 2.0f;
	}

	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}

	@Override
	public boolean canPlay() {
		return true;
	}

}
