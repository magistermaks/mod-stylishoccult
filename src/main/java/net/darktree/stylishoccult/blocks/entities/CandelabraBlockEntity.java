package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.CandelabraBlock;
import net.darktree.stylishoccult.parts.CandelabraStateInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class CandelabraBlockEntity extends AbstractCandleHolderBlockEntity<CandelabraStateInfo> implements Tickable {

    private boolean isLoaded = false;

    public CandelabraBlockEntity() {
        super(BlockEntities.CANDELABRA);
        state = new CandelabraStateInfo( true, true, true, true, true );
        state.render = false;
    }

    @Override
    public void render(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if( state.render ) {
            state.render(matrices, vertexConsumers, light, overlay);
        }
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
        if( this.world == null ) return;

        if( !isLoaded ) {
            CompoundTag tag = this.state.toTag();

            this.state = CandelabraStateInfo.getByType( world.getBlockState( pos ).get( CandelabraBlock.TYPE ) );
            this.state.fromTag( tag );

            isLoaded = true;
        }

        super.tick();
    }
}