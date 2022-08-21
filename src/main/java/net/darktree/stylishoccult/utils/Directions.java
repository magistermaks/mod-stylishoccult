package net.darktree.stylishoccult.utils;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.Direction;

public class Directions {

	private static final ImmutableMap<Direction, Direction[]> DIRECT = Utils.enumMapOf(Direction.class, direction -> new Direction[] {direction});
	private static final ImmutableMap<Direction, Direction[]> OPPOSITES = Utils.enumMapOf(Direction.class, direction -> new Direction[] {direction, direction.getOpposite()});
	public static final Direction[] ALL_EXCEPT_UP = new Direction[] {Direction.DOWN, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};
	public static final Direction[] HORIZONTAL = new Direction[] {Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};
	public static final Direction[] NONE = new Direction[] {};
	public static final Direction[] ALL = Direction.values();
	public static Direction UNDECIDED = null; // set from DirectionMixin

	public static Direction[] of(Direction direction) {
		return DIRECT.get(direction);
	}

	public static Direction[] opposites(Direction direction) {
		return OPPOSITES.get(direction);
	}

	public static Direction[] pair(Direction first, Direction second) {
		return new Direction[] {first, second};
	}

}
