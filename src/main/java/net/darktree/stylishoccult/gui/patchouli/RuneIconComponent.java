package net.darktree.stylishoccult.gui.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class RuneIconComponent implements ICustomComponent {

	private transient Identifier id;
	private transient int x, y;

	private float scale = 4;
	private String image;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		x = componentX;
		y = componentY;

		id = new Identifier(image);
	}

	@Override
	public void render(MatrixStack matrices, IComponentRenderContext context, float ticks, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, id);

		matrices.push();
		matrices.translate(x, y, 0);

		matrices.push();
		matrices.scale(3f, 3f, 3f);
		RenderSystem.enableBlend();
		matrices.translate(0.5, 0.5, 0);
		drawRuneIcon(matrices,0, 0, 0, 0.8f, 0, 0);
		matrices.pop();

		matrices.push();
		matrices.scale(3, 3, 3);
		drawRuneIcon(matrices,0.5f, 0.2f, 0.0f, 1, 0, 0);
		matrices.pop();
		matrices.pop();
	}

	private void drawRuneIcon(MatrixStack matrices, float r, float g, float b, float a, int xo, int yo) {
		RenderSystem.setShaderColor(r, g, b, a);
		DrawableHelper.drawTexture(matrices, xo, yo, 16, 16, 1, 1, 13, 13, 16, 16);
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		image = lookup.apply(IVariable.wrap(image)).asString();
	}

}
