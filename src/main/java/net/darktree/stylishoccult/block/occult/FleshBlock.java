package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FleshBlock extends FullFleshBlock implements ImpureBlock {

	public FleshBlock() {
		super( RegUtil.settings( Material.ORGANIC_PRODUCT, Sounds.FLESH, 0.8F, 0.8F, true ).slipperiness(0.8f).ticksRandomly() );
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		OccultHelper.corruptAround(world, pos, random, true);

		if( random.nextInt(256) == 0 && FossilizedFleshBlock.isPosValid(world, pos) && (BlockUtils.countInArea(world, pos, FossilizedFleshBlock.class, 4) < 3) ) {
			world.setBlockState(pos, ModBlocks.BONE_FLESH.getDefaultState());
		}
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public int impurityLevel(BlockState state) {
		return 30;
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.GENERIC_FLESH;
	}
}
