package net.darktree.stylishoccult.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.SparkVentBlock;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.placementmodifier.CountMultilayerPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.Random;

public class SparkVentFeature extends SimpleFeature<DefaultFeatureConfig> {

	private final static BlockState VENT = ModBlocks.SPARK_VENT.getDefaultState();
	private final static BlockState LAVA = Blocks.LAVA.getDefaultState();

	public SparkVentFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!RandUtils.nextBool(StylishOccult.SETTING.spark_vent_chance, random)) {
			return false;
		}

		if (!world.getBlockState(pos).isAir() || !world.getBlockState(pos.up()).isAir()) {
			return false;
		}

		BlockPos vent = pos.down();
		if (world.getBlockState(vent).getBlock() == Blocks.NETHERRACK) {

			BlockPos source = vent.down();
			if (world.getBlockState(source).getBlock() == Blocks.NETHERRACK) {

				if( BlockUtils.touchesAir(world, source) ) {
					return false;
				}

				BlockPos floor = source.down();
				if (world.getBlockState(floor).getBlock() == Blocks.NETHERRACK) {

					boolean hasSource = generateSource(world, random, source);
					placeBlock(world, vent, VENT.with(SparkVentBlock.ACTIVE, hasSource));

					this.debugWrite(pos);
					return true;

				}
			}
		}

		return false;
	}

	private boolean generateSource(StructureWorldAccess world, Random random, BlockPos pos) {
		if (RandUtils.nextBool(95.0f, random)) {
			placeBlock(world, pos, LAVA);

			for (int i = random.nextInt(10); i > 0; i --) {
				Direction dir = RandUtils.pickFromEnum(Direction.class, random);

				if (dir != Direction.UP) {
					BlockPos tmp = pos.offset(dir);

					if (!BlockUtils.touchesAir(world, tmp)) {

						Block block = world.getBlockState(tmp).getBlock();
						if (block == Blocks.NETHERRACK || block == Blocks.LAVA) {
							placeBlock(world, tmp, LAVA);
							pos = tmp;
						}

					} else {
						break;
					}

				}
			}

			return true;
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
