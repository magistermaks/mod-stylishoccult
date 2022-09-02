package net.darktree.stylishoccult.utils;

import net.minecraft.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Random;

public class RandUtils {

	/**
	 * Get a boolean value depending on given probability
	 *
	 * @param probability the percentage chance that `true` will be returned, must be between 0 and 100
	 */
	public static boolean nextBool(float probability, Random random) {
		return (random.nextDouble() * 100) <= probability;
	}

	/**
	 * Pick a random integer value in range
	 *
	 * @param min the minimum value, inclusive
	 * @param max the maximum value, inclusive
	 */
	public static int nextInt(int min, int max, Random random) {
		return MathHelper.nextInt(random, min, max);
	}

	/**
	 * Pick a random value from an enum class
	 */
	public static <T extends Enum<?>> T pickFromEnum(Class<T> clazz, Random random){
		final T[] values = clazz.getEnumConstants();
		return values[random.nextInt(values.length)];
	}

	/**
	 * Pick a random element from a Java array
	 */
	public static <E> E pickFromArray(E[] array, Random random) {
		return array[random.nextInt(array.length)];
	}

	/**
	 * Pick a random entry from any Minecraft tag
	 */
	public static <T> T pickFromTag(TagKey<T> tag, Random random, T fallback) {
		@SuppressWarnings("unchecked") Registry<T> registry = (Registry<T>) Registry.REGISTRIES.get(tag.registry().getValue());
		return registry == null ? fallback : registry.getEntryList(tag).flatMap(blocks -> blocks.getRandom(random)).map(RegistryEntry::value).orElse(fallback);
	}

}
