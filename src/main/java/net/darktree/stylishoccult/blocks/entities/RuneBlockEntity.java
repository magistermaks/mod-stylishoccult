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
    public NbtCompound writeNbt(NbtCompound tag) {
        if( script != null ) tag.put("state", script.toNbt());
        if( meta != null ) tag.put("meta", meta);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        try {
            if( nbt.contains("state") ) {
                script = RunicScript.fromNbt( nbt.getCompound("state") );
            }
            if( nbt.contains("meta") ) {
                meta = nbt.getCompound("meta");
            }
        } catch (Exception ignored) {}
        super.readNbt(nbt);
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
