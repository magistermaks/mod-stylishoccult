package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LeavesFleshBlock extends LeavesBlock implements ImpureBlock, FoliageFleshBlock {

	public LeavesFleshBlock() {
		super(AbstractBlock.Settings.of(Material.LEAVES)
				.strength(0.2F)
				.slipperiness(0.8f)
				.sounds(Sounds.FLESH)
				.nonOpaque()
				.allowsSpawning((a, b, c, d) -> false)
				.suffocates((a, b, c) -> false)
				.blockVision((a, b, c) -> false)
		);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void cleanse(World world, BlockPos pos, BlockState state) {
		OccultHelper.cleanseFlesh(world, pos, state);
	}

	@Override
	public int impurityLevel(BlockState state) {
		return 20;
	}

	public static BlockState getStateToFit(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		final int distance = (state.getBlock() instanceof LeavesBlock) && state.contains(DISTANCE) ? state.get(DISTANCE) : 7;
		final boolean persistent = state.contains(PERSISTENT) ? state.get(PERSISTENT) : false;
		return ModBlocks.LEAVES_FLESH.getDefaultState().with(DISTANCE, distance).with(PERSISTENT, persistent);
	}

}
