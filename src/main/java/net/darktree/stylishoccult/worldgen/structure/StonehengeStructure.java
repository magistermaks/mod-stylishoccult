package net.darktree.stylishoccult.worldgen.structure;

import com.google.common.collect.ImmutableList;
import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class StonehengeStructure extends StructureFeature<StructurePoolFeatureConfig> {
	private final boolean modifyBoundingBox;
	private final boolean surface;
	private final int slope;

	public StonehengeStructure(boolean modifyBoundingBox, boolean surface, int slope) {
		super(StructurePoolFeatureConfig.CODEC);
		this.modifyBoundingBox = modifyBoundingBox;
		this.surface = surface;
		this.slope = slope;
	}

	@Override
	public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
		return StonehengeStructure.Start::new;
	}

	/**
	 * Try to limit the number of
	 * @param generator
	 * @param pos
	 * @param view
	 * @return
	 */
	public boolean verifyPlacementPosition(ChunkGenerator generator, ChunkPos pos, HeightLimitView view) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		ImmutableList<BlockPos> list = ImmutableList.of(
				new BlockPos(pos.getStartX() + 3, 0, pos.getStartZ() + 3),
				new BlockPos(pos.getEndX() - 3, 0, pos.getStartZ() + 3),
				new BlockPos(pos.getStartX() + 3, 0, pos.getEndZ() - 3),
				new BlockPos(pos.getEndX() - 3, 0, pos.getEndZ() - 3)
		);

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		for (BlockPos samplePos : list) {
			VerticalBlockSample sample = generator.getColumnSample(samplePos.getX(), samplePos.getZ(), view);

			int y = Integer.MIN_VALUE;

			for (int i = 200; i > 40; i --) {
				mutable.set(0, i, 0);

				BlockState state = sample.getState(mutable);

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

		return max - min <= this.slope;
	}

	@Override
	protected boolean isUniformDistribution() {
		return false;
	}

	@Override
	protected boolean shouldStartAt(ChunkGenerator generator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, StructurePoolFeatureConfig config, HeightLimitView view) {
		return verifyPlacementPosition(generator, pos, view);
	}

	public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
		public Start(StructureFeature<StructurePoolFeatureConfig> s, ChunkPos c, int i, long l) {
			super(s, c, i, l);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator generator, StructureManager manager, ChunkPos pos, Biome biome, StructurePoolFeatureConfig config, HeightLimitView view) {
			StonehengeStructure structure = (StonehengeStructure) getFeature();

			StructurePoolBasedGenerator.generate(registryManager, config, PoolStructurePiece::new, generator, manager, new BlockPos(pos.x << 4, 0, pos.z << 4), this, this.random, structure.modifyBoundingBox, structure.surface, view);
			this.setBoundingBoxFromChildren();
		}
	}
}
