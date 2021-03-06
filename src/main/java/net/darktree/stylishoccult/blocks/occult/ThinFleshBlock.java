package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ThinFleshBlock extends SimpleBlock implements ImpureBlock, FluidReplaceable {

    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final IntProperty SIZE = IntProperty.of("size", 1, 3);

    public static final VoxelShape[] SHAPES = {
            Utils.box(  0, 16,  0, 16, 15, 16 ), // UP
            Utils.box(  0,  0,  0, 16,  1, 16 ), // DOWN
            Utils.box(  0,  0, 15, 16, 16, 16 ), // SOUTH
            Utils.box(  0,  0,  0, 16, 16,  1 ), // NORTH
            Utils.box(  0,  0,  0,  1, 16, 16 ), // WEST
            Utils.box( 15,  0,  0, 16, 16, 16 )  // EAST
    };

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.GROWTH;
    }

    public ThinFleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).slipperiness(0.7f).ticksRandomly() );
        setDefaultState( getDefaultState().with(SIZE, 1).with(UP, false).with(DOWN, false).with(SOUTH, false).with(NORTH, false).with(WEST, false).with(EAST, false) );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, SOUTH, NORTH, WEST, EAST, SIZE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        int bitfield = getBitfield(state);

        if( (bitfield & 0b100000) != 0 ) shape = VoxelShapes.combine( SHAPES[0], shape, BooleanBiFunction.OR );
        if( (bitfield & 0b010000) != 0 ) shape = VoxelShapes.combine( SHAPES[1], shape, BooleanBiFunction.OR );
        if( (bitfield & 0b001000) != 0 ) shape = VoxelShapes.combine( SHAPES[2], shape, BooleanBiFunction.OR );
        if( (bitfield & 0b000100) != 0 ) shape = VoxelShapes.combine( SHAPES[3], shape, BooleanBiFunction.OR );
        if( (bitfield & 0b000010) != 0 ) shape = VoxelShapes.combine( SHAPES[4], shape, BooleanBiFunction.OR );
        if( (bitfield & 0b000001) != 0 ) shape = VoxelShapes.combine( SHAPES[5], shape, BooleanBiFunction.OR );

        return shape;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        BooleanProperty property = fromDirection(direction);
        if( state.get( property ) && !canSupport((World) world, pos, direction) ) {
            state = state.with(property, false);
            world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);
        }

        if( getBitfield(state) == 0b000000 ) {
            return Blocks.AIR.getDefaultState();
        }else{
            return state;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateToFit( ctx.getWorld(), ctx.getBlockPos() );
    }

    public BlockState getStateToFit( World world, BlockPos pos ) {
        BlockState state = getDefaultState();
        boolean flag = false;

        for( Direction side : Direction.values() ) {
            if( canSupport( world, pos, side ) ) {
                state = state.with(fromDirection(side), true);
                flag = true;
            }
        }

        return flag ? state : null;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = Integer.bitCount( getBitfield(state) );

        if( count == 0 ) {
            StylishOccult.LOGGER.warn("Empty growth block was found and removed!");
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }

        if( OccultHelper.touchesSource( world, pos ) ) {
            if( BlockUtils.countInArea(world, pos, FoliageFleshBlock.class, 3) != 0 ) {
                return;
            }else{
                if( RandUtils.getBool(10) ) {
                    return;
                }
            }
        }

        if( count != 6 ) {
            if( random.nextInt( count + 1 ) == 0 ) {
                applyRandom( state, pos, world, random );
            }

            BlockPos target = pos.offset( RandUtils.getEnum(Direction.class, random) );

            if( random.nextInt( 8 ) == 0 ) {
                target = target.offset( RandUtils.getEnum(Direction.class, random) );
            }

            BlockState targetState = world.getBlockState( target );

            try {
                if( targetState.getBlock() != this ) {
                    if (targetState.isAir() || targetState.getMaterial().isReplaceable() || targetState.canReplace(null)) {
                        applyRandom(getDefaultState(), target, world, random);
                        return;
                    }
                }
            } catch(Exception ignore) {}
        }

        int size = state.get(SIZE);
        if( size < 3 && random.nextInt( 64 - count * 4 ) == 0 ) {
            world.setBlockState(pos, state.with(SIZE, size + 1));
            return;
        }

        if( size == 3 && random.nextInt( 128 - count * 5 ) == 0 ) {
            OccultHelper.corruptAround(world, pos, random);
        }

    }

    private void applyRandom( BlockState state, BlockPos pos, ServerWorld world, Random random ) {
        Direction side = RandUtils.getEnum(Direction.class, random);
        BlockState target = state.with(fromDirection(side), true);

        if( canSupport( world, pos, side ) ) {
            world.setBlockState( pos, target );
        }
    }

    public static int getBitfield(BlockState state) {
        int map = 0b000000;

        if( state.get(UP)    ) map |= 0b100000;
        if( state.get(DOWN)  ) map |= 0b010000;
        if( state.get(SOUTH) ) map |= 0b001000;
        if( state.get(NORTH) ) map |= 0b000100;
        if( state.get(WEST)  ) map |= 0b000010;
        if( state.get(EAST)  ) map |= 0b000001;

        return map;
    }

    private BooleanProperty fromDirection( Direction direction ) {
        switch( direction ) {
            case UP: return UP;
            case DOWN: return DOWN;
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case WEST: return WEST;
            case EAST: return EAST;
        }

        return null;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isEmpty() || context.getStack().getItem() != this.asItem();
    }

    private boolean canSupport(World world, BlockPos pos, Direction direction ) {
        BlockPos support = pos.offset(direction);
        BlockState state = world.getBlockState( support );
        return state.isFullCube(world, support) || isShapeFullCube(state.getOutlineShape(world, support)) || state.isSideSolidFullSquare( world, support, direction.getOpposite() );
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        int count = Integer.bitCount(getBitfield(state));
        world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);

        if( count <= 1 ) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }else{
            for( Direction side : Direction.values() ) {
                BooleanProperty property = fromDirection(side);
                if( state.get(property) ) {
                    world.setBlockState(pos, state.with(property, false));
                    return;
                }
            }
        }
    }

    @Override
    public int impurityLevel(BlockState state) {
        return (Integer.bitCount(getBitfield(state)) + state.get(SIZE)) * 3;
    }

}
