package net.darktree.stylishoccult.worldgen.feature;

import com.mojang.serialization.Codec;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Random;

public class UrnFeature extends SimpleFeature<DefaultFeatureConfig> {

    public UrnFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {

        if (world.getBlockState(pos).isAir() && pos.getY() > 6 && pos.getY() < 30) {

            BlockPos pos2 = pos.down();
            BlockPos pos3 = pos.offset( Direction.fromHorizontal( random.nextInt() ) );

            if (isFloorAccepted(world.getBlockState(pos2).getBlock()) && world.getBlockState(pos3).isSolidBlock(world, pos3)) {
                world.setBlockState(pos, ModBlocks.URN.getDefaultState(), 3);
                this.debugWrite(pos);
            }

        }

        return true;
    }

    private boolean isFloorAccepted(Block block) {
        // the last one is for mineshaft
        return block == Blocks.STONE || block == Blocks.DEEPSLATE || block == Blocks.ANDESITE || block == Blocks.GRANITE || block == Blocks.OAK_PLANKS;
    }

    @Override
    public ConfiguredFeature<?, ?> configure() {
        return configure( new DefaultFeatureConfig() )
                .decorate( Decorator.COUNT_MULTILAYER.configure(
                        new CountConfig(1)
                ) );
    }

}
