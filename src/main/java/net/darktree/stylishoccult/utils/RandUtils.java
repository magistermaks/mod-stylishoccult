package net.darktree.stylishoccult.utils;

import java.util.ArrayList;
import java.util.Random;

public class RandUtils {

    private static final Random random = new Random();

    public static <T extends Enum<?>> T getEnum(Class<T> clazz){
        return getEnum( clazz, random );
    }

    public static <T extends Enum<?>> T getEnum(Class<T> clazz, Random rand){
        final T[] values = clazz.getEnumConstants();
        return values[ random.nextInt( values.length ) ];
    }

    public static boolean getBool( float probability ) {
        return (Math.random() * 100) <= probability;
    }

    public static boolean getBool( float probability, Random rng ) {
        return (rng.nextDouble() * 100) <= probability;
    }

    public static int rangeInt( int min, int max ) {
        return rangeInt( min, max, random );
    }

    public static int rangeInt( int min, int max, Random rng ) {
        return min + rng.nextInt( (max - min) + 1 );
    }

    public static <E> E getListEntry( ArrayList<E> list, Random random ) {
        return list.get( random.nextInt( list.size() ) );
    }

    public static <E> E getArrayEntry( E[] array, Random random ) {
        return array[ random.nextInt( array.length ) ];
    }

}
