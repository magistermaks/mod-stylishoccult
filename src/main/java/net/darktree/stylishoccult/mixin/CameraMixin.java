package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.overlay.SubmersionExtension;
import net.darktree.stylishoccult.tag.ModTags;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Inject(method="getSubmersionType", at=@At(value="HEAD"))
	private void stylish_getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> info) {
		SubmersionExtension.under_blood = false;
	}

	@Inject(method="getSubmersionType", at=@At(value="RETURN", ordinal=1), cancellable=true, locals=LocalCapture.CAPTURE_FAILHARD)
	private void stylish_getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> info, FluidState state) {
		if (state.isIn(ModTags.BLOOD)) {
			SubmersionExtension.under_blood = true;
			info.setReturnValue(CameraSubmersionType.LAVA);
		}
	}

}
