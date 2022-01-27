package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BoulderFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

	private final BlockState[] BLOCKS = {
			Blocks.BLACKSTONE.getDefaultState(),
			Blocks.GILDED_BLACKSTONE.getDefaultState(),
			ModBlocks.CRYSTALLINE_BLACKSTONE.getDefaultState(),
			Blocks.MAGMA_BLOCK.getDefaultState()
	};

	public BoulderFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos target, DefaultFeatureConfig config) {
		float radius = random.nextFloat() * 2 + 1;
		int erosion = random.nextInt(2);
		int cx = target.getX(), cy = target.getY() - erosion - 1, cz = target.getZ();

		BlockPos.Mutable pos = target.mutableCopy();
		int extend = MathHelper.ceil(radius);

		if( !RandUtils.getBool(StylishOccult.SETTINGS.featureBoulderChance, random) ) {
			return false;
		}

		for(int x = -extend; x < extend; x ++) {
			for(int y = -extend; y < extend; y ++) {
				for(int z = -extend; z < extend; z ++) {
					pos.set(cx + x, cy + y, cz + z);

					float distance = MathHelper.sqrt(x * x + y * y + z * z);

					if(distance < radius) {
						generateBlock(world, pos, random, distance + 1 > radius, erosion);
					}
				}
			}
		}

		this.debugWrite(target);
		return false;
	}

	private void generateBlock(StructureWorldAccess world, BlockPos.Mutable pos, Random random, boolean edge, int erosion) {
		if(edge) {
			if( random.nextInt(4 - erosion) != 0 ) {
				world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState(), 3);
			}
		} else {
			world.setBlockState(pos, RandUtils.getArrayEntry(BLOCKS, random), 3);
		}
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return configure( new DefaultFeatureConfig() )
				.decorate( Decorator.COUNT_MULTILAYER.configure(
						new CountConfig(1)
				) );
	}

}
