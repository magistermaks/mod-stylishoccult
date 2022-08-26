package net.darktree.stylishoccult.worldgen.structure;

import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.function.Predicate;

public class SanctumStructure extends JigsawFeature {
	private final int depth;
	private final int clearance;

	public SanctumStructure(int depth, int clearance) {
		super(StructurePoolFeatureConfig.CODEC, 0, false, false, context -> true);
		this.depth = depth;
		this.clearance = clearance;
	}

	@Override
	public boolean canGenerate(DynamicRegistryManager registryManager, ChunkGenerator generator, BiomeSource biomeSource, StructureManager structureManager, long worldSeed, ChunkPos pos, StructurePoolFeatureConfig config, HeightLimitView view, Predicate<RegistryEntry<Biome>> biomePredicate) {
		return this.getPlacementHeight(generator, pos, view) != Integer.MIN_VALUE;
	}

	private int getPlacementHeight(ChunkGenerator generator, ChunkPos pos, HeightLimitView view) {
		VerticalBlockSample sample = generator.getColumnSample(pos.getCenterX(), pos.getCenterZ(), view);

		boolean air = false;

		for (int i = view.getTopY(); i > view.getBottomY(); i --) {
			if (!sample.getState(i).isAir()) {
				if (air && verifyPlacementPosition(generator, view, pos, i)) {
					return i;
				}

				air = false;
			}else{
				air = true;
			}
		}

		return Integer.MIN_VALUE;
	}

	public boolean verifyPlacementPosition(ChunkGenerator generator, HeightLimitView view, ChunkPos pos, int y) {
		VerticalBlockSample sample = generator.getColumnSample(pos.getStartX(),  pos.getStartZ(), view);

		for (int i = 0; i < this.clearance; i ++) {
			if (!sample.getState(y + i).isAir()) {
				return false;
			}
		}

		boolean hit = false;

		for (int i = 0; i < this.depth; i ++) {
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

//	public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {
//		public Start(StructureFeature<DefaultFeatureConfig> s, ChunkPos c, int i, long l) {
//			super(s, c, i, l);
//		}
//
//		@Override
//		public void init(DynamicRegistryManager registry, ChunkGenerator generator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView view) {
//
//			SanctumStructure feature = (SanctumStructure) this.getFeature();
//			int l = feature.getPlacementHeight(generator, pos, view);
//
//			if (l == Integer.MIN_VALUE) {
//				return;
//			}
//
//			BlockPos target = new BlockPos(pos.x * 16 + 8, l, pos.z * 16 + 8);
//			StructurePoolFeatureConfig pool = WorldGen.getPool(registry, "sanctum/start", StylishOccult.SETTING.sanctum.depth);
//			StructurePoolBasedGenerator.generate(registry, pool, PoolStructurePiece::new, generator, manager, target, this, this.random, false, false, view);
//
//			// center the structure on the chunk center
//			Vec3i center = this.children.get(0).getBoundingBox().getCenter();
//			int xOffset = target.getX() - center.getX();
//			int zOffset = target.getZ() - center.getZ();
//
//			for(StructurePiece piece : this.children){
//				piece.translate(xOffset, 0, zOffset);
//			}
//
//			this.setBoundingBoxFromChildren();
//		}
//	}
}