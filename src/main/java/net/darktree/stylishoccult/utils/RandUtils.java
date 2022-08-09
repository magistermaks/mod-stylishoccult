package net.darktree.stylishoccult.utils;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class RandUtils {

    public static <T extends Enum<?>> T getEnum(Class<T> clazz, Random random){
        final T[] values = clazz.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    public static boolean getBool(float probability, Random random) {
        return (random.nextDouble() * 100) <= probability;
    }

    public static int rangeInt(int min, int max, Random random) {
        return MathHelper.nextInt(random, min, max);
    }

    public static <E> E getArrayEntry(E[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

}
