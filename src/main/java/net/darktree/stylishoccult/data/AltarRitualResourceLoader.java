package net.darktree.stylishoccult.data;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AltarRitualResourceLoader extends SimpleDirectoryResourceReloadListener {

	private final ArrayList<AltarRitual> rituals = new ArrayList<>();
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
		rituals.add(GSON.fromJson(reader, AltarRitual.Json.class).build(identifier, hashes));
	}

	public AltarRitual find(Item catalyst, List<Item> ingredients) {
		return rituals.stream().filter(ritual -> ritual.match(catalyst, ingredients)).findAny().orElse(null);
	}

	public AltarRitual find(Identifier identifier) {
		return rituals.stream().filter(ritual -> ritual.match(identifier)).findAny().orElse(null);
	}

}
