package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.BuildingBlock;
import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class EyeBlock extends BuildingBlock implements ImpureBlock, FoliageFleshBlock {

    private static final VoxelShape SHAPE = Utils.join(
            Utils.box( 1, 1, 0, 15, 15, 16 ),
            Utils.box( 0, 1, 1, 16, 15, 15 ),
            Utils.box( 1, 0, 1, 15, 16, 15 )
    );

    public EyeBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 1.0F, 1.0F, true ) );
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return (world.getBlockState(pos.up()).getBlock() instanceof TentacleBlock) || (world.getBlockState(pos.down()).getBlock() instanceof TentacleBlock);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 8;
    }

}
