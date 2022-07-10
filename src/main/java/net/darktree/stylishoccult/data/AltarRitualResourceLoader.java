package net.darktree.stylishoccult.data;

import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AltarRitualResourceLoader extends SimpleDirectoryResourceReloadListener {

	public final ArrayList<AltarRitual> rituals = new ArrayList<>();

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
	}

	@Override
	public void apply(Identifier identifier, Reader reader) {
		rituals.add(GSON.fromJson(reader, AltarRitual.Json.class).build());
	}

	public AltarRitual find(Item catalyst, List<Item> ingredients) {
		return rituals.stream().filter(ritual -> ritual.match(catalyst, ingredients)).findAny().orElse(null);
	}
}
