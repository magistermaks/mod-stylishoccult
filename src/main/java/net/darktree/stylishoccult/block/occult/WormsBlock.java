package net.darktree.stylishoccult.block.occult;

import net.darktree.interference.api.FluidReplaceable;
import net.darktree.stylishoccult.block.BuildingBlock;
import net.darktree.stylishoccult.block.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class WormsBlock extends BuildingBlock implements ImpureBlock, FoliageFleshBlock, FluidReplaceable {

    private static final VoxelShape SHAPE = Utils.shape(2, 0, 2, 14, 10, 14);

    public WormsBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).noCollision().breakInstantly().ticksRandomly() );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (RandUtils.getBool(60, random) && BlockUtils.countInArea(world, pos, WormsBlock.class, 2) < 4) {

            BlockPos target = pos
                    .offset(RandUtils.getEnum(Direction.class, random))
                    .offset(RandUtils.getEnum(Direction.class, random))
                    .offset(RandUtils.getEnum(Direction.class, random));

            BlockState stateTarget = world.getBlockState(target);
            BlockState stateDown = world.getBlockState(target.down());

            if (stateDown.getBlock() instanceof FullFleshBlock && (stateTarget.isAir() || stateTarget.getMaterial().isReplaceable())) {
                world.setBlockState( target, getDefaultState() );
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 2;
    }

}
