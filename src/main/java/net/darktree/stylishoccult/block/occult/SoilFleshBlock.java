package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoilFleshBlock extends FullFleshBlock implements ImpureBlock {

	public SoilFleshBlock() {
		this(RegUtil.settings( Material.ORGANIC_PRODUCT, Sounds.FLESH, 0.8F, 0.8F, true ).slipperiness(0.7f));
	}

	public SoilFleshBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public int impurityLevel(BlockState state) {
		return 32;
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.GENERIC_FLESH;
	}
}
