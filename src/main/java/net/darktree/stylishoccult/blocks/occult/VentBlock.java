package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class VentBlock extends SimpleBlock implements FoliageFleshBlock, ImpureBlock {

    public static DirectionProperty FACING = Properties.FACING;

    public static VoxelShape[] SHAPES = {
            Voxels.box(3, 16, 3, 13, 15, 13).box(4, 15, 4, 12, 14, 12).build(),
            Voxels.box(3, 0, 3, 13, 1, 13).box(4, 1, 4, 12, 2, 12).build(),
            Voxels.box(3, 3, 16, 13, 13, 15).box(4, 4, 15, 12, 12, 14).build(),
            Voxels.box(3, 3, 0, 13, 13, 1).box(4, 4, 1, 12, 12, 2).build(),
            Voxels.box(16, 3, 3, 15, 13, 13).box(15, 4, 4, 14, 12, 12).build(),
            Voxels.box(0, 3, 3, 1, 13, 13).box(1, 4, 4, 2, 12, 12).build()
    };

    public static Vector3f[] OFFSETS = {
            new Vector3f(8, 13, 8),
            new Vector3f(8, 3, 8),
            new Vector3f(8, 8, 13),
            new Vector3f(8, 8, 3),
            new Vector3f(13, 8, 8),
            new Vector3f(3, 8, 8)
    };

    public VentBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.STONE, 2, 2, false ).ticksRandomly() );
        setDefaultState( getDefaultState().with(FACING, Direction.UP) );
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        Vector3f offset = OFFSETS[state.get(FACING).getId()];
        double x = offset.getX() / 16.0f, y = offset.getY() / 16.0f, z = offset.getZ() / 16.0f;

        double d = (double)pos.getX() + x;
        double e = (double)pos.getY() + y;
        double f = (double)pos.getZ() + z;
        world.addParticle( ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(FACING).getId()];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 10;
    }
}
