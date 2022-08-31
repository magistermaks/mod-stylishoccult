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

//	public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {
//		public Start(StructureFeature<DefaultFeatureConfig> structure, ChunkPos pos, int i, long l) {
//			super(structure, pos, i, l);
//		}
//
//		@Override
//		public void init(DynamicRegistryManager registry, ChunkGenerator generator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView view) {
//			BlockPos target = new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8);
//
//			// generate pool based structure
//			StructurePoolFeatureConfig pool = WorldGen.getPool(registry, "stonehenge/start", StylishOccult.SETTING.stonehenge.depth);
//			StructurePoolBasedGenerator.generate(registry, pool, PoolStructurePiece::new, generator, manager, target, this, this.random, false, true, view);
//
//			// center the structure on the chunk center
//			StructurePiece piece = this.children.get(0);
//			Vec3i center = piece.getBoundingBox().getCenter();
//			piece.translate(target.getX() - center.getX(), 0, target.getZ() - center.getZ());
//
//			this.setBoundingBoxFromChildren();
//		}
//	}

}
