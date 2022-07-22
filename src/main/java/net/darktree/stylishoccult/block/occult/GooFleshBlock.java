package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GooFleshBlock extends SimpleBlock implements ImpureBlock {

    public static final BooleanProperty TOP = BooleanProperty.of("top");
    private static final VoxelShape BOX = Utils.shape(0, 0, 0, 16, 15, 16);

    public GooFleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).noCollision().ticksRandomly());
        setDefaultState( getDefaultState().with(TOP, false) );
    }

    @Override
    public boolean canMobSpawnInside() {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(TOP) ? BOX : VoxelShapes.fullCube();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos().up();
        BlockState state = getDefaultState();

        return state.with(TOP, world.getBlockState(pos).isAir());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TOP);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if( direction == Direction.UP ) {
            return state.with(TOP, newState.isAir());
        }

        return state;

    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (StylishOccult.SETTING.infinite_flesh_growth) {
            BlockPos target = pos.offset(RandUtils.getEnum(Direction.class));
            BlockState targetState = world.getBlockState(target);

            if (RandUtils.getBool(5f, random) && targetState.getBlock() instanceof FluidBlock) {
                world.setBlockState(target, state.with(TOP, world.getBlockState(target.up()).isAir()));
            }
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.slowMovement( state, new Vec3d(0.95, 0.95, 0.95) );
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 12;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) && (direction.getAxis() == Direction.Axis.Y || (stateFrom.get(TOP) == state.get(TOP)));
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.SIMPLE;
    }

}
