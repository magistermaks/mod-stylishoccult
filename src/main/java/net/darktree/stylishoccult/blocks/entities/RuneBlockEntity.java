package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class RuneBlockEntity extends SimpleBlockEntity {

    private Script script;
    private NbtCompound meta;

    public RuneBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.RUNESTONE, pos, state);

        script = null;
        meta = null;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if(script != null) tag.put("s", script.writeNbt(new NbtCompound()));
        if(meta != null) tag.put("m", meta);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        try {
            if( nbt.contains("s") ) script = Script.fromNbt( nbt.getCompound("s") );
            if( nbt.contains("m") ) meta = nbt.getCompound("m");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        super.readNbt(nbt);
    }

    public void store(Script script) {
        this.script = script;
        markDirty();
    }

    public void clear() {
        script = null;
        markDirty();
    }

    public Script getScript() {
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
