package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.WallCandelabraBlock;
import net.darktree.stylishoccult.parts.WallCandelabraStateInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class WallCandelabraBlockEntity extends AbstractCandleHolderBlockEntity<WallCandelabraStateInfo> implements Tickable {

    private boolean isLoaded = false;

    public WallCandelabraBlockEntity() {
        super( BlockEntities.WALL_CANDELABRA);
        state = new WallCandelabraStateInfo( WallCandelabraStateInfo.POINT_NORTH );
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
        if( world == null ) return;

        if( !isLoaded ) {
            CompoundTag tag = this.state.toTag();

            this.state = WallCandelabraStateInfo.getByType( world.getBlockState( pos ).get( WallCandelabraBlock.HORIZONTAL_FACING ) );
            this.state.fromTag( tag );

            isLoaded = true;
        }

        super.tick();
    }
}
