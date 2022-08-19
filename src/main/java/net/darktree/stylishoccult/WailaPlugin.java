package net.darktree.stylishoccult;

import mcp.mobius.waila.api.*;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.OccultCauldronBlock;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntity;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.BlockState;

public class WailaPlugin implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		registrar.addOverride(new LavaDemonComponent(), LavaDemonBlock.class);
		registrar.addComponent(new OccultCauldronComponent(), TooltipPosition.BODY, OccultCauldronBlock.class);
	}

	static class LavaDemonComponent implements IBlockComponentProvider {

		@Override
		public BlockState getOverride(IBlockAccessor accessor, IPluginConfig config) {
			BlockState state = accessor.getBlockState();

			if (state.get(LavaDemonBlock.ANGER) > 0) {
				return state;
			} else {
				return state.get(LavaDemonBlock.MATERIAL).asBlock().getDefaultState();
			}
		}

	}

	static class OccultCauldronComponent implements IBlockComponentProvider {

		@Override
		public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
			if (accessor.getBlockEntity() instanceof OccultCauldronBlockEntity cauldron) {
				tooltip.add(Utils.tooltip("waila_cauldron_amount", cauldron.getStorage().getAmount()));
			}
		}

	}

}