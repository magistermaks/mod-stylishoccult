package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.ChandelierBlockEntity;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.items.ChandelierItem;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class ChandelierBlock extends AbstractCandleHolderBlock {

    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape( 6.5, 15, 6.5, 9.5, 16, 9.5 );

    protected ChandelierBlock() {
        super( RegUtil.settings( Material.WOOD, BlockSoundGroup.WOOD, 0.1F, 1.0F, false )
                .ticksRandomly());

        this.setDefaultState( this.stateManager.getDefaultState()
                .with(LIT, false)
                .with(MATERIAL, CandleHolderMaterial.OAK));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, MATERIAL);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if( facing == Direction.UP && !this.canPlaceAt(state, world, pos) ) {
            onBreak((World) world, pos, state, null);
            return Blocks.AIR.getDefaultState();
        }else{
            return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Block.sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return BASE_SHAPE;
    }

    @Override
    public ItemStack getMainStack( BlockState state ) {
        return new ItemStack( ChandelierItem.getItem( state.get(MATERIAL) ) );
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ChandelierBlockEntity();
    }

}
