package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.tag.ModTags;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Random;

public class BoulderFeature extends SimpleFeature<DefaultFeatureConfig> {

	public BoulderFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos target, DefaultFeatureConfig config) {
		float radius = random.nextFloat() * StylishOccult.SETTINGS.featureBoulderRadiusBase + 1;
		int erosion = random.nextInt(2);
		int cx = target.getX(), cy = target.getY() - erosion - 2, cz = target.getZ();

		BlockPos.Mutable pos = target.mutableCopy();
		int extend = MathHelper.ceil(radius);

		if (!RandUtils.getBool(StylishOccult.SETTINGS.featureBoulderChance, random)) {
			return false;
		}

		boolean fire = RandUtils.getBool(StylishOccult.SETTINGS.featureBoulderFireChance, random);

		for (int x = -extend; x < extend; x ++) {
			for (int y = -extend; y < extend; y ++) {
				for (int z = -extend; z < extend; z ++) {
					pos.set(cx + x, cy + y, cz + z);

					float distance = MathHelper.sqrt(x * x + y * y + z * z);

					if (distance < radius) {
						generateBlock(world, pos, random, distance + 1 > radius, erosion, fire);
					}
				}
			}
		}

		this.debugWrite(target);
		return false;
	}

	private void generateBlock(StructureWorldAccess world, BlockPos.Mutable pos, Random random, boolean edge, int erosion, boolean fire) {
		if (edge) {
			if (random.nextInt(4 - erosion) != 0) {
				world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState(), 3);
			}
		} else {
			if (RandUtils.getBool(StylishOccult.SETTINGS.featureBoulderBlackstoneChance, random)) {
				world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState(), 3);
			} else {
				world.setBlockState(pos, ModTags.BOULDER_FILLER.getRandom(random).getDefaultState(), 3);
			}
		}

		if (fire && random.nextBoolean()) {
			BlockPos target = pos.offset(RandUtils.getEnum(Direction.class, random));

			if(world.getBlockState(target.down()).getBlock() == Blocks.NETHERRACK && world.getBlockState(target).isAir()) {
				world.setBlockState(target, Blocks.FIRE.getDefaultState(), 3);
			}
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
