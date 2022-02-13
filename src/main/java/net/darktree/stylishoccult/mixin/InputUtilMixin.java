package net.darktree.stylishoccult.mixin;

import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InputUtil.class)
public class InputUtilMixin {

//	@Redirect(method="setCursorParameters", at=@At(value="INVOKE", target="Lorg/lwjgl/glfw/GLFW;glfwSetInputMode(JII)V"))
//	private static void glfwSetInputModeRedirect(long window, int mode, int value) {
//		if (value != InputUtil.GLFW_CURSOR_DISABLED) {
//			GLFW.glfwSetInputMode(window, 208897, value);
//		}
//	}

}
