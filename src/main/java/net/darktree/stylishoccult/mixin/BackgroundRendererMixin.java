package net.darktree.stylishoccult.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darktree.stylishoccult.overlay.SubmersionExtension;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@Shadow private static float red;
	@Shadow private static float blue;
	@Shadow private static float green;

	@Inject(method="render", at=@At(value="FIELD", opcode=Opcodes.PUTSTATIC, target="Lnet/minecraft/client/render/BackgroundRenderer;blue:F", ordinal=1))
	private static void stylish_render(Camera camera, float tickDelta, ClientWorld world, int i, float f, CallbackInfo ci) {
		if (SubmersionExtension.under_blood) {
			red = 0.3f;
			green = 0.0f;
			blue = 0.0f;
		}
	}

	@Inject(method="applyFog", at=@At(value="INVOKE", remap=false, target="Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V", ordinal=1, shift=At.Shift.AFTER))
	private static void stylish_applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
		if (SubmersionExtension.under_blood) {
			RenderSystem.setShaderFogStart(1);
			RenderSystem.setShaderFogEnd(4);
		}
	}

}
