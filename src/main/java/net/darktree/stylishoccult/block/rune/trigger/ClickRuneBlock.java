package net.darktree.stylishoccult.block.rune.trigger;

import net.darktree.stylishoccult.block.rune.EntryRuneBlock;
import net.darktree.stylishoccult.sounds.Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClickRuneBlock extends EntryRuneBlock {

	public ClickRuneBlock(String name) {
		super(name);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		emit(world, pos, player);
		Sounds.ACTIVATE.play(world, pos);
		return ActionResult.SUCCESS;
	}

}
