package net.darktree.stylishoccult.parts;

import net.darktree.stylishoccult.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class CandleStateInfo {

    // Helpers to keep consistency
    final public static int ID_CENTER = 0;
    final public static int ID_NORTH = 1;
    final public static int ID_SOUTH = 2;
    final public static int ID_WEST = 3;
    final public static int ID_EAST = 4;

    final private float[] offset;
    final public int id;

    public short layers;
    public boolean lit;
    public VoxelShape outline;

    public CandleStateInfo( float[] offset, int id ) {
        this.layers = 0;
        this.lit = false;
        this.offset = offset;
        this.outline = VoxelShapes.empty();
        this.id = id;
    }

    public String getName() {
        return Integer.toString(id);
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("lit", this.lit);
        tag.putShort("layers", this.layers);
        return tag;
    }

    public void fromTag(CompoundTag tag) {
        this.lit = tag.getBoolean("lit");
        this.layers = tag.getShort("layers");
        updateOutlineBox();
    }

    public void updateOutlineBox() {
        if( this.layers == 0 ) {
            this.outline = VoxelShapes.cuboid(
                     6.75/16 + offset[0],
                    -1.00/16 + offset[1],
                     6.75/16 + offset[2],
                     9.25/16 + offset[0],
                     0.00/16 + offset[1],
                     9.25/16 + offset[2]
            );
        }else{
            this.outline = VoxelShapes.cuboid(
                     7.00/16 + offset[0],
                     0.00/16 + offset[1],
                     7.00/16 + offset[2],
                     9.00/16 + offset[0],
                     1.00/16 * layers + offset[1],
                     9.00/16 + offset[2]
            );
        }
    }

    public void randomUpdate() {
        if( this.lit && (this.layers > 1) ) {
            this.layers -= 1;
            if( this.layers < 2 ) {
                this.lit = false;
            }
            updateOutlineBox();
        }else{
            throw new RuntimeException( "CandleStateInfo invalid state!" );
        }
    }

    private ItemStack asItemStack() {
        if( layers > 1 ) {
            ItemStack itemStack = new ItemStack( lit ? ModItems.CANDLE : ModItems.EXTINGUISHED_CANDLE );

            CompoundTag tag = new CompoundTag();
            CompoundTag tag2 = new CompoundTag();
            tag2.putString( "layers", String.valueOf( layers ) );
            tag.put( "BlockStateTag", tag2 );
            itemStack.setTag(tag);

            return itemStack;
        }

        return ItemStack.EMPTY;
    }

    public void emitParticle( World world, BlockPos pos, ParticleEffect particleEffect, int server ) {
        double d = (double) pos.getX() + 0.5D + offset[0];
        double e = (double) pos.getY() + 0.15D + 0.059D * layers + offset[1];
        double f = (double) pos.getZ() + 0.5D + offset[2];

        if( server != 0 ) {
            ((ServerWorld) world).spawnParticles(particleEffect, d, e, f, server, 0, 0, 0, 0);
        }else{
            world.addParticle(particleEffect, d, e, f, 0, 0, 0);
        }
    }

    public ItemStack drop() {
        ItemStack itemStack = ItemStack.EMPTY;

        if( layers > 0 ) {
            if( this.layers > 1 ) {
                itemStack = this.asItemStack();
            }

            this.layers = 0;
            this.lit = false;
            updateOutlineBox();
        }

        return itemStack;
    }

    public boolean setOff() {
        this.lit = false;

        if( layers == 1 ) {
            this.layers = 0;
            updateOutlineBox();
            return true;
        }

        return false;
    }

    public boolean setOn() {
        if( lit ) {
            return false;
        }
        return (this.lit = true);
    }

    public boolean putCandle( short layers, boolean lit ) {
        if( this.layers <= 1 ) {
            this.layers = layers;
            this.lit = lit;
            updateOutlineBox();
            return true;
        }
        return false;
    }

    public float[] getOffset() {
        return offset;
    }
}
