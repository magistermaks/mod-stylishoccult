package net.darktree.stylishoccult.worldgen.structure;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class StonehengeStructure extends JigsawFeature {

	public StonehengeStructure(int slope) {
		super(StructurePoolFeatureConfig.CODEC, 0, false, true, context -> canGenerate(context, slope));
	}

	private static boolean canGenerate(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context, int slope) {
		return canGenerate(context.chunkGenerator(), context.chunkPos(), context.world(), slope);
	}

	private static boolean canGenerate(ChunkGenerator generator, ChunkPos pos, HeightLimitView view, int slope) {

		// TODO look into using StructureGeneratorFactory.Context#getCornerHeights
		ImmutableList<BlockPos> list = ImmutableList.of(
				new BlockPos(pos.getStartX(), 0, pos.getStartZ()),
				new BlockPos(pos.getEndX(), 0, pos.getStartZ()),
				new BlockPos(pos.getStartX(), 0, pos.getEndZ()),
				new BlockPos(pos.getEndX(), 0, pos.getEndZ())
		);

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		for (BlockPos samplePos : list) {
			VerticalBlockSample sample = generator.getColumnSample(samplePos.getX(), samplePos.getZ(), view);

			int y = Integer.MIN_VALUE;

			for (int i = 200; i > 0; i --) {
				BlockState state = sample.getState(i);

				if (state.isAir()) {
					continue;
				}

				if (state.getBlock() == Blocks.WATER) {
					return false;
				}

				y = i;
				break;
			}

			if (y > max) max = y;
			if (y < min) min = y;

		}

		return max - min <= slope;
	}

}
