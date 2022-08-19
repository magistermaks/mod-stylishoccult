package net.darktree.stylishoccult.utils;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.Direction;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Directions {

	private static final ImmutableMap<Direction, Direction[]> DIRECT = bake(direction -> new Direction[] {direction});
	private static final ImmutableMap<Direction, Direction[]> OPPOSITES = bake(direction -> new Direction[] {direction, direction.getOpposite()});
	public static final Direction[] ALL_EXCEPT_UP = new Direction[] {Direction.DOWN, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};
	public static final Direction[] HORIZONTAL = new Direction[] {Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};
	public static final Direction[] NONE = new Direction[] {};
	public static final Direction[] ALL = Direction.values();

	public static Direction[] of(Direction direction) {
		return DIRECT.get(direction);
	}

	public static Direction[] opposites(Direction direction) {
		return OPPOSITES.get(direction);
	}

	public static Direction[] pair(Direction first, Direction second) {
		return new Direction[] {first, second};
	}

	private static ImmutableMap<Direction, Direction[]> bake(Function<Direction, Direction[]> supplier) {
		return ImmutableMap.copyOf(Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), supplier)));
	}

}
