package net.darktree.stylishoccult.gui.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class RuneRecipeComponent implements ICustomComponent {

	private transient AltarRitual ritual;
	private transient Identifier altar;
	private transient int x, y;

	private String recipe;
	private int radius;
	private float offset = 0;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		x = componentX;
		y = componentY;

		ritual = ResourceLoaders.ALTAR_RITUALS.find(new Identifier(recipe));
		altar = ModIdentifier.of("textures/misc/book/altar.png");
	}

	@Override
	public void render(MatrixStack matrices, IComponentRenderContext context, float ticks, int mouseX, int mouseY) {
		float step = MathHelper.TAU / ritual.ingredients.length;
		float angle = offset;

		for (Item item : ritual.ingredients) {
			matrices.push();

			float x = this.x + MathHelper.sin(angle) * radius;
			float y = this.y + MathHelper.cos(angle) * radius;
//			context.renderItemStack(matrices, (int) x, (int) y, mouseX, mouseY, new ItemStack(item));
			drawStack(context, matrices, false, (int) x, (int) y, mouseX, mouseY, item);

			matrices.pop();
			angle += step;
		}

		drawStack(context, matrices, true, this.x, this.y, mouseX, mouseY, ritual.catalyst);
//		context.renderItemStack(matrices, 0, 0, mouseX, mouseY, new ItemStack(ritual.catalyst));
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		recipe = lookup.apply(IVariable.wrap(recipe)).asString();
	}

	private void drawStack(IComponentRenderContext context, MatrixStack matrices, boolean center, int x, int y, int mx, int my, Item item) {
		if (center) {
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, altar);

			float scale = 0.4f;

			matrices.push();
			matrices.translate(x - (128*scale/2) + 8, y - (128*scale/2) + 8, 0);
			matrices.scale(scale, scale, scale);
			DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 128, 128, 128, 128);
			matrices.pop();
		}

		context.renderItemStack(matrices, x, y, mx, my, new ItemStack(item));
	}


}
