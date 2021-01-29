package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class RuneBlockEntity extends SimpleBlockEntity {

    private RunicScript script;
    private CompoundTag meta;

    public RuneBlockEntity() {
        this(BlockEntities.RUNESTONE);

        script = null;
        meta = null;
    }

    public RuneBlockEntity(BlockEntityType entity) {
        super(entity);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if( script != null ) tag.put( "state", script.toTag() );
        if( meta != null ) tag.put( "meta", meta );
        return super.toTag( tag );
    }

    @Override
    public void fromTag(CompoundTag tag) {
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

    public CompoundTag getMeta() {
        return meta;
    }

    public void setMeta( CompoundTag tag ) {
        meta = tag;
    }

}
