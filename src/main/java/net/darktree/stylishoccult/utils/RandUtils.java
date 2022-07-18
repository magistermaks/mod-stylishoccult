package net.darktree.stylishoccult.utils;

import java.util.Random;

public class RandUtils {

    public static final Random RANDOM = new Random();

    public static <T extends Enum<?>> T getEnum(Class<T> clazz){
        return getEnum(clazz, RANDOM);
    }

    public static <T extends Enum<?>> T getEnum(Class<T> clazz, Random random){
        final T[] values = clazz.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    public static boolean getBool(float probability) {
        return (Math.random() * 100) <= probability;
    }

    public static boolean getBool(float probability, Random random) {
        return (random.nextDouble() * 100) <= probability;
    }

    public static int rangeInt(int min, int max) {
        return rangeInt(min, max, RANDOM);
    }

    public static int rangeInt(int min, int max, Random random) {
        return min + random.nextInt( (max - min) + 1 );
    }

    public static <E> E getArrayEntry(E[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

}
