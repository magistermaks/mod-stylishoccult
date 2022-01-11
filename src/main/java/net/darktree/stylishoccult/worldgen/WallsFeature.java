package net.darktree.stylishoccult.worldgen;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.tags.ModTags;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallsFeature extends Feature<DefaultFeatureConfig> implements SimpleFeature {

    private static final Direction[] neighbors = new Direction[] {
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH
    };

    private static ArrayList<BlockState> BLOCKS = null;

    public WallsFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        // init block collection
        if( BLOCKS == null ) {
            BLOCKS = new ArrayList<>();
            List<Block> runes = ModTags.RUNES.values();

            for( Block rune : runes ) {
                BLOCKS.add(rune.getDefaultState().with(RuneBlock.FROZEN, true));
                BLOCKS.add(ModBlocks.RUNESTONE.getDefaultState());
                BLOCKS.add(Blocks.BLACKSTONE.getDefaultState());
                BLOCKS.add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState());
                BLOCKS.add(Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState());
            }
        }

        BlockPos target = pos.down();

        if( RandUtils.getBool(2, random) && world.getBlockState( target ).isSolidBlock( world, target ) ) {
            generateWall( getAxis(random), world, target, RandUtils.rangeInt(2, 5, random), (float) RandUtils.rangeInt(80, 90, random), random );
            scatterUrns(world, target, random);

            StylishOccult.debug( "Runic wall generated at: " + BlockUtils.posToString( target ) );
        }

        return false;
    }

    private boolean generateColumn( StructureWorldAccess world, BlockPos origin, int height, Random random ) {

        if( generateFoundation(world, origin, height, random) ) {

            BlockPos.Mutable pos = origin.mutableCopy();

            for( int i = 0; i <= height; i ++ ) {
                pos.move( Direction.UP );
                generateRune(world, pos, random);
            }

            return true;
        }

        return false;
    }

    private void generateWall( Direction.Axis axis, StructureWorldAccess world, BlockPos pos, int height, float chance, Random random ) {

        generateColumn( world, pos, height, random );

        Direction.Axis child = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        boolean hasChild = true;

        int ah = height, ad = 0;
        while ( RandUtils.getBool(chance, random) ) {
            ah += random.nextInt(2) - 2;
            ad --;

            if( hasChild && RandUtils.getBool( 25.0f, random ) ) {
                generateWall( child, world, pos.offset( axis, ad ), ah + 1, chance, random );
                hasChild = false;
            }

            if( !generateColumn( world, pos.offset( axis, ad ), ah, random ) ) break;
        }

        int bh = height, bd = 0;
        while ( RandUtils.getBool(chance, random) ) {
            bh += random.nextInt(2) - 2;
            bd ++;

            if( hasChild && RandUtils.getBool( 25.0f, random ) ) {
                generateWall( child, world, pos.offset( axis, bd ), bh + 1, chance, random );
                hasChild = false;
            }

            if( !generateColumn( world, pos.offset( axis, bd ), bh, random ) ) break;
        }

    }

    private boolean generateFoundation( StructureWorldAccess world, BlockPos origin, int height, Random random ) {

        BlockPos.Mutable pos = origin.mutableCopy();

        for( int i = 0; i <= height; i ++ ) {

            if( world.getBlockState(pos).isSolidBlock(world, pos) ) {
                return true;
            }else{
                generateRune(world, pos, random);
            }

            pos.move( Direction.DOWN );
        }

        return false;
    }

    private void scatterUrns(StructureWorldAccess world, BlockPos target, Random random) {

        final int minX = RandUtils.rangeInt(-5, -2, random);
        final int minY = RandUtils.rangeInt(-4, -2, random);
        final int minZ = RandUtils.rangeInt(-5, -2, random);
        final int maxX = RandUtils.rangeInt( 2,  5, random);
        final int maxY = RandUtils.rangeInt( 2,  4, random);
        final int maxZ = RandUtils.rangeInt( 2,  5, random);

        int slots = RandUtils.rangeInt( 2, 4, random);

        for( int x = minX; x <= maxX; x ++ ) {
            for( int z = minZ; z <= maxZ; z ++ ) {
                for( int y = minY; y <= maxY; y ++ ) {
                    if( RandUtils.getBool(33, random) ) {
                        BlockPos pos = target.west(z).north(x).down(y);
                        BlockPos surface = pos.down();

                        if (world.getBlockState(surface).isSolidBlock(world, surface)) {
                            if (world.getBlockState(pos).isAir() && touchesRunes(world, pos)) {
                                world.setBlockState(pos, ModBlocks.URN.getDefaultState(), 3);
                                slots --;
                            }
                        }

                        if( slots <= 0 ) return;
                    }
                }
            }
        }

    }

    public static boolean touchesRunes(BlockView world, BlockPos origin) {
        for( Direction direction : neighbors ){
            if(world.getBlockState( origin.offset( direction ) ).getBlock() instanceof RuneBlock) return true;
        }

        return false;
    }

    private void generateRune( StructureWorldAccess world, BlockPos pos, Random random ) {
        world.setBlockState( pos, RandUtils.getListEntry(BLOCKS, random), 3 );
    }

    private Direction.Axis getAxis( Random random ) {
        return RandUtils.getBool( 50f, random ) ? Direction.Axis.X : Direction.Axis.Z;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() )
                .decorate( Decorator.COUNT_MULTILAYER.configure(
                        new CountConfig(1)
                ) );
    }

}
