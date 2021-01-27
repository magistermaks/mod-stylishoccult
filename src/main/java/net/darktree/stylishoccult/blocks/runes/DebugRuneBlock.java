package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.DebugRuneBlockEntity;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DebugRuneBlock extends RuneBlock {

    public DebugRuneBlock( String name ) {
        super(RuneType.TRANSFER, name);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( player.getStackInHand(hand).isEmpty() ) {
            DebugRuneBlockEntity entity = BlockUtils.getEntity(DebugRuneBlockEntity.class, world, pos);
            if( entity != null && entity.scriptCopy != null && !world.isClient ) {
                player.sendMessage( new LiteralText("Snapshot of previous rune activation:"), false );
                player.sendMessage( new LiteralText(" Local: " + entity.scriptCopy.value), false );
                player.sendMessage( new LiteralText(" Stack: (size " + entity.scriptCopy.getStack().size() + ")"), false );
                player.sendMessage( new LiteralText(entity.scriptCopy.getStack().print()), false );
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DebugRuneBlockEntity();
    }

}
