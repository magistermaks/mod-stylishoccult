package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.SparkVentBlock;
import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.entities.SporeEntity;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Voxels;
import net.minecraft.block.*;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class VentBlock extends SimpleBlock implements FoliageFleshBlock, ImpureBlock {

    public static DirectionProperty FACING = Properties.FACING;
    public static BooleanProperty ACTIVE = SparkVentBlock.ACTIVE;

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
        setDefaultState( getDefaultState().with(FACING, Direction.UP).with(ACTIVE, false) );
    }

    public BlockState getStateToFit( World world, BlockPos pos ) {
        for( Direction direction : Direction.values() ) {
            if( world.getBlockState( pos.offset(direction) ).getBlock() instanceof FullFleshBlock ) {
                return getDefaultState().with(FACING, direction.getOpposite());
            }
        }

        throw new RuntimeException("Unable to fit the block!");
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if( state.get(FACING).getOpposite() == direction ) {
            if( !(world.getBlockState(pos.offset(direction)).getBlock() instanceof FullFleshBlock) ) {
                return Blocks.AIR.getDefaultState();
            }
        }

        return state;
    }

    private void summon(BlockState state, World world, BlockPos pos ) {

        // TODO: add custom sound
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ROTATE_ITEM, SoundCategory.BLOCKS, 1, 1);

        SporeEntity sporeEntity = ModEntities.SPORE.create(world);

        if( sporeEntity == null ){
            throw new RuntimeException( "Unable to summon Spore!" );
        }

        sporeEntity.setVentDirection( state.get(FACING), 0.9f );
        sporeEntity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        sporeEntity.initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.REINFORCEMENT, null, null);
        world.spawnEntity(sporeEntity);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        Direction facing = ctx.getPlacementDirections()[0];

        if( ctx.getWorld().getBlockState(ctx.getBlockPos().offset(facing)).getBlock() instanceof FullFleshBlock ) {
            return getDefaultState().with(FACING, facing.getOpposite());
        }

        try {
            return getStateToFit(ctx.getWorld(), ctx.getBlockPos());
        }catch (RuntimeException ignored) {
            return null;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( !world.getBlockTickScheduler().isScheduled(pos, this) ) {
            world.getBlockTickScheduler().schedule(pos, this, 100);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(ACTIVE, true));

        summon(state, world, pos);

        if( random.nextInt( 5 ) == 0 ) {
            world.setBlockState(pos, state.with(ACTIVE, false));
            world.getBlockTickScheduler().schedule(pos, this, random.nextInt(450) + 250);
        }else{
            world.getBlockTickScheduler().schedule(pos, this, 20);
        }
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
        builder.add(FACING, ACTIVE);
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 10 * (state.get(ACTIVE) ? 2 : 1);
    }

}
