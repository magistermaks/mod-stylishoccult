package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.parts.ChandelierStateInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class ChandelierBlockEntity extends AbstractCandleHolderBlockEntity<ChandelierStateInfo> implements Tickable {

    public ChandelierBlockEntity() {
        super( BlockEntities.CHANDELIER);
        state = new ChandelierStateInfo();
        state.fromTag( state.toTag() );
    }

    @Override
    public void render(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        state.render(matrices, vertexConsumers, light, overlay);
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("candles", state.toTag() );

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        if( tag.contains("candles") ){
            state.fromTag( tag.getCompound("candles") );
        }else{
            this.markDirty();
        }

    }

    @Override
    public void tick() {
        if( world == null ) return;

        super.tick();
    }


}
