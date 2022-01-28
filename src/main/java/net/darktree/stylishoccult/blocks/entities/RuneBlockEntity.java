package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class RuneBlockEntity extends SimpleBlockEntity {

    private RunicScript script;
    private NbtCompound meta;

    public RuneBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.RUNESTONE, pos, state);

        script = null;
        meta = null;
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        if( script != null ) tag.put( "state", script.toTag() );
        if( meta != null ) tag.put( "meta", meta );
        return super.toTag( tag );
    }

    @Override
    public void fromTag(NbtCompound tag) {
        try {
            if( tag.contains("state") ) {
                script = RunicScript.fromTag( tag.getCompound("state") );
            }
            if( tag.contains("meta") ) {
                meta = tag.getCompound("meta");
            }
        } catch (Exception ignored) {}
        super.fromTag(tag);
    }

    public void store( RunicScript script ) {
        this.script = script;
        markDirty();
    }

    public void clear() {
        script = null;
        markDirty();
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

    public boolean hasMeta() {
        return meta != null;
    }

    public NbtCompound getMeta() {
        return meta;
    }

    public void setMeta( NbtCompound tag ) {
        meta = tag;
        markDirty();
    }

}
