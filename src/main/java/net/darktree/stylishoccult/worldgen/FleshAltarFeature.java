package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class FleshAltarFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    public FleshAltarFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        BlockPos origin = pos.down();

        if( !world.getBlockState(pos).isAir() ) return false;

        if( RandUtils.getBool(0.1f, random) && generateFlesh(world, origin, random) ) {
            generatePillars(world, origin, random);

            StylishOccult.debug( "Altar generated at: " + BlockUtils.posToString( origin ) );
        }

        return true;
    }

    private boolean generateFlesh( StructureWorldAccess world, BlockPos origin, Random random ) {
        return generateFlesh(world, origin, random, RandUtils.rangeInt(5, 7, random));
    }

    private boolean generateFlesh( StructureWorldAccess world, BlockPos pos, Random random, int size ) {
        if( world.getBlockState(pos).isSolidBlock(world, pos) ) {
            world.setBlockState( pos, ModBlocks.DEFAULT_FLESH.getDefaultState(), 3 );

            if( size > 0 ) {
                generateFlesh(world, pos.offset(RandUtils.getEnum(Direction.class, random)), random, size - 1);
                generateFlesh(world, pos.offset(RandUtils.getEnum(Direction.class, random)), random, size - 1);
            }

            return true;
        }

        return false;
    }

    private void generatePillars( StructureWorldAccess world, BlockPos pos, Random random ) {
        generatePillar(world, pos, random, -3, 1 );
        generatePillar(world, pos, random, -3, -1 );
        generatePillar(world, pos, random, 3, 1 );
        generatePillar(world, pos, random, 3, -1 );
        generatePillar(world, pos, random, 1, 3 );
        generatePillar(world, pos, random, -1, 3 );
        generatePillar(world, pos, random, 1, -3 );
        generatePillar(world, pos, random, -1, -3 );
    }

    private void generatePillar( StructureWorldAccess world, BlockPos origin, Random random, int x, int z ) {
        if(RandUtils.getBool(99f, random)) {
            BlockPos.Mutable pos = origin.north(x).west(z).down( RandUtils.rangeInt(1, 2) ).mutableCopy();
            int height = RandUtils.rangeInt(1, 3);

            if( world.getBlockState(pos).isSolidBlock(world, pos) ) {
                for (int i = 0; i <= height; i++) {
                    world.setBlockState( pos, Blocks.OBSIDIAN.getDefaultState(), 3 );
                    pos.move(Direction.UP);
                }

                if( RandUtils.getBool(24.2f, random) ) {
                    if( world.getBlockState(pos).isAir() ) {
                        world.setBlockState(pos, ModBlocks.FIERY_LANTERN.getDefaultState(), 3);
                    }
                }
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
