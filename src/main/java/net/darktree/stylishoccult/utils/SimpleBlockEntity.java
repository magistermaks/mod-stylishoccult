package net.darktree.stylishoccult.utils;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class SimpleBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public SimpleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        fromTag(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return toTag(tag);
    }

    public void fromTag(NbtCompound tag) {
        super.readNbt(tag);
    }

    public NbtCompound toTag(NbtCompound tag) {
        return this.writeNbt(tag);
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
