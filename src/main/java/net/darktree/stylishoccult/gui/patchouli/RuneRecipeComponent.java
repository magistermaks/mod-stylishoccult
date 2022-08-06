package net.darktree.stylishoccult.gui.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public class RuneRecipeComponent implements ICustomComponent {

	private transient Identifier altar;
	private transient ItemStack catalyst;
	private transient ItemStack[] ingredients;
	private transient int x, y;

	private String recipe;
	private int radius;
	private float offset = 0;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		x = componentX;
		y = componentY;

		AltarRitual ritual = ResourceLoaders.ALTAR_RITUALS.find(new Identifier(recipe));
		altar = ModIdentifier.of("textures/misc/book/altar.png");
		catalyst = new ItemStack(ritual.catalyst);
		ingredients = Arrays.stream(ritual.ingredients).map(ItemStack::new).toArray(ItemStack[]::new);
	}

	@Override
	public void render(MatrixStack matrices, IComponentRenderContext context, float ticks, int mouseX, int mouseY) {
		float step = MathHelper.TAU / ingredients.length;
		float angle = offset;
		float scale = 0.4f;

		for (ItemStack stack : ingredients) {
			matrices.push();

			float x = this.x + MathHelper.sin(angle) * radius;
			float y = this.y + MathHelper.cos(angle) * radius;
			context.renderItemStack(matrices, (int) x, (int) y, mouseX, mouseY, stack);

			matrices.pop();
			angle += step;
		}

		// draw altar circle
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, altar);

		matrices.push();
		matrices.translate(x - (128*scale/2) + 8, y - (128*scale/2) + 8, 0);
		matrices.scale(scale, scale, scale);
		DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 128, 128, 128, 128);
		matrices.pop();

		context.renderItemStack(matrices, x, y, mouseX, mouseY, catalyst);
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		recipe = lookup.apply(IVariable.wrap(recipe)).asString();
	}

}
