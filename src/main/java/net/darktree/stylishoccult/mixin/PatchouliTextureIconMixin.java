package net.darktree.stylishoccult.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darktree.stylishoccult.duck.PatchouliTextureIconDuck;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.book.BookIcon;

@Pseudo
@Mixin(value=BookIcon.TextureIcon.class, remap=false)
public abstract class PatchouliTextureIconMixin implements PatchouliTextureIconDuck {

	@Shadow
	public abstract Identifier texture();

	@Unique
	private boolean stylish_rune = false;

	@Override
	public void stylish_markOccultRune() {
		stylish_rune = true;
	}

	@Inject(method="render", at=@At("HEAD"), cancellable=true, require=0)
	private void stylish_drawTexture(MatrixStack matrices, int x, int y, CallbackInfo info) {
		if (stylish_rune) {
			RenderSystem.setShaderColor(0.5f, 0.1f, 0, 1);
			RenderSystem.setShaderTexture(0, this.texture());
			DrawableHelper.drawTexture(matrices, x, y, 16, 16, 1, 1, 13, 13, 16, 16);
			info.cancel();
		}
	}

}
