package net.darktree.stylishoccult.parts;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;

public class ChandelierStateInfo extends AbstractCandleHolder {

    static final float HEIGHT = (1.0F/16.0F) * 4;
    static final float DISTANCE = 0.4057F;
    static final VoxelShape BASE = Block.createCuboidShape( 6.5, 15, 6.5, 9.5, 16, 9.5 );

    public static final float[] POINT_SOUTH = { 0, HEIGHT, DISTANCE };
    public static final float[] POINT_NORTH = { 0, HEIGHT, -DISTANCE };
    public static final float[] POINT_EAST = { DISTANCE, HEIGHT, 0 };
    public static final float[] POINT_WEST = { -DISTANCE, HEIGHT, 0 };

    public CandleStateInfo north;
    public CandleStateInfo south;
    public CandleStateInfo west;
    public CandleStateInfo east;

    public ChandelierStateInfo() {
        this.north = new CandleStateInfo( POINT_NORTH, CandleStateInfo.ID_NORTH );
        this.south = new CandleStateInfo( POINT_SOUTH, CandleStateInfo.ID_SOUTH );
        this.west = new CandleStateInfo( POINT_WEST, CandleStateInfo.ID_WEST );
        this.east = new CandleStateInfo( POINT_EAST, CandleStateInfo.ID_EAST );
    }

    @Override
    public void forEach(AbstractCandleHolder.VoidFunction lambda) {
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
