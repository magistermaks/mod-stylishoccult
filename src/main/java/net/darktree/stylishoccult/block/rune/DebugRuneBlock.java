package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
    public void apply(Script script, World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);
        entity.setMeta( script.writeNbt(new NbtCompound()) );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity != null && entity.hasMeta() && !world.isClient ) {
            Network.DEBUG.send(player, pos, entity.getMeta());
        }

        return ActionResult.SUCCESS;
    }

}
