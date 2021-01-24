package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.components.Rune;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClickRuneBlock extends EntryRuneBlock {

    public ClickRuneBlock(Rune rune) {
        super(rune);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        emit( world, pos );
        world.playSound( null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1, 1 );
        return ActionResult.SUCCESS;
    }

}
