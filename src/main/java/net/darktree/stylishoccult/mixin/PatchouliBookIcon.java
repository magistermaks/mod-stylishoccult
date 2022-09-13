package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.duck.PatchouliTextureIconDuck;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookIcon;

@Pseudo
@Mixin(value=BookIcon.class, remap=false)
public interface PatchouliBookIcon {

	@Inject(method="from", at=@At("HEAD"), cancellable=true, require=0)
	private static void stylish_from(String str, CallbackInfoReturnable<BookIcon> info) {
		if (str.startsWith("stylish_occult_rune;") && str.endsWith(".png")) {
			String image = str.split(";")[1];
			BookIcon icon = new BookIcon.TextureIcon(new Identifier(image));
			((PatchouliTextureIconDuck) (Object) icon).stylish_markOccultRune();
			info.setReturnValue(icon);
		}
	}

}
