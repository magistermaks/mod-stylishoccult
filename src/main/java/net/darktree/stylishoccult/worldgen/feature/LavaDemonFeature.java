package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.Random;

// FIXME broken
public class LavaDemonFeature extends SimpleFeature<DefaultFeatureConfig> {

	public LavaDemonFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

		if( !RandUtils.getBool(StylishOccult.SETTING.demon_chance, random) ) {
			return false;
		}

		Direction direction = Direction.Type.HORIZONTAL.random( random );
		BlockPos pos2 = pos.offset( direction );

		if( (world.getBlockState( pos2 ).getBlock() == Blocks.STONE) && pos2.getY() > 10 ) {
			world.setBlockState(pos2, ModBlocks.LAVA_DEMON.getDefaultState().with(LavaDemonBlock.PART, LavaDemonPart.HEAD), 3);
			this.debugWrite(pos2);
		}

		return true;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return new ConfiguredFeature<>(this, new DefaultFeatureConfig());
	}

	@Override
	public PlacedFeature placed(RegistryEntry<ConfiguredFeature<?, ?>> configured) {
		return new PlacedFeature(
				configured,
				Arrays.asList(
						CountPlacementModifier.of(1),
						SquarePlacementModifier.of()
				)
		);
	}

}
