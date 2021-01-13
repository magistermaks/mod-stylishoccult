package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.PedestalBlockEntity;
import net.darktree.stylishoccult.loot.BakedLootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PedestalBlock extends SimpleBlock implements BlockEntityProvider {

    public static final VoxelShape SHAPE = Utils.combine(
            new VoxelShape[]{Utils.box(4, 0, 4, 12, 11, 12), Utils.box(1, 11, 1, 15, 15, 15)}, BooleanBiFunction.OR );

    public PedestalBlock() {
        super( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 1.5f, 6.0f, false ) );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return SHAPE;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView view, BlockPos pos, Direction facing) {
        PedestalBlockEntity entity = BlockUtils.getEntity( PedestalBlockEntity.class, view, pos );
        return (entity != null) ? entity.getPower() : 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView view, BlockPos pos, Direction facing) {
        return facing == Direction.UP ? state.getWeakRedstonePower(view, pos, facing) : 0;
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new PedestalBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PedestalBlockEntity entity = BlockUtils.getEntity( PedestalBlockEntity.class, world, pos );
        return entity != null ? entity.interact( player, hand ) : ActionResult.PASS;
    }

    @Override
    public BakedLootTable getInternalLootTableId() {
        return LootTables.PEDESTAL;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        PedestalBlockEntity entity = BlockUtils.getEntity( PedestalBlockEntity.class, world, pos );
        if( entity != null ) {
            ItemStack stack = entity.drop();

            if( !world.isClient && !stack.isEmpty() ) {
                double d = pos.getX() + world.random.nextFloat() * 0.5F + 0.3D;
                double e = pos.getY() + world.random.nextFloat() * 0.5F + 0.3D;
                double g = pos.getZ() + world.random.nextFloat() * 0.5F + 0.3D;

                ItemEntity itemEntity = new ItemEntity(world, d, e, g, stack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }
    }

}
