package net.darktree.stylishoccult.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.placementmodifier.CountMultilayerPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
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

		if (!RandUtils.nextBool(StylishOccult.SETTING.grass_patch_chance, random)) {
			return false;
		}

		if (world.getBlockState( pos.down() ).getBlock() == Blocks.NETHERRACK) {

			if (pos.getY() > 1 && pos.getY() < 255) {
				int g = RADIUS * RADIUS;

				for (int i = 0; i < g; i ++) {
					BlockPos target = pos.add(
							random.nextInt(RADIUS) - random.nextInt(RADIUS),
							random.nextInt(HEIGHT) - random.nextInt(HEIGHT),
							random.nextInt(RADIUS) - random.nextInt(RADIUS)
					);

					if (world.isAir(target) && target.getY() > 0 && GRASS.canPlaceAt(world, target)) {
						placeBlock(world, target, RandUtils.nextBool(StylishOccult.SETTING.fern_chance, random) ? FERN : GRASS);
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
	public List<PlacementModifier> modifiers() {
		return ImmutableList.of(
				CountMultilayerPlacementModifier.of(3)
		);
	}

}
