package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
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

public class UrnFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    public UrnFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        if( world.getBlockState(pos).isAir() && pos.getY() > 6 ) {

            BlockPos pos2 = pos.down();
            BlockPos pos3 = pos.offset( Direction.fromHorizontal( random.nextInt() ) );

            if( world.getBlockState( pos2 ).isSolidBlock(world, pos2) && world.getBlockState( pos3 ).isSolidBlock(world, pos3) ) {
                world.setBlockState(pos, ModBlocks.URN.getDefaultState(), 3);
                this.debugWrite(pos);
            }

        }

        return true;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() ).decorate( Decorator.CARVING_MASK.configure(
                new CarvingMaskDecoratorConfig(
                        GenerationStep.Carver.AIR,
                        0.00092f
                ) ) );
    }

}
