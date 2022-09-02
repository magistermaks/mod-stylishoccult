package net.darktree.stylishoccult.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.mixin.StructureFeatureAccessor;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.SimpleFeatureProvider;
import net.darktree.stylishoccult.worldgen.feature.*;
import net.darktree.stylishoccult.worldgen.processor.BlackstoneStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.DeepslateStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.SanctumStructureProcessor;
import net.darktree.stylishoccult.worldgen.processor.StoneStructureProcessor;
import net.darktree.stylishoccult.worldgen.structure.SanctumStructure;
import net.darktree.stylishoccult.worldgen.structure.StonehengeStructure;
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
import net.minecraft.world.gen.feature.*;

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

	public static void addStructure(Identifier id, StructureFeature<StructurePoolFeatureConfig> structure, GenerationStep.Feature step) {
		Registry.register(Registry.STRUCTURE_FEATURE, id, structure);
		StructureFeatureAccessor.stylish_getStructureGenerationStepMap().put(structure, step);
	}

	public static StructureProcessorList addProcessorList(String name, StructureProcessor processor) {
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, new ModIdentifier(name), new StructureProcessorList(ImmutableList.of(processor))).value();
	}

	public static StructureProcessorType<?> addProcessorType(String name, Supplier<StructureProcessor> supplier) {
		return StructureProcessorType.register(StylishOccult.NAMESPACE + "_" + name + "_processor", Codec.unit(supplier));
	}

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

		// Structures
		WorldGen.addStructure(
				new ModIdentifier("sanctum"),
				new SanctumStructure(8, 4),
				GenerationStep.Feature.SURFACE_STRUCTURES
		);

		WorldGen.addStructure(
				new ModIdentifier("stonehenge"),
				new StonehengeStructure(4),
				GenerationStep.Feature.SURFACE_STRUCTURES
		);

	}

}
