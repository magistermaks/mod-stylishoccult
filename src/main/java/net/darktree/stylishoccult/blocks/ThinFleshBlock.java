package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Properties;
import java.util.Random;

public class ThinFleshBlock extends SimpleBlock {

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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = Integer.bitCount( getBitfield(state) );

        if( count == 0 ) {
            StylishOccult.LOGGER.warn("Empty growth block was found and removed!");
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }

        if( count != 6 ) {

            if( random.nextInt( count + 2 ) == 0 ) {
                applyRandom( state, pos, world, random );
                return;
            }

            if( random.nextInt( count + 1 ) == 0 ) {

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
        }

        int size = state.get(SIZE);
        if( size < 3 && random.nextInt( 128 - count * 4 ) == 0 ) {
            world.setBlockState(pos, state.with(SIZE, size + 1));
//            return;
        }

//        if( size == 3 && random.nextInt( 128 - count * 4 ) == 0 ) {
//            //TODO
//        }

    }

    private void applyRandom( BlockState state, BlockPos pos, ServerWorld world, Random random ) {
        Direction side = RandUtils.getEnum(Direction.class, random);
        BlockState target = state.with(fromDirection(side), true);
        BlockPos support = pos.offset(side);

        if( world.getBlockState( support ).isSideSolidFullSquare(world, support, side.getOpposite()) ) {
            world.setBlockState( pos, target );
        }
    }

    private int getBitfield(BlockState state) {
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
}
