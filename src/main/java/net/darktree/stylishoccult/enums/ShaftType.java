package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum ShaftType implements StringIdentifiable {
    P("positive"),
    N("negative"),
    L("long");

    private final String name;

    ShaftType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

    public ShaftType applyRotation(Direction d) {
        if( this == L ) return L;
        if( d == Direction.DOWN || d == Direction.NORTH || d == Direction.WEST  ) return N;
        if( d == Direction.UP || d == Direction.SOUTH || d == Direction.EAST ) return P;
        throw new RuntimeException("Something's wrong with the universe!");
    }

    public Direction.AxisDirection getMinecraft() {
        if( this == L ) throw new RuntimeException( "Unable to get AxisDirection of ShaftType.L!" );
        if( this == P ) return Direction.AxisDirection.POSITIVE;
        return Direction.AxisDirection.NEGATIVE;
    }
}

