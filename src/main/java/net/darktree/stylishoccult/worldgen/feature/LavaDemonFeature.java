package net.darktree.stylishoccult.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.tag.ModTags;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;
import java.util.Random;

public class LavaDemonFeature extends SimpleFeature<DefaultFeatureConfig> {

	public LavaDemonFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos).isAir()) {

			if (!world.getBlockState(pos.down()).isAir()) {
				return false;
			}

			if (!world.getBlockState(pos.up()).isAir()) {
				return false;
			}

			Direction direction = Direction.Type.HORIZONTAL.random(random);
			BlockPos target = pos.offset(direction);

			if (world.getBlockState(target).isIn(ModTags.LAVA_DEMON_REPLACEABLE)) {
				placeBlock(world, target, ModBlocks.LAVA_DEMON.getDefaultState().with(LavaDemonBlock.ANGER, 2).with(LavaDemonBlock.PART, LavaDemonPart.HEAD));
				this.debugWrite(target);
			}
		}

		return true;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return new ConfiguredFeature<>(this, new DefaultFeatureConfig());
	}

	@Override
	public List<PlacementModifier> modifiers() {
		return ImmutableList.of(
				CountPlacementModifier.of(StylishOccult.SETTING.demon_chance),
				SquarePlacementModifier.of(),
				HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(80))
		);
	}

}
