package net.darktree.stylishoccult.data.json;

import it.unimi.dsi.fastutil.ints.IntList;
import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
	private final NbtCompound nbt;

	public static class Json {

		private int blood;
		private boolean consume;
		private String catalyst;
		private String product;
		private String[] ingredients;
		private int count;

		public Json(NbtCompound nbt) {
			this.blood = nbt.getInt("blood");
			this.consume = nbt.getBoolean("consume");
			this.catalyst = nbt.getString("catalyst");
			this.product = nbt.getString("product");
			this.ingredients = nbt.getList("ingredients", NbtElement.STRING_TYPE).stream().map(NbtElement::asString).toArray(String[]::new);
			this.count = nbt.getInt("count");
		}

		public AltarRitual build(Identifier identifier, IntList hashes) {
			final int hash = inputHash();

			if (hashes.contains(hash)) {
				StylishOccult.LOGGER.warn("The input hash of altar ritual '" + identifier.toString() + "' (" + hash + ") collides, verify its uniqueness!");
			}

			if (ingredients.length == 0) {
				StylishOccult.LOGGER.warn("Altar ritual '" + identifier.toString() + "' has no ingredients!");
			}

			hashes.add(hash);
			return new AltarRitual(this.blood, this.consume, this.catalyst, this.product, this.ingredients, this.count, identifier, toNbt());
		}

		private NbtCompound toNbt() {
			NbtList list = new NbtList();

			for (String ingredient : ingredients) {
				list.add(NbtString.of(ingredient));
			}

			NbtCompound nbt = new NbtCompound();
			nbt.putInt("blood", blood);
			nbt.putBoolean("consume", consume);
			nbt.putString("catalyst", catalyst);
			nbt.putString("product", product);
			nbt.put("ingredients", list);
			nbt.putInt("count", count);

			return nbt;
		}

		private int inputHash() {
			return 31 * Objects.hash(catalyst) + Arrays.hashCode(Arrays.stream(ingredients).sorted().toArray());
		}

	}

	private AltarRitual(int blood, boolean consume, String catalyst, String product, String[] ingredients, int count, Identifier identifier, NbtCompound nbt) {
		this.blood = blood;
		this.consume = consume;
		this.catalyst = getItem(catalyst);
		this.product = getItem(product);
		this.ingredients = Arrays.stream(ingredients).map(this::getItem).toArray(Item[]::new);
		this.count = count;
		this.identifier = identifier;
		this.nbt = nbt;
		this.nbt.putString("id", identifier.toString());
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

	public NbtCompound getNbt() {
		return nbt;
	}

}
