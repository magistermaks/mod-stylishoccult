package net.darktree.stylishoccult.parts;

import net.darktree.stylishoccult.enums.CandelabraType;
import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;

public class CandelabraStateInfo extends AbstractCandleHolder {

    static final float HEIGHT = (1.0F/16.0F) * 7;
    static final float DISTANCE = 0.25F;
    static final VoxelShape BASE = Block.createCuboidShape( 6, 0, 6, 10, 7, 10 );

    public static final float[] POINT_CENTER = { 0, HEIGHT, 0 };
    public static final float[] POINT_SOUTH = { 0, HEIGHT, DISTANCE };
    public static final float[] POINT_NORTH = { 0, HEIGHT, -DISTANCE };
    public static final float[] POINT_EAST = { DISTANCE, HEIGHT, 0 };
    public static final float[] POINT_WEST = { -DISTANCE, HEIGHT, 0 };

    public CandleStateInfo center;
    public CandleStateInfo north;
    public CandleStateInfo south;
    public CandleStateInfo west;
    public CandleStateInfo east;

    public boolean render = true;

    public static CandelabraStateInfo getByType( CandelabraType type ) {
        switch( type ) {
            case ONE: return new CandelabraStateInfo( true, false, false, false, false );
            case TWO_NORTH_SOUTH: return new CandelabraStateInfo( false, true, true, false, false );
            case TWO_WEST_EAST: return new CandelabraStateInfo( false, false, false, true, true );
            case THREE_NORTH_SOUTH: return new CandelabraStateInfo( true, true, true, false, false );
            case THREE_WEST_EAST: return new CandelabraStateInfo( true, false, false, true, true );
            case FOUR: return new CandelabraStateInfo( false, true, true, true, true );
        }

        return new CandelabraStateInfo( true, true, true, true, true );
    }

    public CandelabraStateInfo( boolean c, boolean n, boolean s, boolean w, boolean e ) {
        this.center = c ? new CandleStateInfo( POINT_CENTER, CandleStateInfo.ID_CENTER ) : null;
        this.north = n ? new CandleStateInfo( POINT_NORTH, CandleStateInfo.ID_NORTH ) : null;
        this.south = s ? new CandleStateInfo( POINT_SOUTH, CandleStateInfo.ID_SOUTH ) : null;
        this.west = w ? new CandleStateInfo( POINT_WEST, CandleStateInfo.ID_WEST ) : null;
        this.east = e ? new CandleStateInfo( POINT_EAST, CandleStateInfo.ID_EAST ) : null;
    }

    @Override
    public void forEach(AbstractCandleHolder.VoidFunction lambda) {
        if( center != null ) lambda.call( center );
        if( north != null ) lambda.call( north );
        if( south != null ) lambda.call( south );
        if( west != null ) lambda.call( west );
        if( east != null ) lambda.call( east );
    }

    @Override
    public VoxelShape getBaseShape() {
        return BASE;
    }

}
