package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.CandelabraBlockEntity;
import net.darktree.stylishoccult.enums.CandelabraType;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.items.CandelabraItem;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CandelabraBlock extends AbstractCandleHolderBlock {

    public static final EnumProperty<CandelabraType> TYPE = EnumProperty.of("type", CandelabraType.class);;
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape( 6, 0, 6, 10, 7, 10 );;

    public CandelabraBlock() {
        super(RegUtil.settings( Material.WOOD, BlockSoundGroup.WOOD, 0.1F, 1.0F, false ).lightLevel(15).ticksRandomly());
        this.setDefaultState( this.stateManager.getDefaultState()
                .with(LIT, false)
                .with(MATERIAL, CandleHolderMaterial.OAK)
                .with(TYPE, CandelabraType.FIVE));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, TYPE, MATERIAL);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if( facing == Direction.DOWN && !this.canPlaceAt(state, world, pos) ) {
            onBreak((World) world, pos, state, null);
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }else{
            return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return sideCoversSmallSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return BASE_SHAPE;
    }

    @Override
    public ItemStack getMainStack( BlockState state ) {
        return new ItemStack( CandelabraItem.getItem( state.get(MATERIAL), state.get(TYPE).getCount() ) );
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new CandelabraBlockEntity();
    }

}