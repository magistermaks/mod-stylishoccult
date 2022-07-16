package net.darktree.stylishoccult;

import mcp.mobius.waila.api.*;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.minecraft.block.BlockState;

public class WailaPlugin implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		registrar.addOverride(new LavaDemonOverride(), LavaDemonBlock.class);
	}

	static class LavaDemonOverride implements IBlockComponentProvider {

		@Override
		public BlockState getOverride(IBlockAccessor accessor, IPluginConfig config) {
			return accessor.getBlockState().get(LavaDemonBlock.MATERIAL).asBlock().getDefaultState();
		}

	}

}