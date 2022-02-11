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
import net.darktree.stylishoccult.worldgen.structure.StonehengeStructure;
import net.darktree.stylishoccult.worldgen.structure.SanctumStructure;
import net.darktree.stylishoccult.worldgen.structure.generator.SanctumGenerator;
import net.darktree.stylishoccult.worldgen.structure.generator.StonehengeGenerator;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
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
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configure());
		RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id);
		BiomeModifications.addFeature(selector, step, key);
	}

	public static void addStructure(String name, int spacing, int separation, boolean adjustsSurface, Predicate<BiomeSelectionContext> selector, StructureFeature<StructurePoolFeatureConfig> feature, StructurePool pool, GenerationStep.Feature step) {
		Identifier id = new ModIdentifier(name);

		ConfiguredStructureFeature<StructurePoolFeatureConfig, ?> configured = feature.configure(new StructurePoolFeatureConfig(() -> pool, 2));
		FabricStructureBuilder<?, ?> builder = FabricStructureBuilder.create(id, feature).step(step).defaultConfig(spacing, separation, 29483148).superflatFeature(configured);
		if (adjustsSurface) builder.adjustsSurface();
		builder.register();
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ModIdentifier("configured_" + name), configured);

		// add to biomes
		BiomeModifications.create(new ModIdentifier(name)).add(ModificationPhase.ADDITIONS, selector, (context) -> {
			context.getGenerationSettings().addBuiltInStructure(configured);
		});
	}

	public static StructureProcessorList addProcessorList(String name, ImmutableList<StructureProcessor> processors) {
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, new ModIdentifier(name), new StructureProcessorList(processors));
	}

	public static StructureProcessorType<?> addProcessorType(String name, Supplier<StructureProcessor> supplier) {
		return StructureProcessorType.register(StylishOccult.NAMESPACE + "_" + name + "_processor", Codec.unit(supplier));
	}

	// Processor lists
	public static StructureProcessorList STONE_PROCESSOR = addProcessorList("stone", ImmutableList.of(new StoneStructureProcessor()));
	public static StructureProcessorList BLACKSTONE_PROCESSOR = addProcessorList("blackstone", ImmutableList.of(new BlackstoneStructureProcessor()));
	public static StructureProcessorList DEEPSLATE_PROCESSOR = addProcessorList("deepslate", ImmutableList.of(new DeepslateStructureProcessor()));
	public static StructureProcessorList SANCTUM_PROCESSOR = addProcessorList("sanctum", ImmutableList.of(new SanctumStructureProcessor()));

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
				new FleshStonePatchFeature( OreFeatureConfig.CODEC )
		);

		WorldGen.addFeature(
				"nether_grass_patch_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new NetherGrassFeature( DefaultFeatureConfig.CODEC )
		);

		WorldGen.addFeature(
				"spark_vent_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new SparkVentFeature( DefaultFeatureConfig.CODEC )
		);

		WorldGen.addFeature(
				"urn_feature",
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				new UrnFeature( DefaultFeatureConfig.CODEC )
		);

		WorldGen.addFeature(
				"runic_wall_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new WallsFeature( DefaultFeatureConfig.CODEC )
		);

		WorldGen.addFeature(
				"boulder_feature",
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.RAW_GENERATION,
				new BoulderFeature( DefaultFeatureConfig.CODEC )
		);

		StonehengeGenerator.init();
		SanctumGenerator.init();

		// Structures
		WorldGen.addStructure(
				"sanctum",
				2, 1, true,
				BiomeSelectors.foundInTheNether().and(BiomeSelectors.excludeByKey(BiomeKeys.BASALT_DELTAS, BiomeKeys.SOUL_SAND_VALLEY)),
				new SanctumStructure(false, false, 8, 5, 4),
				SanctumGenerator.POOL,
				GenerationStep.Feature.SURFACE_STRUCTURES
		);

		WorldGen.addStructure(
				"stonehedge",
				30, 14, true,
				BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.TAIGA)),
				new StonehengeStructure(true, true, 7),
				StonehengeGenerator.POOL,
				GenerationStep.Feature.SURFACE_STRUCTURES
		);

	}

}
