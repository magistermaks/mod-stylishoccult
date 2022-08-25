package net.darktree.stylishoccult.tag;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTags {

	// lists all rune blocks
	public static TagKey<Block> RUNES = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("runes"));

	// lists all flesh blocks
	public static TagKey<Block> FLESH = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("flesh"));

	// empty by default, blocks in this list are made corruptible
	public static TagKey<Block> CORRUPTIBLE = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("corruptible"));

	// empty by default, blocks in this list are made incorruptible
	public static TagKey<Block> INCORRUPTIBLE = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("incorruptible"));

	// block that can be found in boulder features
	public static TagKey<Block> BOULDER_FILLER = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("boulder_filler"));

	// all fluids that should use the blood overlays
	public static TagKey<Fluid> BLOOD = TagKey.of(Registry.FLUID_KEY, ModIdentifier.of("blood"));

	// lists all "top_soil" blocks (grass_block, nylium blocks etc.), makes them turn to soil_flesh not default_flesh
	public static TagKey<Block> TOP_SOIL = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "top_soil"));

	// lists all heat emitting blocks (fire, campfires, lava) if the block has a LIT property is must be set to true
	public static TagKey<Block> HEAT_SOURCE = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "heat_source"));

	// list of blocks that nether grass and ferns can be planted on (excluding the common soils like dirt, nylium, etc.)
	public static TagKey<Block> NETHER_GRASS_SOIL = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("nether_grass_soil"));

	// list of blacks that will be used to generate runic walls, except the runes, those are picked separately - from the runes block tag
	public static TagKey<Block> RUNIC_WALL = TagKey.of(Registry.BLOCK_KEY, ModIdentifier.of("runic_wall"));

	public static void init() {
		// noop
	}

}
