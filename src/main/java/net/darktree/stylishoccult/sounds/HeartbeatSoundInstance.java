package net.darktree.stylishoccult.sounds;

import net.darktree.stylishoccult.overlay.PlayerEntityMadnessDuck;
import net.minecraft.client.sound.MovingSoundInstance;

public class HeartbeatSoundInstance extends MovingSoundInstance {

	private final PlayerEntityMadnessDuck player;

	public HeartbeatSoundInstance(PlayerEntityMadnessDuck player) {
		super(Sounds.HEARTBEAT.event, Sounds.HEARTBEAT.category);
		this.player = player;
	}

	@Override
	public void tick() {
		if (player.stylish_getMadness() <= 0) {
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
