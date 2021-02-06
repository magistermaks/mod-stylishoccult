package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.occult.api.FoliageFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.blocks.occult.api.ImpureBlock;
import net.darktree.stylishoccult.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class EyesBlock extends SimpleBlock implements ImpureBlock, FoliageFleshBlock {

    public static final IntProperty SIZE = IntProperty.of("size", 1, 3);

    // TODO: MAKE IT POP WHEN STEED ON (AND GIVE EFFECTS) - WART VARIANT ONLY

    public EyesBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ).noCollision().ticksRandomly() );
        setDefaultState( getDefaultState().with(SIZE, 1) );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int size = state.get(SIZE);

        if( size == 3 && RandUtils.getBool(40) && BlockUtils.countInArea(world, pos, EyesBlock.class, 3) < 3) {
            BlockPos target = pos.offset( RandUtils.getEnum( Direction.class ) );

            if( RandUtils.getBool(75) ) {
                target = target.offset( RandUtils.getEnum( Direction.class ) );
            }

            BlockState stateTarget = world.getBlockState(target);
            BlockState stateDown = world.getBlockState(target.down());

            if( stateDown.getBlock() instanceof FullFleshBlock && (stateTarget.isAir() || stateTarget.getMaterial().isReplaceable()) ) {
                world.setBlockState( target, getDefaultState().with( SIZE, RandUtils.rangeInt(1, 3) ) );
            }
        }

        //if( size != 3 && RandUtils.getBool(30 + size * 5) ) {
        //    world.setBlockState( pos, state.with(SIZE, size + 1) );
        //}
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SIZE);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).isAir();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // TODO: check item
        world.setBlockState( pos, state.cycle(SIZE) );
        return ActionResult.SUCCESS;
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return state.get(SIZE) + 2;
    }

}
