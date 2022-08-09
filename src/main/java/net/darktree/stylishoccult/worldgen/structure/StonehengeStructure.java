package net.darktree.stylishoccult.worldgen.structure;

import com.google.common.collect.ImmutableList;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.worldgen.WorldGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class StonehengeStructure extends StructureFeature<DefaultFeatureConfig> {
	private final int slope;

	public StonehengeStructure(int slope) {
		super(DefaultFeatureConfig.CODEC);
		this.slope = slope;
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return StonehengeStructure.Start::new;
	}

	@Override
	protected boolean isUniformDistribution() {
		return false;
	}

	@Override
	protected boolean shouldStartAt(ChunkGenerator generator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig config, HeightLimitView view) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

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

	public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {
		public Start(StructureFeature<DefaultFeatureConfig> structure, ChunkPos pos, int i, long l) {
			super(structure, pos, i, l);
		}

		@Override
		public void init(DynamicRegistryManager registry, ChunkGenerator generator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView view) {
			BlockPos target = new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8);

			// generate pool based structure
			StructurePoolFeatureConfig pool = WorldGen.getPool(registry, "stonehenge/start", StylishOccult.SETTING.stonehenge.depth);
			StructurePoolBasedGenerator.generate(registry, pool, PoolStructurePiece::new, generator, manager, target, this, this.random, false, true, view);

			// center the structure on the chunk center
			StructurePiece piece = this.children.get(0);
			Vec3i center = piece.getBoundingBox().getCenter();
			piece.translate(target.getX() - center.getX(), 0, target.getZ() - center.getZ());

			this.setBoundingBoxFromChildren();
		}
	}
}
