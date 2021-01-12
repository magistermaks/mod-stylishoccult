package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.effects.ModEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Inject(at=@At(shift=At.Shift.AFTER, value="INVOKE", target="Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"), method="update(Lnet/minecraft/entity/player/PlayerEntity;)V", cancellable=true)
    public void update(PlayerEntity player, CallbackInfo ci) {
        if( player.hasStatusEffect(ModEffects.CORRUPTED_BLOOD) ) {
            ci.cancel();
        }
    }

}
