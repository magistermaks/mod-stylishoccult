package net.darktree.stylishoccult.worldgen.structure;

import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Optional;

public class SanctumStructure extends StructureFeature<StructurePoolFeatureConfig> {

	public SanctumStructure(int depth, int clearance) {
		super(StructurePoolFeatureConfig.CODEC, context -> {
			int i = getPlacementHeight(context.chunkGenerator(), context.chunkPos(), context.world(), depth, clearance);

			if (i == Integer.MIN_VALUE) {
				return Optional.empty();
			}

			BlockPos pos = new BlockPos(context.chunkPos().getStartX(), i, context.chunkPos().getStartZ());
			return StructurePoolBasedGenerator.generate(context, PoolStructurePiece::new, pos, false, false);
		});
	}

	private static int getPlacementHeight(ChunkGenerator generator, ChunkPos pos, HeightLimitView view, int depth, int clearance) {
		VerticalBlockSample sample = generator.getColumnSample(pos.getCenterX(), pos.getCenterZ(), view);

		boolean air = false;

		for (int i = view.getTopY(); i > view.getBottomY(); i --) {
			if (!sample.getState(i).isAir()) {
				if (air && verifyPlacementPosition(generator, view, pos, i, depth, clearance)) {
					return i;
				}

				air = false;
			}else{
				air = true;
			}
		}

		return Integer.MIN_VALUE;
	}

	private static boolean verifyPlacementPosition(ChunkGenerator generator, HeightLimitView view, ChunkPos pos, int y, int depth, int clearance) {
		VerticalBlockSample sample = generator.getColumnSample(pos.getStartX(),  pos.getStartZ(), view);

		for (int i = 0; i < clearance; i ++) {
			if (!sample.getState(y + i).isAir()) {
				return false;
			}
		}

		boolean hit = false;

		for (int i = 0; i < depth; i ++) {
			if (!sample.getState(y - i).isAir()) {
				hit = true;
			}else{
				if (hit) {
					return false;
				}
			}
		}

		return hit;
	}

}