package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.overlay.OverlayManager;
import net.darktree.stylishoccult.overlay.PlayerEntityClientDuck;
import net.darktree.stylishoccult.overlay.PlayerEntityDuck;
import net.darktree.stylishoccult.sounds.HeartbeatSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityClientMixin implements PlayerEntityClientDuck {

	@Unique
	@Environment(EnvType.CLIENT)
	SoundInstance heartbeat;

	@Inject(method="tickMovement", at=@At("HEAD"))
	public void stylish_tickMovement(CallbackInfo ci) {
		float value = MathHelper.sin(((PlayerEntityDuck) this).stylish_getMadness() * MathHelper.HALF_PI);
		OverlayManager.show(OverlayManager.MADNESS, value);
	}

	@Override
	public void stylish_startHeartbeatSound() {
		if(((PlayerEntity) (Object) this).world.isClient) {
			if(heartbeat == null || !MinecraftClient.getInstance().getSoundManager().isPlaying(heartbeat)) {
				heartbeat = new HeartbeatSoundInstance((PlayerEntityDuck) this);
				MinecraftClient.getInstance().getSoundManager().play(heartbeat);
			}
		}
	}

}
