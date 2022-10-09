package net.darktree.stylishoccult.config;

/*
 * Copyright (c) 2022 magistermaks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.google.gson.internal.Primitives;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Config<T> {

	private static final Logger LOGGER = LogManager.getLogger("Config");

	private final ConfigRequest<T> request;
	private final List<Property<T>> properties = new ArrayList<>();
	private final Map<String, List<Property<T>>> groups = new LinkedHashMap<>();

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Entry{

		/**
		 * The key in the config file, if left at the default value
		 * the name of the filed will be used as the key
		 */
		String key() default "";

		/**
		 * The config group, this becomes a tab in GUI and a name
		 * prefix in the config file
		 */
		String group() default "master";

		/**
		 * The minimum allowed value of this entry,
		 * when applied to a String entry it determines the minimum string length
		 */
		float min() default Float.MIN_VALUE;

		/**
		 * The maximum allowed value of this entry,
		 * when applied to a String entry it determines the maximum string length
		 */
		float max() default Float.MAX_VALUE;

		/**
		 * IIndicates whether the game must be restarted
		 * for this config entry to be applied
		 */
		boolean restart() default false;

	}

	/**
	 * Used to mark classes that need to be recursively added to the config
	 */
	public interface ConfigProperty {

	}

	public interface HeaderProvider {
		String get();

		static String timestamped() { return "# Last updated on " + new Date() + "\n"; }
		static String empty() { return ""; }
	}

	public static class ConfigRequest<T> {

		private final Class<T> clazz;
		private final File file;
		private final String path;
		private HeaderProvider headerProvider;

		private ConfigRequest(Class<T> clazz, File file, String path) {
			this.clazz = clazz;
			this.file = file;
			this.path = path;
			this.headerProvider = HeaderProvider::timestamped;
		}

		/**
		 * Sets the default config provider, used to generate the
		 * config header on every save and if it's missing.
		 *
		 * @param headerProvider config header
		 * @return current config request object
		 * @see HeaderProvider
		 */
		public ConfigRequest<T> using(HeaderProvider headerProvider) {
			this.headerProvider = headerProvider;
			return this;
		}

		/**
		 * Load the config, returns the constructed config object
		 *
		 * @return config
		 */
		public Config<T> request() {
			return new Config<>(this);
		}

		private String getHeader() {
			return headerProvider.get() + "\n";
		}

	}

	/**
	 * Creates new config request object, ideally `filename`
	 * should contain the mod id of the calling mod
	 *
	 * @param clazz The class describing the config
	 * @param filename The name of the config file, without extension
	 */
	public static <T> ConfigRequest<T> of(Class<T> clazz, String filename) {
		Path path = FabricLoader.getInstance().getConfigDir();
		return new ConfigRequest<>(clazz, path.resolve(filename + ".properties").toFile(), filename);
	}

	/**
	 * Tries to save the given string as the config file
	 */
	private void createConfigFile(String contents) {
		try {
			// try creating missing files
			try {
				request.file.getParentFile().mkdirs();
				Files.createFile(request.file.toPath());
			} catch (IOException ignore) {
				// we don't really care much
			}

			// write default config data
			PrintWriter writer = new PrintWriter(request.file, "UTF-8");
			writer.write(contents);
			writer.close();
		} catch (Exception exception) {
			LOGGER.error("Filed to write to config file!", exception);
		}
	}

	/**
	 * Tries to load the config contents into a key-value string map
	 */
	private void loadConfigFile(HashMap<String, String> config) throws IOException {
		Scanner reader = new Scanner(request.file);
		for(int line = 1; reader.hasNextLine(); line ++) {
			parseConfigFileEntry(reader.nextLine(), line, config);
		}
	}

	/**
	 * Parses a single config line, adds key-value pair to map (if present)
	 */
	private void parseConfigFileEntry(String entry, int line, HashMap<String, String> config) {
		if (!entry.isEmpty() && !entry.startsWith("#")) {
			String[] parts = entry.split("=", 2);
			if (parts.length == 2) {
				config.put(parts[0], parts[1]);
			} else {
				throw new RuntimeException("Syntax error in config file on line " + line + "!");
			}
		}
	}

	private Config(ConfigRequest<T> request) {
		final HashMap<String, String> config = new HashMap<>();
		this.request = request;

		if (!request.file.exists()) {
			createConfigFile(request.getHeader());
		}

		try {
			loadConfigFile(config);
		} catch (Exception e) {
			LOGGER.error("Config '" + request.path + "' failed to load!");
			config.clear();
		}

		T instance = getDefault();
		appendClassFields(null, "", new FieldStack(), request.clazz, instance);

		// load config from file
		properties.forEach(property -> {
			property.setAndApply(get(config, property.key, property.type, property.defaultValue));
		});
	}

	private void appendClassFields(String group, String prefix, FieldStack stack, Class<?> clazz, Object object) {
		Field[] fields = clazz.getDeclaredFields();

		// create property list
		for (Field field : fields) {
			Entry entry = field.getAnnotation(Entry.class);

			if (entry != null) {
				int modifiers = field.getModifiers();

				if (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers)) {
					LOGGER.warn("Config entry '" + field + "' in: " + clazz + " must be public and non-final! Ignoring...");
					continue;
				}

				String key = entry.key();

				if (key.equals("")) {
					key = field.getName();
				}

				if (group == null) {
					groups.putIfAbsent(entry.group(), new ArrayList<>());
				}

				try {
					Object instance = field.get(object);
					Class<?> type = field.getType();
					String path = prefix + (group != null ? "" : entry.group() + ".") + key;

					// add properties
					if (ConfigProperty.class.isAssignableFrom(type)) {
						appendClassFields(entry.group(), path + ".", stack.push(field), type, instance);
					} else {
						Property<T> property = new Property<>(path, stack.push(field), entry.min(), entry.max(), entry.restart(), instance);

						if (group == null) {
							groups.get(entry.group()).add(property);
						} else {
							groups.get(group).add(property);
						}

						properties.add(property);
					}
				} catch (Exception e) {
					LOGGER.warn("Config entry '" + field + "' in: " + clazz + " failed to be parsed!", e);
				}
			}
		}
	}

	/**
	 * Get a config instance reflecting the default state
	 */
	private T getDefault() {
		try {
			return request.clazz.newInstance();
		} catch (Exception exception) {
			LOGGER.error("Failed to instantiate config class!", exception);
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Get a config instance reflecting current state
	 */
	public T getConfigured() {
		T instance = getDefault();
		properties.forEach(property -> property.reflect(instance));
		return instance;
	}

	/**
	 * Applies are non-applied config changes
	 */
	public void applyAllChanges() {
		int count = getChangedCount();

		if (count > 0) {
			properties.forEach(Property::apply);
			LOGGER.info("Applied " + count + " config changes!");
		}

		generateConfigFile();
	}

	/**
	 * Discards all non-applied config changes
	 */
	public void discardAllChanges() {
		int count = getChangedCount();

		if (count > 0) {
			properties.forEach(Property::discard);
			LOGGER.info("Discarded " + count + " config changes!");
		}
	}

	/**
	 * Get number of non-applied config changes
	 */
	public int getChangedCount() {
		return (int) properties.stream().filter(Property::isModified).count();
	}

	/**
	 * Update config file with the changes made to the loaded properties
	 */
	private void generateConfigFile() {
		StringBuilder builder = new StringBuilder(request.getHeader());

		properties.forEach(property -> {
			property.append(builder);
		});

		createConfigFile(builder.toString());
	}

	public Set<Map.Entry<String, List<Property<T>>>> getGroups() {
		return groups.entrySet();
	}

	public static class Property<T> {
		public final String key;
		public final Class<?> type;
		public final boolean requiresRestart;
		private final FieldStack stack;
		private final float min;
		private final float max;
		private final Object defaultValue;
		private Object currentValue;
		private Object futureValue;

		private Property(String key, FieldStack stack, float min, float max, boolean restart, Object defaultValue) {
			this.key = key;
			this.type = Primitives.wrap(stack.type());
			this.stack = stack;
			this.min = min;
			this.max = max;
			this.requiresRestart = restart;
			this.defaultValue = defaultValue;
			this.currentValue = defaultValue;
			this.futureValue = defaultValue;
		}

		/**
		 * Set the value, no changes will be visible until they are applied
		 */
		public void set(Object value) {
			this.futureValue = value;
		}

		/**
		 * Set the value from string, no changes will be visible until they are applied
		 */
		public boolean parse(String string) {
			if (type == String.class && string.length() < min && string.length() > max) {
				return false;
			}

			try {
				Object value = Config.parse(type, string);

				if (value instanceof Number number) {
					float f = number.floatValue();

					if (f < min || f > max) {
						return false;
					}
				}

				this.futureValue = value;
			} catch (Exception ignore) {
				return false;
			}

			return true;
		}

		/**
		 * Get the current value
		 */
		public Object get() {
			return this.futureValue;
		}

		/**
		 * Restore this property to its default state
		 */
		public void restore() {
			this.futureValue = this.defaultValue;
		}

		/**
		 * Check if there are non-applied changes made to this property
		 */
		public boolean isModified() {
			return !this.currentValue.equals(this.futureValue);
		}

		/**
		 * Check if this property differs from the default value
		 */
		public boolean isNotDefault() {
			return !this.futureValue.equals(this.defaultValue);
		}

		private void setAndApply(Object value) {
			this.currentValue = this.futureValue = value;
		}

		private void reflect(T instance) {
			try {
				stack.set(instance, this.currentValue);
			} catch (Exception exception) {
				LOGGER.error("Failed to update config data!", exception);
			}
		}

		private void discard() {
			this.futureValue = this.currentValue;
		}

		private void apply() {
			this.currentValue = this.futureValue;
		}

		private void append(StringBuilder builder) {
			if (!this.defaultValue.equals(this.currentValue)) {
				builder.append(key).append("=").append(this.currentValue.toString()).append('\n');
			}
		}
	}

	private Object get(HashMap<String, String> config, String key, Class<?> clazz, Object def) {
		String value = config.get(key);
		if (value == null) return def;

		Object parsed;

		try {
			parsed = parse(clazz, value);
		} catch (Exception exception) {
			LOGGER.warn("Failed to parse config entry '" + value + "' as type '" + clazz.getName() + "'!");
			return def;
		}

		if (parsed == null) {
			LOGGER.warn("Failed to map config entry to type!");
			return def;
		}

		return parsed;
	}

	private static Object parse(Class<?> type, String value) {
		if (type == String.class) return value;
		if (type == Integer.class) return Integer.parseInt(value);
		if (type == Float.class) return Float.parseFloat(value);
		if (type == Boolean.class) return Boolean.parseBoolean(value);

		return null;
	}

	private static class FieldStack {

		private final List<Field> fields = new ArrayList<>();

		public FieldStack push(Field field) {
			FieldStack child = new FieldStack();
			child.fields.addAll(fields);
			child.fields.add(field);
			return child;
		}

		public void set(Object instance, Object value) {
			int last = fields.size() - 1;

			try {
				for (int i = 0; i < fields.size(); i++) {
					if (i == last) {
						fields.get(i).set(instance, value);
					} else {
						instance = fields.get(i).get(instance);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public Class<?> type() {
			return fields.get(fields.size() - 1).getType();
		}

	}

}
