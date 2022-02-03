package net.darktree.stylishoccult.blocks.runes.flow;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.blocks.runes.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.script.engine.Stack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class JoinRuneBlock extends DirectionalRuneBlock {

    public JoinRuneBlock(String name) {
        super(name);
    }

    @Override
    public void apply(Script script, World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity.hasMeta() ) {
            Stack stack = new Stack(32);
            stack.readNbt(entity.getMeta());
            stack.reset(script.stack::push);
            entity.setMeta(null);
        }else{
            entity.setMeta(script.stack.writeNbt(new NbtCompound()));
            script.stack.reset(element -> {});
        }
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        if( !getEntity(world, pos).hasMeta() ) {
            return new Direction[] { getFacing(world, pos) };
        }

        return new Direction[] {};
    }
}
