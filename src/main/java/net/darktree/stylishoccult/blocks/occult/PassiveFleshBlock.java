package net.darktree.stylishoccult.blocks.occult;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.items.BottleItem;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.BakedLootTable;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PassiveFleshBlock extends SimpleBlock implements ImpureBlock {

    public static final BooleanProperty BLOODY = BooleanProperty.of("bloody");
    public static final VoxelShape SMALL_CUBE = Utils.box(1, 1, 1, 15, 15, 15);

    public PassiveFleshBlock() {
        super( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, true ).slipperiness(0.8f) );
        setDefaultState( getDefaultState().with(BLOODY, false) );
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BLOODY);
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SMALL_CUBE;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.PASSIVE_FLESH;
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
    public void cleanse(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1, 1);

        if( state.get(BLOODY) ) {
            world.setBlockState(pos, state.with(BLOODY, false));
        }else{
            // TODO REPLACE WITH REAL DEAD FLESH
            world.setBlockState(pos, ModBlocks.ARCANE_ASH.getDefaultState());
        }
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 5 + (state.get(BLOODY) ? 4 : 0);
    }

}
