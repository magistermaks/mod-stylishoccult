package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRuneBlock extends RuneBlock {

    public DebugRuneBlock( String name ) {
        super(RuneType.ACTOR, name);
    }

    @Override
    protected void onTriggered(RunicScript script, World world, BlockPos pos, BlockState state) {
        RuneBlockEntity entity = getEntity(world, pos);
        entity.setMeta( script.toNbt() );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity != null && entity.hasMeta() && !world.isClient ) {
            RunicScript script = RunicScript.fromNbt( entity.getMeta() );

            player.sendMessage( new LiteralText("Snapshot of previous rune activation:"), false );
            player.sendMessage( new LiteralText(" Local: " + script.value), false );
            player.sendMessage( new LiteralText(" Stack: (size " + script.getStack().size() + ")"), false );
            player.sendMessage( new LiteralText(script.getStack().print()), false );
        }

        return ActionResult.SUCCESS;
    }

}
