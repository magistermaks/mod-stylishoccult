package net.darktree.stylishoccult.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.darktree.stylishoccult.worldgen.feature.*;
import net.darktree.stylishoccult.worldgen.processor.BlackstoneStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.DeepslateStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.SanctumStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.StoneStructureProcessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class WorldGen {

	public static void addFeature(String name, Predicate<BiomeSelectionContext> selector, GenerationStep.Feature step, SimpleFeatureProvider feature) {
		Identifier id = new ModIdentifier(name);
		Registry.register(Registry.FEATURE, id, (Feature<?>) feature);
		ConfiguredFeature<?, ?> configured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configure());
		Registry.register(BuiltinRegistries.PLACED_FEATURE, id, feature.placed(RegistryEntry.of(configured)));
		BiomeModifications.addFeature(selector, step, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
	}

//	public static void addStructure(Identifier id, StructureConfig config, boolean adjustsSurface, Predicate<BiomeSelectionContext> selector, StructureFeature<DefaultFeatureConfig> feature, GenerationStep.Feature step) {
//		ConfiguredStructureFeature<DefaultFeatureConfig, ?> configured = feature.configure(new DefaultFeatureConfig());
//		FabricStructureBuilder<?, ?> builder = FabricStructureBuilder.create(id, feature).step(step).defaultConfig(config.spacing, config.separation, config.salt).superflatFeature(configured);
//		if (adjustsSurface) builder.adjustsSurface();
//		builder.register();
//		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(id.getNamespace(), "configured_" + id.getPath()), configured);
//
//		// add to biomes
//		BiomeModifications.create(id).add(ModificationPhase.ADDITIONS, selector, (context) -> {
//			context.getGenerationSettings().addBuiltInStructure(configured);
//		});
//	}

	public static StructureProcessorList addProcessorList(String name, StructureProcessor processor) {
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, new ModIdentifier(name), new StructureProcessorList(ImmutableList.of(processor))).value();
	}

	public static StructureProcessorType<?> addProcessorType(String name, Supplier<StructureProcessor> supplier) {
		return StructureProcessorType.register(StylishOccult.NAMESPACE + "_" + name + "_processor", Codec.unit(supplier));
	}

//	public static StructurePoolFeatureConfig getPool(DynamicRegistryManager registry, String path, int size) {
//		return new StructurePoolFeatureConfig(() -> registry.get(Registry.STRUCTURE_POOL_KEY).get(new ModIdentifier(path)), size);
//	}

	// Structure processors
	private static final StructureProcessorList STONE_DECAY_PROCESSOR = WorldGen.addProcessorList("stone_decay", new StoneStructureProcessor());
	private static final StructureProcessorList DEEPSLATE_DECAY_PROCESSOR = WorldGen.addProcessorList("deepslate_decay", new DeepslateStructureProcessor());
	private static final StructureProcessorList BLACKSTONE_DECAY_PROCESSOR = WorldGen.addProcessorList("blackstone_decay", new BlackstoneStructureProcessor());
	private static final StructureProcessorList SANCTUM_PROCESSOR = WorldGen.addProcessorList("sanctum", new SanctumStructureProcessor());

	public static void init() {

		// Features
		WorldGen.addFeature(
				"lava_demon_feature",
				BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.OCEAN).negate()),
				GenerationStep.Feature.VEGETAL_DECORATION,
				new LavaDemonFeature(DefaultFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"flesh_patch_feature",
				BiomeSelectors.foundInTheNether().and(BiomeSelectors.excludeByKey(BiomeKeys.BASALT_DELTAS)),
				GenerationStep.Feature.UNDERGROUND_ORES,
				new FleshPatchFeature(OreFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"flesh_stone_patch_feature",
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_ORES,
				new FleshStonePatchFeature(OreFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"nether_grass_patch_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new NetherGrassFeature(DefaultFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"spark_vent_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new SparkVentFeature(DefaultFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"runic_wall_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new WallsFeature(DefaultFeatureConfig.CODEC)
		);

		WorldGen.addFeature(
				"boulder_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new BoulderFeature(DefaultFeatureConfig.CODEC)
		);

		// FIXME structures are borked
//		// Structures
//		WorldGen.addStructure(
//				new ModIdentifier("sanctum"),
//				StylishOccult.SETTING.sanctum, true,
//				BiomeSelectors.foundInTheNether().and(BiomeSelectors.excludeByKey(BiomeKeys.BASALT_DELTAS, BiomeKeys.SOUL_SAND_VALLEY)),
//				new SanctumStructure(8, 4),
//				GenerationStep.Feature.SURFACE_STRUCTURES
//		);
//
//		WorldGen.addStructure(
//				new ModIdentifier("stonehenge"),
//				StylishOccult.SETTING.stonehenge, true,
//				BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.TAIGA)),
//				new StonehengeStructure(6),
//				GenerationStep.Feature.SURFACE_STRUCTURES
//		);

	}

}
