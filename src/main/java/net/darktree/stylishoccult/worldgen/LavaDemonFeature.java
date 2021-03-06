package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.LavaDemonBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.enums.LavaDemonPart;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
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

public class LavaDemonFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    public LavaDemonFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        Direction direction = Direction.Type.HORIZONTAL.random( random );
        BlockPos pos2 = pos.offset( direction );

        if( (world.getBlockState( pos2 ).getBlock() == Blocks.STONE) && pos2.getY() > 10 ) {
            world.setBlockState(
                    pos2,
                    ModBlocks.LAVA_DEMON.getDefaultState().with(LavaDemonBlock.PART, LavaDemonPart.HEAD),
                    3);

            StylishOccult.debug( "Lava Demon generated at: " + BlockUtils.posToString( pos2 ) + " (offset: " + direction.toString() + ")" );
        }

        return true;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() ).decorate( Decorator.CARVING_MASK.configure(
                new CarvingMaskDecoratorConfig(
                        GenerationStep.Carver.AIR,
                        0.0005f
                ) ) );
    }

}
