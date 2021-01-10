package net.darktree.stylishoccult.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;

public abstract class SimpleBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public SimpleBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    public void fromClientTag(CompoundTag tag) {
        fromTag( tag );
    }

    public void fromTag( BlockState state, CompoundTag tag ) {
        fromTag( tag );
    }

    public void fromTag( CompoundTag tag ) {
        super.fromTag( null, tag );
    }

    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag( tag );
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

    @Environment(EnvType.CLIENT)
    public void render( float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay ) {}
}
