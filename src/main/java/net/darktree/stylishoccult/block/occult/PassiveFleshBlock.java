package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.item.BottleItem;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class PassiveFleshBlock extends FullFleshBlock implements ImpureBlock {

    public static final BooleanProperty BLOODY = BooleanProperty.of("bloody");
    public static final VoxelShape SMALL_CUBE = Utils.shape(1, 1, 1, 15, 15, 15);

    public PassiveFleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, true ).ticksRandomly().slipperiness(0.8f) );
        setDefaultState( getDefaultState().with(BLOODY, false) );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (StylishOccult.SETTING.infinite_flesh_growth && RandUtils.getBool(5f, random)) {
            world.setBlockState(pos, ModBlocks.DEFAULT_FLESH.getDefaultState());
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BLOODY);
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SMALL_CUBE;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.GENERIC_FLESH;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if( state.get( BLOODY ) ) {
            ItemStack stack = player.getStackInHand(hand);

            if( ((BottleItem) ModItems.BLOOD_BOTTLE).fill( stack, world, player, hand ) ) {
                world.setBlockState( pos, state.with( BLOODY, false ) );
                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);

        if( state.get(BLOODY) ) {
            world.setBlockState(pos, state.with(BLOODY, false));
        }else{
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 5 + (state.get(BLOODY) ? 3 : 0);
    }

}
