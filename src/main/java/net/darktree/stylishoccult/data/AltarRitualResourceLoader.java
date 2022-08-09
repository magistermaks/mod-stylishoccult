package net.darktree.stylishoccult.data;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltarRitualResourceLoader extends SimpleDirectoryResourceReloadListener {

	private final Map<Identifier, AltarRitual> rituals = new HashMap<>();
	private final IntList hashes = new IntArrayList();

	public AltarRitualResourceLoader() {
		super("altar");
	}

	@Override
	public Identifier getFabricId() {
		return new ModIdentifier("altar_recipes");
	}

	@Override
	public void onReloadStart() {
		rituals.clear();
		hashes.clear();
	}

	@Override
	public void onReloadEnd() {
		StylishOccult.LOGGER.info("Loaded " + rituals.size() + " altar rituals.");
	}

	@Override
	public void apply(Identifier identifier, Reader reader) {
		JsonElement json = PARSER.parse(reader);

		if (json.isJsonObject() && json.getAsJsonObject().size() == 0) {
			rituals.remove(identifier);
		} else {
			rituals.put(identifier, GSON.fromJson(json, AltarRitual.Json.class).build(identifier, hashes));
		}
	}

	public AltarRitual find(Item catalyst, List<Item> ingredients) {
		return rituals.values().stream().filter(ritual -> ritual.match(catalyst, ingredients)).findAny().orElse(null);
	}

	public AltarRitual find(Identifier identifier) {
		return rituals.values().stream().filter(ritual -> ritual.match(identifier)).findAny().orElse(null);
	}

	public void sync(NbtList list) {
		StylishOccult.LOGGER.info("Received " + list.size() + " altar rituals to sync from server!");
		rituals.clear();
		hashes.clear();

		for (NbtElement element : list) {
			NbtCompound compound = (NbtCompound) element;
			Identifier id = new Identifier(compound.getString("id"));

			rituals.put(id, new AltarRitual.Json(compound).build(id, hashes));
		}
	}

	public NbtList serialize() {
		NbtList list = new NbtList();
		rituals.values().stream().map(AltarRitual::getNbt).forEach(list::add);

		return list;
	}

}
