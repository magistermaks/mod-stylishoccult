package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRuneBlock extends RuneBlock {

    // TODO: redo this rune

    public DebugRuneBlock( String name ) {
        super(RuneType.ACTOR, name);
    }

    @Override
    public void apply(Script script, World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);
        entity.setMeta( script.writeNbt(new NbtCompound()) );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity != null && entity.hasMeta() && !world.isClient ) {
            Script script = Script.fromNbt(entity.getMeta());

            player.sendMessage( new LiteralText("Snapshot of previous rune activation:"), false );
            player.sendMessage( new LiteralText(" - Main Stack: (size " + script.stack.size() + ")"), false );

            script.stack.reset(element -> {
                player.sendMessage( new LiteralText("   " + element.toString()), false );
            });

            player.sendMessage( new LiteralText(" - Drop Stack:"), false );

            script.ring.reset(element -> {
                player.sendMessage( new LiteralText("   " + (element == null ? "null" : element.toString())), false );
            });
        }

        return ActionResult.SUCCESS;
    }

}
