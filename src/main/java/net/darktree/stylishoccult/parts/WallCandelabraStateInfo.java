package net.darktree.stylishoccult.parts;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class WallCandelabraStateInfo extends AbstractCandleHolder {

    private static final float HEIGHT = (1.0F/16.0F) * 6;
    private static final float DISTANCE = 0.312F;
    public final VoxelShape BASE;

    public static final float[] POINT_SOUTH = { 0, HEIGHT, DISTANCE };
    public static final float[] POINT_NORTH = { 0, HEIGHT, -DISTANCE };
    public static final float[] POINT_EAST = { DISTANCE, HEIGHT, 0 };
    public static final float[] POINT_WEST = { -DISTANCE, HEIGHT, 0 };

    public CandleStateInfo candle;

    public boolean render = true;

    public WallCandelabraStateInfo( float[] point ) {
        this.candle = new CandleStateInfo( point, CandleStateInfo.ID_CENTER );

        if (point == POINT_SOUTH) {
            BASE = Block.createCuboidShape(6.5, 1, 15.5, 9.5, 4, 16); return;
        } else if (point == POINT_NORTH) {
            BASE = Block.createCuboidShape(6.5, 1, 0.5, 9.5, 4, 0); return;
        } else if (point == POINT_EAST) {
            BASE = Block.createCuboidShape(15.5, 1, 6.5, 16, 4, 9.5); return;
        } else if (point == POINT_WEST) {
            BASE = Block.createCuboidShape(0, 1, 6.5, 0.5, 4, 9.5); return;
        }else{
            BASE = VoxelShapes.empty();
        }

        throw new RuntimeException( "Invalid point" );
    }

    public static WallCandelabraStateInfo getByType( Direction direction ) {
        switch ( direction ) {
            case NORTH: return new WallCandelabraStateInfo( POINT_NORTH );
            case SOUTH: return new WallCandelabraStateInfo( POINT_SOUTH );
            case WEST: return new WallCandelabraStateInfo( POINT_WEST );
            case EAST: return new WallCandelabraStateInfo( POINT_EAST );
        }

        throw new RuntimeException( "Invalid Wall Candelabra rotation!" );
    }

    @Override
    public void forEach(VoidFunction lambda) {
        if( candle != null ) lambda.call( candle );
    }

    @Override
    public VoxelShape getBaseShape() {
        return BASE;
    }

}
