package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.tag.ModTags;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WallsFeature extends SimpleFeature<DefaultFeatureConfig> {

    private static final Direction[] neighbors = new Direction[] {
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH
    };

    private static final BlockState[] BLOCKS = {
            ModBlocks.RUNESTONE.getDefaultState(),
            Blocks.BLACKSTONE.getDefaultState(),
            Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState(),
            Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState()
    };

    public WallsFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos target = pos.down();

        if (RandUtils.getBool(StylishOccult.SETTING.wall_chance, random) && world.getBlockState(target).isSolidBlock(world, target)) {
            generateWall( getAxis(random), world, target, RandUtils.rangeInt(2, 5, random), (float) RandUtils.rangeInt(83, 90, random), random );
            scatterUrns(world, target, random);
            this.debugWrite(target);
        }

        return false;
    }

    private boolean generateColumn( StructureWorldAccess world, BlockPos origin, int height, Random random ) {

        if (generateFoundation(world, origin, height, random)) {
            BlockPos.Mutable pos = origin.mutableCopy();

            for (int i = 0; i <= height; i ++) {
                pos.move(Direction.UP);
                generateRune(world, pos, random);
            }

            return true;
        }

        return false;
    }

    private void generateWall( Direction.Axis axis, StructureWorldAccess world, BlockPos pos, int height, float chance, Random random ) {
        generateColumn(world, pos, height, random);

        Direction.Axis child = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        boolean hasChild = true;

        int ah = height, ad = 0;
        while (RandUtils.getBool(chance, random)) {
            ah += random.nextInt(2) - 2;
            ad --;

            if (hasChild && RandUtils.getBool(25.0f, random)) {
                generateWall(child, world, pos.offset(axis, ad), ah + 1, chance, random );
                hasChild = false;
            }

            if (!generateColumn(world, pos.offset(axis, ad), ah, random)) break;
        }

        int bh = height, bd = 0;
        while (RandUtils.getBool(chance, random)) {
            bh += random.nextInt(2) - 2;
            bd ++;

            if (hasChild && RandUtils.getBool(25.0f, random)) {
                generateWall(child, world, pos.offset(axis, bd), bh + 1, chance, random );
                hasChild = false;
            }

            if (!generateColumn(world, pos.offset(axis, bd), bh, random)) break;
        }

    }

    private boolean generateFoundation(StructureWorldAccess world, BlockPos origin, int height, Random random) {

        BlockPos.Mutable pos = origin.mutableCopy();

        for (int i = 0; i <= height; i ++) {

            if (world.getBlockState(pos).isSolidBlock(world, pos)) {
                return true;
            } else {
                generateRune(world, pos, random);
            }

            pos.move(Direction.DOWN);
        }

        return false;
    }

    private void scatterUrns(StructureWorldAccess world, BlockPos target, Random random) {

        final int minX = -5;
        final int minY = -4;
        final int minZ = -5;
        final int maxX =  5;
        final int maxY =  4;
        final int maxZ =  5;

        ArrayList<BlockPos> qualified = new ArrayList<>();
        int cx = target.getX(), cy = target.getY(), cz = target.getZ();
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int x = minX; x <= maxX; x ++) {
            for (int z = minZ; z <= maxZ; z ++) {
                for (int y = minY; y <= maxY; y ++) {
                    pos.set(cx + x, cy + y, cz + z);

                    if (world.getBlockState(pos).isSolidBlock(world, pos)) {
                        pos.move(0, 1, 0);

                        if (world.getBlockState(pos).isAir() && touchesRunes(world, pos)) {
                            qualified.add(pos.toImmutable());
                        }
                    }
                }
            }
        }

        // pick random candidates
        Collections.shuffle(qualified, random);
        int slots = RandUtils.rangeInt(0, 5, random);

        for (BlockPos position : qualified) {
            if (slots > 0) {
                placeRandomDecoration(world, position, random, cy);
                slots --;
            } else {
                break;
            }
        }

    }

    private void placeRandomDecoration(StructureWorldAccess world, BlockPos pos, Random random, int base) {
        int height = pos.getY() - base;

        if (height > 2 || random.nextInt(3) == 0) {
            world.setBlockState(pos, Blocks.CANDLE.getDefaultState()
                    .with(CandleBlock.CANDLES, random.nextInt(4) + 1)
                    .with(CandleBlock.LIT, random.nextInt(8) != 0), 3);
        } else {
            world.setBlockState(pos, ModBlocks.URN.getDefaultState(), 3);
        }
    }

    private static boolean touchesRunes(BlockView world, BlockPos origin) {
        for (Direction direction : neighbors){
            if(world.getBlockState( origin.offset( direction ) ).getBlock() instanceof RuneBlock) return true;
        }

        return false;
    }

    private void generateRune(StructureWorldAccess world, BlockPos pos, Random random) {
        if (RandUtils.getBool(StylishOccult.SETTING.wall_rune_chance, random)) {
            Block rune = ModTags.RUNES.getRandom(random);
            world.setBlockState(pos, rune.getDefaultState().with(RuneBlock.FROZEN, true), 3);
        } else {
            world.setBlockState(pos, RandUtils.getArrayEntry(BLOCKS, random), 3);
        }
    }

    private Direction.Axis getAxis(Random random) {
        return RandUtils.getBool(50f, random) ? Direction.Axis.X : Direction.Axis.Z;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() )
                .decorate( Decorator.COUNT_MULTILAYER.configure(
                        new CountConfig(1)
                ) );
    }

}
