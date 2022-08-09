package net.darktree.stylishoccult.item.client;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronFluidRenderer;
import net.darktree.stylishoccult.block.fluid.ModFluids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

@Environment(EnvType.CLIENT)
public final class OccultCauldronItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

	private final BlockState cauldron;

	public OccultCauldronItemRenderer() {
		cauldron = ModBlocks.OCCULT_CAULDRON.getDefaultState();
	}

	@Override
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(cauldron, matrices, vertexConsumers, light, overlay);

		NbtCompound nbt = stack.getNbt();
		if (nbt != null && nbt.contains("amount", NbtElement.INT_TYPE)) {
			OccultCauldronFluidRenderer.drawCauldronFluidQuad(matrices, vertexConsumers, ModFluids.BLOOD_VARIANT, nbt.getInt("amount"), light);
		}
	}

}
