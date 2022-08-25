package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;

import java.util.Arrays;
import java.util.Random;

public class NetherGrassFeature extends SimpleFeature<DefaultFeatureConfig> {

	private final static int RADIUS = 7;
	private final static int HEIGHT = 3;
	private final static BlockState GRASS = ModBlocks.NETHER_GRASS.getDefaultState();
	private final static BlockState FERN = ModBlocks.NETHER_FERN.getDefaultState();

	public NetherGrassFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

		if (!RandUtils.getBool(StylishOccult.SETTING.grass_patch_chance, random)) {
			return false;
		}

		if (world.getBlockState( pos.down() ).getBlock() == Blocks.NETHERRACK) {

			if (pos.getY() > 1 && pos.getY() < 255) {

				int g = RADIUS * RADIUS;

				for (int m = 0; m < g; m ++) {

					BlockPos target = pos.add(
							random.nextInt(RADIUS) - random.nextInt(RADIUS),
							random.nextInt(HEIGHT) - random.nextInt(HEIGHT),
							random.nextInt(RADIUS) - random.nextInt(RADIUS)
					);

					if (world.isAir(target) && target.getY() > 0 && GRASS.canPlaceAt(world, target)) {
						world.setBlockState(target, RandUtils.getBool(StylishOccult.SETTING.fern_chance, random) ? FERN : GRASS, 2);
					}

				}

				return true;
			}

		}

		return false;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return new ConfiguredFeature<>(this, new DefaultFeatureConfig());
	}

	@Override
	public PlacedFeature placed(RegistryEntry<ConfiguredFeature<?, ?>> configured) {
		return new PlacedFeature(
				configured,
				Arrays.asList(
						CountPlacementModifier.of(3)
				)
		);
	}

}
