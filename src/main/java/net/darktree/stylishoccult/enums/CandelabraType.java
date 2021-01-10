package net.darktree.stylishoccult.enums;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum CandelabraType implements StringIdentifiable {
    ONE("one"),
    TWO_NORTH_SOUTH("two_north_south"),
    TWO_WEST_EAST("two_west_east"),
    THREE_NORTH_SOUTH("three_north_south"),
    THREE_WEST_EAST("three_west_east"),
    FOUR("four"),
    FIVE("five");

    private final String name;

    CandelabraType(String name) {
        this.name = name;
    }

    public static CandelabraType getByCount( int i ) {
        if( i == 5 ) return FIVE;
        if( i == 4 ) return FOUR;
        if( i == 3 ) return THREE_NORTH_SOUTH;
        if( i == 2 ) return TWO_NORTH_SOUTH;

        return CandelabraType.ONE;
    }

    public int getCount() {
        if( this == ONE ) return 1;
        if( this == TWO_NORTH_SOUTH ) return 2;
        if( this == TWO_WEST_EAST ) return 2;
        if( this == THREE_NORTH_SOUTH ) return 3;
        if( this == THREE_WEST_EAST ) return 3;
        if( this == FOUR ) return 4;
        if( this == FIVE ) return 5;

        return 0;
    }

    public boolean requiresRotation() {
        boolean flag = false;
        flag = flag || ( this == TWO_NORTH_SOUTH );
        flag = flag || ( this == TWO_WEST_EAST );
        flag = flag || ( this == THREE_NORTH_SOUTH );
        flag = flag || ( this == THREE_WEST_EAST );
        return flag;
    }

    public CandelabraType applyRotation( Direction dir ) {

        if( this == TWO_NORTH_SOUTH && (dir == Direction.NORTH || dir == Direction.SOUTH) ) {
            return TWO_WEST_EAST;
        }

        if( this == THREE_NORTH_SOUTH && (dir == Direction.NORTH || dir == Direction.SOUTH) ) {
            return THREE_WEST_EAST;
        }

        if( this == TWO_WEST_EAST && (dir == Direction.WEST || dir == Direction.EAST) ) {
            return TWO_NORTH_SOUTH;
        }

        if( this == THREE_WEST_EAST && (dir == Direction.WEST || dir == Direction.EAST) ) {
            return THREE_NORTH_SOUTH;
        }

        return this;

    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
