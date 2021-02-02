package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.loot.BakedLootTable;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class UrnBlock extends SimpleBlock {

    public static VoxelShape COLLISION_SHAPE;
    public static VoxelShape OUTLINE_SHAPE;

    public UrnBlock() {
        super( RegUtil.settings( Material.WOOD, Sounds.URN, 0.1F, 6.0f, false ) );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return COLLISION_SHAPE;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return OUTLINE_SHAPE;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.URN;
    }

    static {
        COLLISION_SHAPE = Block.createCuboidShape( 3, 0, 3, 13, 13, 13 );
        OUTLINE_SHAPE = Utils.combine( new VoxelShape[]{
                Block.createCuboidShape( 4, 0, 4, 12, 1, 12 ),
                Block.createCuboidShape( 3, 1, 3, 13, 10, 13 ),
                Block.createCuboidShape( 5, 10, 5, 11, 12, 11 ),
                Block.createCuboidShape( 4, 12, 4, 12, 13, 12 ),
        }, BooleanBiFunction.OR );
    }

}
