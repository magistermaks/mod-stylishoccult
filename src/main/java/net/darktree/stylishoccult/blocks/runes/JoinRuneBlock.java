package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class JoinRuneBlock extends DirectionalRuneBlock {

    public JoinRuneBlock(String name) {
        super(name);
    }

    @Override
    public void apply(RunicScript script, World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);
        Direction facing = getFacing(world, pos);

        if( entity != null ) {
            if( facing == script.getDirection() ) {
                if( entity.hasMeta() ) {
                    RunicScript storedScript = RunicScript.fromTag( entity.getMeta() );
                    script.combine( storedScript );
                }
            }else{
                entity.setMeta( script.toTag() );
            }
        }
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, RunicScript script) {
        if( script.getDirection() != getFacing(world, pos) ) {
            return new Direction[] {};
        }

        return super.getDirections(world, pos, script);
    }
}
