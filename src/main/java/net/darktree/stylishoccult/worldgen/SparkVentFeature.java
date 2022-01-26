package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.SparkVentBlock;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.CarvingMaskDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class SparkVentFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    private final static BlockState VENT = ModBlocks.SPARK_VENT.getDefaultState();
    private final static BlockState LAVA = Blocks.LAVA.getDefaultState();

    public SparkVentFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        if( !world.getBlockState(pos).isAir() ) {
            return false;
        }

        BlockPos vent = pos.down();
        if (world.getBlockState(vent).getBlock() == Blocks.NETHERRACK) {

            BlockPos source = vent.down();
            if (world.getBlockState(source).getBlock() == Blocks.NETHERRACK) {

                if( BlockUtils.touchesAir(world, source) ) {
                    return false;
                }

                BlockPos floor = source.down();
                if (world.getBlockState(floor).getBlock() == Blocks.NETHERRACK) {

                    boolean hasSource = generateSource(world, random, source);
                    world.setBlockState(vent, VENT.with(SparkVentBlock.ACTIVE, hasSource), 2);
                    StylishOccult.debug("Generated Spark Vent at: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());

                    return true;

                }
            }
        }

        return false;
    }

    public boolean generateSource(StructureWorldAccess world, Random random, BlockPos pos) {
        if( RandUtils.getBool(95.0f) ) {
            world.setBlockState(pos, LAVA, 2);

            for( int i = random.nextInt(9); i > 0; i -- ) {
                Direction dir = RandUtils.getEnum(Direction.class);

                if( dir != Direction.UP ) {

                    BlockPos tmp = pos.offset(dir);

                    if( !BlockUtils.touchesAir(world, tmp) ) {

                        Block block = world.getBlockState(tmp).getBlock();
                        if( block == Blocks.NETHERRACK || block == Blocks.LAVA ) {
                            world.setBlockState(tmp, LAVA, 2);
                            pos = tmp;
                        }

                    } else {
                        break;
                    }

                }
            }

            return true;
        }

        return false;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() ).decorate( Decorator.CARVING_MASK.configure(
                new CarvingMaskDecoratorConfig(
                        GenerationStep.Carver.AIR,
                        0.0008f
                ) ) );
    }

}
