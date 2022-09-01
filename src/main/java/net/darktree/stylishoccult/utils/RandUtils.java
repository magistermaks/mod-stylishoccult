package net.darktree.stylishoccult.utils;

import net.minecraft.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Random;

public class RandUtils {

	// rename to nextBool, add javadoc
	public static boolean getBool(float probability, Random random) {
		return (random.nextDouble() * 100) <= probability;
	}

	// rename to nextInt, add javadoc
	public static int rangeInt(int min, int max, Random random) {
		return MathHelper.nextInt(random, min, max);
	}

	// rename to pickFromEnum, add javadoc
	public static <T extends Enum<?>> T getEnum(Class<T> clazz, Random random){
		final T[] values = clazz.getEnumConstants();
		return values[random.nextInt(values.length)];
	}

	// rename to pickFromArray, add javadoc
	public static <E> E getArrayEntry(E[] array, Random random) {
		return array[random.nextInt(array.length)];
	}

	// add javadoc
	public static <T> T pickFromTag(TagKey<T> tag, Random random, T fallback) {
		@SuppressWarnings("unchecked") Registry<T> registry = (Registry<T>) Registry.REGISTRIES.get(tag.registry().getValue());
		return registry == null ? fallback : registry.getEntryList(tag).flatMap(blocks -> blocks.getRandom(random)).map(RegistryEntry::value).orElse(fallback);
	}

}
