package net.darktree.stylishoccult.worldgen.structure;

import com.google.common.collect.ImmutableList;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Random;

public class SanctumStructure extends StructureFeature<StructurePoolFeatureConfig> {
	private final int depth;
	private final int slope;
	private final int clearance;

	public SanctumStructure(int depth, int slope, int clearance) {
		super(StructurePoolFeatureConfig.CODEC);
		this.depth = depth;
		this.slope = slope;
		this.clearance = clearance;
	}

	@Override
	public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
		return SanctumStructure.Start::new;
	}

	@Override
	protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, StructurePoolFeatureConfig config, HeightLimitView world) {
		return true;
	}

	private int getPlacementHeight(ChunkGenerator generator, ChunkPos pos, HeightLimitView view, BlockBox box, Random random) {
		VerticalBlockSample sample = generator.getColumnSample(pos.getCenterX(), pos.getCenterZ(), view);
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		boolean air = false;

		for (int i = view.getTopY(); i > view.getBottomY(); i --) {
			mutable.set(0, i, 0);
			if (!sample.getState(mutable).isAir()) {
				if (air && verifyPlacementPosition(generator, pos, view, box, random, i)) {
					return i;
				}

				air = false;
			}else{
				air = true;
			}
		}

		return Integer.MIN_VALUE;
	}

	public boolean verifyPlacementPosition(ChunkGenerator generator, ChunkPos pos, HeightLimitView view, BlockBox box, Random random, int y) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		ImmutableList<BlockPos> list = ImmutableList.of(
				new BlockPos(box.getMinX(), 0, box.getMinZ()),
				new BlockPos(box.getMaxX(), 0, box.getMinZ()),
				new BlockPos(box.getMinX(), 0, box.getMaxZ()),
				new BlockPos(box.getMaxX(), 0, box.getMaxZ())
		);

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		for (BlockPos samplePos : list) {
			VerticalBlockSample sample = generator.getColumnSample(samplePos.getX(), samplePos.getZ(), view);

			for (int i = 0; i < this.clearance; i ++) {
				mutable.set(0, y + i, 0);

				if ( !sample.getState(mutable).isAir() ) {
					return false;
				}
			}

			boolean hit = false;

			for (int i = 0; i < this.depth; i ++) {
				mutable.set(0, y - i, 0);

				if (!sample.getState(mutable).isAir()) {

					if (!hit) {
						if (i > max) max = i;
						if (i < min) min = i;
					}

					hit = true;
				}else{
					if (hit) {
						return false;
					}
				}
			}

			if (!hit) {
				return false;
			}

		}

		return (max - min) <= this.slope;
	}

	public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
		public Start(StructureFeature<StructurePoolFeatureConfig> s, ChunkPos c, int i, long l) {
			super(s, c, i, l);
		}

		@Override
		public void init(DynamicRegistryManager registry, ChunkGenerator generator, StructureManager manager, ChunkPos pos, Biome biome, StructurePoolFeatureConfig config, HeightLimitView view) {
			Identifier id = new ModIdentifier("sanctum/upper");
			SanctumStructure feature = (SanctumStructure) this.getFeature();
			BlockRotation rotation = Util.getRandom(BlockRotation.values(), this.random);

			Structure structure = manager.getStructureOrBlank(id);

			BlockMirror mirror = this.random.nextFloat() < 0.5f ? BlockMirror.NONE : BlockMirror.FRONT_BACK;
			BlockPos blockPos = new BlockPos(structure.getSize().getX() / 2, 0, structure.getSize().getZ() / 2);
			BlockBox box = structure.calculateBoundingBox(pos.getStartPos(), rotation, blockPos, mirror);

			int l = feature.getPlacementHeight(generator, pos, view, box, this.random);

			if (l == Integer.MIN_VALUE) {
				return;
			}

//			StructurePoolBasedGenerator.generate(registryManager, config, PoolStructurePiece::new, chunkGenerator, manager, new BlockPos(pos.x << 4, l, pos.z << 4), this, this.random, feature.modifyBoundingBox, feature.surface, view);
//			this.setBoundingBoxFromChildren();

			StructurePoolFeatureConfig pool = new StructurePoolFeatureConfig(
					() -> registry.get(Registry.STRUCTURE_POOL_KEY).get(new ModIdentifier("sanctum/start")), 5
			);

			StructurePoolBasedGenerator.generate(
					registry, pool, PoolStructurePiece::new, generator, manager, new BlockPos(pos.x << 4, l, pos.z << 4), this, this.random, false, false, view
			);

			this.setBoundingBoxFromChildren();

		}
	}
}