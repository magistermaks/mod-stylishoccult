package net.darktree.stylishoccult.utils;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public abstract class SimpleBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public SimpleBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag( tag );
    }

    @Override
    public void fromTag( BlockState state, CompoundTag tag ) {
        fromTag( tag );
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag( tag );
    }

    public void fromTag( CompoundTag tag ) {
        super.fromTag( null, tag );
    }

    public void safeSync() {
        if( isServer() ) {
            sync();
        }
    }

    public void update() {
        this.markDirty();
        this.safeSync();
    }

    public boolean isClient() {
        return ( this.world != null && world.isClient );
    }

    public boolean isServer() {
        return ( this.world != null && !world.isClient );
    }

}
