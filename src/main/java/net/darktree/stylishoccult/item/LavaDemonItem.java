package net.darktree.stylishoccult.item;

import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LavaDemonItem extends BlockItem {

	LavaDemonPart part;

	public LavaDemonItem(LavaDemonPart part, ItemGroup group) {
		super(ModBlocks.LAVA_DEMON, new Item.Settings().group(group));
		this.part = part;
	}

	@Override
	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = ((LavaDemonBlock) getBlock()).getPartPlacementState(this.part, context);
		return state != null && this.canPlace(context, state) ? state : null;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(new ItemStack(this));
		}
	}

	@Override
	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Utils.tooltip("lava_demon_item"));
	}
}
