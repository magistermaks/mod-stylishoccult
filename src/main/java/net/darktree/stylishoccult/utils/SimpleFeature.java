package net.darktree.stylishoccult.utils;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public abstract class SimpleFeature<T extends FeatureConfig> extends Feature<T> implements SimpleFeatureProvider {

	public SimpleFeature(Codec<T> codec) {
		super(codec);
	}

	abstract public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos target, T config);

	@Override
	public boolean generate(FeatureContext<T> context) {
		return this.generate(context.getWorld(), context.getGenerator(), context.getRandom(), context.getOrigin(), context.getConfig());
	}

	protected final void placeBlock(StructureWorldAccess world, BlockPos pos, BlockState state) {
		if (!world.getBlockState(pos).isIn(BlockTags.FEATURES_CANNOT_REPLACE)) {
			world.setBlockState(pos, state, Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS);
		}
	}

	protected final void placeBlock(StructureWorldAccess world, BlockPos pos, Block block) {
		placeBlock(world, pos, block.getDefaultState());
	}

}
