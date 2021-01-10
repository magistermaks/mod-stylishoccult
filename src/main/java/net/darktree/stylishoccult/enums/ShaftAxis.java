package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public enum ShaftAxis implements StringIdentifiable {
    X("x"),
    Y("y"),
    Z("z");

    private final String name;

    ShaftAxis(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

    public static ShaftAxis fromName( String name ) {
        if( name.equals("x") ) return ShaftAxis.X;
        if( name.equals("y") ) return ShaftAxis.Y;
        if( name.equals("z") ) return ShaftAxis.Z;
        return null;
    }

    public ShaftAxis applyRotation(Direction d) {
        if( d == Direction.DOWN || d == Direction.UP ) return Y;
        if( d == Direction.NORTH || d == Direction.SOUTH ) return Z;
        if( d == Direction.WEST || d == Direction.EAST ) return X;
        throw new RuntimeException("Something's wrong with the universe!");
    }

    public BlockPos getRelativePos( BlockPos pos, int x, int y, int z ) {
        if( this == Y ) return pos.up( y ).south( z ).east( x );
        if( this == X ) return pos.up( -x ).south( z ).east( y );
        return pos.up( -z ).south( y ).east( x );
    }

    public ShaftAxis getRelativeAxis( ShaftAxis axis ) {
        if( this == Y ) return axis;
        switch( axis ) {
            case Y: return (this == X) ? Z : X;
            case X: return (this == X) ? Y : X;
            default: return (this == X) ? Z : Y;
        }
    }

    public Direction.Axis getMinecraft() {
        switch( this ) {
            case X: return Direction.Axis.X;
            case Y: return Direction.Axis.Y;
        }
        return Direction.Axis.Z;
    }
}
