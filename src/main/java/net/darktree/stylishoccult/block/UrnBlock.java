package net.darktree.stylishoccult.block;

import net.darktree.interference.Voxels;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class UrnBlock extends SimpleBlock {

    private static final VoxelShape COLLISION_SHAPE = Voxels.box(3, 0, 3, 13, 13, 13).build();
    private static final VoxelShape OUTLINE_SHAPE = Voxels.box(4, 0, 4, 12, 1, 12).box(3, 1, 3, 13, 10, 13).box(5, 10, 5, 11, 12, 11).box(4, 12, 4, 12, 13, 12).build();

    public UrnBlock() {
        super( RegUtil.settings( Material.WOOD, Sounds.URN, 0.1F, 6.0f, false ) );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return OUTLINE_SHAPE;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.URN;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

}
