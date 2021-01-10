package net.darktree.stylishoccult.parts;

import net.darktree.stylishoccult.items.CandleItem;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.utils.Ray;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCandleHolder {

    public interface VoidFunction {
        void call( CandleStateInfo candle );
    }

    private VoxelShape outline = VoxelShapes.empty();
    private boolean lit = false;

    // Candle holder specific methods
    public abstract void forEach( VoidFunction lambda );
    public abstract VoxelShape getBaseShape();

    @Environment(EnvType.CLIENT)
    public void render( MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay ) {
        forEach( candle -> CandleRenderer.render( candle, matrices, vertexConsumers, light, overlay ) );
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        forEach( candle -> tag.put( candle.getName(), candle.toTag() ));
        return tag;
    }

    public void fromTag( CompoundTag tag ) {
        forEach( candle -> candle.fromTag( tag.getCompound( candle.getName() ) ));
        updateState();
        updateOutlineBox();
    }

    public boolean randomUpdate(Random random) {
        ArrayList<CandleStateInfo> list = getUpdatable();

        if( list.isEmpty() ) {
            return false;
        }

        list.get( random.nextInt( list.size() ) ).randomUpdate();

        updateState();
        updateOutlineBox();
        return true;
    }

    protected void updateOutlineBox() {
        BooleanBiFunction func = BooleanBiFunction.OR;
        final VoxelShape[] shape = {VoxelShapes.empty()};

        // compute container voxel shape
        forEach( candle -> shape[0] = VoxelShapes.combine(shape[0], candle.outline, func));
        shape[0] = VoxelShapes.combine(shape[0], getBaseShape(), func);

        this.outline = shape[0];
    }

    protected void updateState() {
        lit = !getUpdatable().isEmpty();
    }

    public ArrayList<CandleStateInfo> getUpdatable() {
        ArrayList<CandleStateInfo> list = new ArrayList<>();

        forEach( candle -> {
            if( candle.lit ) list.add( candle );
        } );

        return list;
    }

    public CandleStateInfo get(int id) {
        AtomicReference<CandleStateInfo> result = new AtomicReference<>(null);

        forEach( candle -> {
            if( candle.id == id ) result.set(candle);
        } );

        return result.get();
    }

    public ArrayList<ItemStack> dropAll() {
        ArrayList<ItemStack> list = new ArrayList<>();

        forEach( info -> list.add( info.drop() ) );

        updateState();
        updateOutlineBox();

        return list;
    }

    public ItemStack drop( CandleStateInfo candle ) {
        ItemStack stack = candle.drop();
        updateState();
        updateOutlineBox();

        return stack;
    }

    public void extinguish( CandleStateInfo candle ) {
        if( candle.setOff() ){
            updateOutlineBox();
        }
        updateState();
    }

    public void ignite( CandleStateInfo candle ) {
        if( candle.setOn() ) {
            updateState();
        }
    }

    public void put( CandleStateInfo candle, ItemStack itemStack, boolean creative ) {
        try {
            short l = CandleItem.getLayers( itemStack );
            l = l > 8 ? 8 : l;

            if( l >= 1 && candle.putCandle(l, itemStack.getItem() == ModItems.CANDLE) ) {
                if( !creative ) itemStack.decrement(1);
                updateState();
                updateOutlineBox();
            }
        }catch ( Exception ignored ) {}
    }

    public CandleStateInfo rayTrace(BlockPos pos, PlayerEntity player, int layersMin, int layersMax ) {
        Ray ray = new Ray( player.getCameraPosVec(1.0F), player.getRotationVec(1.0F) );
        ray.offset( pos );

        final double[] distance = {Double.MAX_VALUE};
        final CandleStateInfo[] closestCandle = {null};

        forEach( candle -> {
            if( candle.layers >= layersMin && candle.layers <= layersMax ) {
                Box box = candle.outline.getBoundingBox();
                if( ray.intersects( box ) ) {
                    double distance2 = box.getCenter().distanceTo( ray.origin );
                    if( distance2 < distance[0]) {
                        distance[0] = distance2;
                        closestCandle[0] = candle;
                    }
                }
            }
        } );

        return closestCandle[0];
    }

    public void emitParticles( BlockPos pos, World world, ParticleEffect particleEffect ) {
        forEach( candle -> {
            if( candle.lit && candle.layers > 1 ) {
                candle.emitParticle( world, pos, particleEffect, 0 );
            }
        } );
    }

    public VoxelShape getOutline() {
        return outline;
    }

    public boolean isLit() {
        return lit;
    }
}
