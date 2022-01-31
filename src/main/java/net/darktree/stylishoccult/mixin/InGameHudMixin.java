package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.overlay.OverlayManager;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	protected abstract void renderOverlay(Identifier texture, float opacity);

	@Inject(method="render", at=@At(value="INVOKE", shift=At.Shift.BEFORE, target="Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
	public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci){
		Identifier texture = OverlayManager.getTexture();

		if(texture != null) {
			this.renderOverlay(texture, OverlayManager.getIntensity());
		}
	}

}
