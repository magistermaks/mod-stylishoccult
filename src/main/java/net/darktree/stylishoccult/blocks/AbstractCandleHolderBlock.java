package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.entities.AbstractCandleHolderBlockEntity;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.ShiftUsable;
import net.darktree.stylishoccult.utils.SimpleBlockWithEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public abstract class AbstractCandleHolderBlock extends SimpleBlockWithEntity implements ShiftUsable {

    public static final BooleanProperty LIT = Properties.LIT;
    public static final EnumProperty<CandleHolderMaterial> MATERIAL = EnumProperty.of("material", CandleHolderMaterial.class);

    protected AbstractCandleHolderBlock(Settings settings) {
        super( settings.luminance( state -> state.get(LIT) ? 15 : 0 ) );
    }

    @Deprecated
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( !player.abilities.allowModifyWorld ) {
            return ActionResult.PASS;
        }

        AbstractCandleHolderBlockEntity entity = BlockUtils.getEntity( AbstractCandleHolderBlockEntity.class, world, pos );
        if( entity != null ){
            if( entity.interact(pos, player, hand, player.isSneaking(), world.isClient) )  return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    public ActionResult shiftUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return onUse( state, world, pos, player, hand, hit );
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        AbstractCandleHolderBlockEntity blockEntity = BlockUtils.getEntity( AbstractCandleHolderBlockEntity.class, view, pos );
        if( blockEntity == null ) {
            return VoxelShapes.fullCube();
        }

        return blockEntity.getOutline();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( world.isClient ) {
            return;
        }
        BlockUtils.getEntity( AbstractCandleHolderBlockEntity.class, world, pos ).randomUpdate();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return getMainStack( state );
    }

    public ItemStack getMainStack(BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.CANDLE_CONTAINER;
    }

}
