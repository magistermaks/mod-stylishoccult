package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class NetherGrassFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    private final static int RADIUS = 7;
    private final static int HEIGHT = 3;
    private final static BlockState GRASS = ModBlocks.NETHER_GRASS.getDefaultState();
    private final static BlockState FERN = ModBlocks.NETHER_FERN.getDefaultState();

    public NetherGrassFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() )
                .decorate( Decorator.COUNT_MULTILAYER.configure(
                        new CountConfig(3)
                ) );
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        if( world.getBlockState( pos.down() ).getBlock() == Blocks.NETHERRACK ) {

            if( pos.getY() > 1 && pos.getY() < 255 ) {

                int count = 0, g = RADIUS * RADIUS;

                for(int m = 0; m < g; m ++) {

                    BlockPos target = pos.add(
                            random.nextInt(RADIUS) - random.nextInt(RADIUS),
                            random.nextInt(HEIGHT) - random.nextInt(HEIGHT),
                            random.nextInt(RADIUS) - random.nextInt(RADIUS)
                    );

                    if( world.isAir(target) && target.getY() > 0 && GRASS.canPlaceAt(world, target) ) {
                        world.setBlockState(target, random.nextInt(16) == 0 ? FERN : GRASS, 2);
                        count ++;
                    }

                }

                return count > 0;
            }

        }

        return false;
    }

}
