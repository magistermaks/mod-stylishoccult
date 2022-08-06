package net.darktree.stylishoccult.data.json;

import it.unimi.dsi.fastutil.ints.IntList;
import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AltarRitual {

	public final int blood;
	public final boolean consume;
	public final Item catalyst;
	public final Item product;
	public final Item[] ingredients;
	public final int count;
	private final Identifier identifier;

	public static class Json {

		private int blood;
		private boolean consume;
		private String catalyst;
		private String product;
		private String[] ingredients;
		private int count;

		public AltarRitual build(Identifier identifier, IntList hashes) {
			final int hash = inputHash();

			if (hashes.contains(hash)) {
				StylishOccult.LOGGER.warn("The input hash of altar ritual '" + identifier.toString() + "' (" + hash + ") collides, verify its uniqueness!");
			}

			if (ingredients.length == 0) {
				StylishOccult.LOGGER.warn("Altar ritual '" + identifier.toString() + "' has no ingredients!");
			}

			hashes.add(hash);
			return new AltarRitual(this.blood, this.consume, this.catalyst, this.product, this.ingredients, this.count, identifier);
		}

		private int inputHash() {
			return 31 * Objects.hash(catalyst) + Arrays.hashCode(Arrays.stream(ingredients).sorted().toArray());
		}
	}

	private AltarRitual(int blood, boolean consume, String catalyst, String product, String[] ingredients, int count, Identifier identifier) {
		this.blood = blood;
		this.consume = consume;
		this.catalyst = getItem(catalyst);
		this.product = getItem(product);
		this.ingredients = Arrays.stream(ingredients).map(this::getItem).toArray(Item[]::new);
		this.count = count;
		this.identifier = identifier;
	}

	private Item getItem(String key) {
		return Registry.ITEM.get(new Identifier(key));
	}

	public boolean match(Item catalyst, List<Item> ingredients) {
		if (this.catalyst != catalyst || ingredients.size() != this.ingredients.length) return false;

		List<Item> items = new ArrayList<>(ingredients);

		for (Item item : this.ingredients) {
			if (!items.remove(item)) return false;
		}

		return true;
	}

	public boolean match(Identifier identifier) {
		return this.identifier.equals(identifier);
	}

}
