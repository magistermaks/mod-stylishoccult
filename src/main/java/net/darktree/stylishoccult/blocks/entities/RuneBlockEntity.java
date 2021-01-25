package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.Rune;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RuneBlockEntity extends SimpleBlockEntity {

    private RunicScript script;

    public RuneBlockEntity() {
        super(BlockEntities.RUNESTONE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if( script != null ) tag.put( "state", script.toTag() );
        return super.toTag( tag );
    }

    @Override
    public void fromTag(CompoundTag tag) {
        try {
            script = RunicScript.fromTag( tag.getCompound("state") );
        } catch (Exception ignored) {
            script = null;
        }
        super.fromTag(tag);
    }

    public void store( RunicScript script ) {
        this.script = script;
        markDirty();
    }

    public void execute( Rune rune ) {
        if( script != null ) script.apply( rune, this.world, this.pos );
    }

    public void clear() {
        script = null;
        markDirty();
    }

    public Direction[] directions( World world, BlockPos pos, Rune rune ) {
        return script != null ? rune.directions(world, pos, script) : new Direction[] {};
    }

    public RunicScript copyScript(Direction direction ) {
        return script.copyFor( direction );
    }

    public RunicScript getScript() {
        return script;
    }

    public boolean hasScript() {
        return script != null;
    }

}
