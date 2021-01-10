package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.WallCandelabraBlockEntity;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.items.WallCandelabraItem;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class WallCandelabraBlock extends AbstractCandleHolderBlock {

    public static final DirectionProperty HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;

    protected WallCandelabraBlock() {
        super(RegUtil.settings( Material.WOOD, BlockSoundGroup.WOOD, 0.1F, 1.0F, false )
                .ticksRandomly());

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HORIZONTAL_FACING, Direction.NORTH)
                .with(MATERIAL, CandleHolderMaterial.OAK)
                .with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add( HORIZONTAL_FACING, MATERIAL, LIT );
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(HORIZONTAL_FACING).getOpposite();
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new WallCandelabraBlockEntity();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return VoxelShapes.empty();
    }

    @Override
    public ItemStack getMainStack( BlockState state ) {
        return new ItemStack( WallCandelabraItem.getItem( state.get(MATERIAL) ) );
    }

}
