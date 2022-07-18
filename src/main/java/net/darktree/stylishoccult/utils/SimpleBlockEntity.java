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
        readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }

    public void safeSync() {
        if (world != null && !world.isClient) {
            sync();
        }
    }

    public void update() {
        this.markDirty();
        this.safeSync();
    }

}
