package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.RegUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ExtinguishedCandleBlock extends CandleBlock {

    public ExtinguishedCandleBlock() {
        super( RegUtil.settings( Material.SUPPORTED, Sounds.CANDLE, 0.1F, 1.0f, false ) );
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL ) {
            world.setBlockState(pos, ModBlocks.CANDLE.getDefaultState().with( LAYERS, state.get( LAYERS ) ));
            world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            player.getStackInHand(hand).damage(1, player, (p) -> p.sendToolBreakStatus( hand ));
            addParticle( state, world, pos, Particles.CANDLE_FLAME, 0 );
            initScheduler(world, pos );
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
