package net.darktree.stylishoccult.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import vazkii.patchouli.client.book.BookIcon;

// FIXME rewrite

@Pseudo
@Mixin(value=BookIcon.class, remap=false)
public abstract class BookIconMixin {

	@Unique
	private boolean stylish_occult_rune = false;

//	@Inject(method="from", at=@At("HEAD"), cancellable=true, require=0)
//	private static void stylish_from(String str, CallbackInfoReturnable<BookIcon> info) {
//		if (str.startsWith("stylish_occult_rune;") && str.endsWith(".png")) {
//			String image = str.split(";")[1];
//			BookIcon icon = new BookIcon(new Identifier(image));
//			((BookIconMixin) (Object) icon).stylish_occult_rune = true;
//			info.setReturnValue(icon);
//		}
//	}
//
//	@Redirect(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawableHelper;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V", remap=true), require=0)
//	private void stylish_drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
//		if (stylish_occult_rune) {
//			RenderSystem.setShaderColor(0.5f, 0.1f, 0, 1);
//			DrawableHelper.drawTexture(matrices, x, y, 16, 16, 1, 1, 13, 13, 16, 16);
//		} else {
//			DrawableHelper.drawTexture(matrices, x, y, u, v, width, height, textureWidth, textureHeight);
//		}
//	}

}
