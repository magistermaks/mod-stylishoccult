package net.darktree.stylishoccult.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockUtils {

    public interface BlockStateComparator {
        boolean call(BlockState state);
    }

    public static int fastDistance(BlockPos pos1, BlockPos pos2){
        return Math.abs( pos1.getX() - pos2.getX() ) + Math.abs( pos1.getY() - pos2.getY() ) + Math.abs( pos1.getZ() - pos2.getZ() );
    }

    public static String posToString( BlockPos pos ) {
        if( pos == null ) return "null";
        return ( pos.getX() + " " + pos.getY() + " " + pos.getZ() );
    }

    public static <T extends BlockEntity, Y extends BlockView> T getEntity(Class<T> clazz, Y world, BlockPos pos){
        if( world != null && pos != null && clazz != null ) {
            BlockEntity entity = world.getBlockEntity(pos);
            if( clazz.isInstance( entity ) ) return clazz.cast( entity );
        }
        return null;
    }

    public static boolean areInLine( BlockPos pos1, BlockPos pos2 ) {
        int x = (pos1.getX() - pos2.getX() != 0) ? 1 : 0;
        int y = (pos1.getY() - pos2.getY() != 0) ? 1 : 0;
        int z = (pos1.getZ() - pos2.getZ() != 0) ? 1 : 0;

        return (x + y + z) == 1;
    }

    public static boolean touchesAir(BlockView world, BlockPos origin) {
        for( Direction direction : Direction.values() ){
            BlockState state = world.getBlockState( origin.offset( direction ) );
            if( state.isAir() ) return true;
        }
        return false;
    }

    public static BlockPos find(World world, BlockPos origin, Block block, int radius, BlockUtils.BlockStateComparator comp) {
        for( int x = -radius; x <= radius; x ++ ){
            for( int y = -radius; y <= radius; y ++ ){
                for( int z = -radius; z <= radius; z ++ ){

                    BlockPos pos = new BlockPos( x + origin.getX(), Math.max( y + origin.getY(), 0 ), z + origin.getZ());
                    BlockState state = world.getBlockState( pos );

                    if( state.getBlock() == block || (block == null) ) {
                        if( comp != null ) {
                            if( comp.call( state ) ){
                                return pos;
                            }
                        }else{
                            return pos;
                        }
                    }

                }
            }
        }

        return null;
    }

    public static BlockPos offsetPos( BlockPos pos, Direction.Axis axis, int distance ) {
        switch( axis ) {
            case X: return pos.east( distance );
            case Z: return pos.south( distance );
        }
        return pos.up( distance );
    }

    public static Direction getOffsetDirection( BlockPos origin, BlockPos target ) {
        if( target.getX() < origin.getX() )  return Direction.from(Direction.Axis.X, Direction.AxisDirection.NEGATIVE);
        else if ( target.getX() > origin.getX() ) return Direction.from(Direction.Axis.X, Direction.AxisDirection.POSITIVE);
        if( target.getZ() < origin.getZ() ) return Direction.from(Direction.Axis.Z, Direction.AxisDirection.NEGATIVE);
        else if ( target.getZ() > origin.getZ() ) return Direction.from(Direction.Axis.Z, Direction.AxisDirection.POSITIVE);
        if( target.getY() < origin.getY() ) return Direction.from( Direction.Axis.Y, Direction.AxisDirection.NEGATIVE );
        return Direction.from( Direction.Axis.Y, Direction.AxisDirection.POSITIVE );
    }

}
