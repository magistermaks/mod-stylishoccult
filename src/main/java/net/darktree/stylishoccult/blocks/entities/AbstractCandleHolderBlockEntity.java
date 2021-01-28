package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.CandelabraBlock;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.parts.AbstractCandleHolder;
import net.darktree.stylishoccult.parts.CandleStateInfo;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;

public abstract class AbstractCandleHolderBlockEntity<T extends AbstractCandleHolder> extends SimpleBlockEntity {

    protected T state;
    protected boolean lit;

    @Environment(EnvType.CLIENT)
    protected int toNextParticle = -1;

    public AbstractCandleHolderBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    public boolean interact(BlockPos pos, PlayerEntity player, Hand hand, boolean isSneaking, boolean apply) {

        ItemStack stack = player.getStackInHand(hand);

        // extinguish candle
        if( !isSneaking && stack.isEmpty() ) {
            CandleStateInfo candle = state.rayTrace( pos, player, 1, 8 );
            if( apply && candle != null && candle.lit ) {
                Network.CANDLE_PACKET.send(pos, candle.id, 1, hand);
            }
            return true;
        }

        // ignite candle
        if( !isSneaking && (stack.getItem() == net.minecraft.item.Items.FLINT_AND_STEEL) ) {
            CandleStateInfo candle = state.rayTrace( pos, player, 2, 8 );
            if( apply && candle != null && !candle.lit ) {
                toNextParticle = 0;
                Network.CANDLE_PACKET.send(pos, candle.id, 2, hand);
            }
            return true;
        }

        // take candle
        if( isSneaking && stack.isEmpty() ) {
            CandleStateInfo candle = state.rayTrace( pos, player, 1, 8 );
            if( apply && candle != null ) {
                Network.CANDLE_PACKET.send(pos, candle.id, 4, hand);
            }
            return true;
        }

        // place candle
        if( stack.getItem() == ModItems.CANDLE || stack.getItem() == ModItems.EXTINGUISHED_CANDLE ) {
            CandleStateInfo candle = state.rayTrace( pos, player, 0, 1 );
            if( apply && candle != null ) {
                if( stack.getItem() == ModItems.CANDLE ) {
                    toNextParticle = 0;
                }

                Network.CANDLE_PACKET.send(pos, candle.id, 3, hand);
            }
            return true;
        }

        return false;

    }

    public void applyAction(PlayerEntity player, int id, int op, boolean offhand){

        final CandleStateInfo candle = state.get(id);
        ItemStack hand = player.getStackInHand( offhand ? Hand.OFF_HAND : Hand.MAIN_HAND );

        if( world == null || candle == null ) {
            StylishOccult.LOGGER.error( "Invalid candle operation context!" );
            return;
        }

        switch( op ) {

            case 1: // extinguish candle
                state.extinguish(candle);
                if( isServer() ) {
                    candle.emitParticle(world, pos, ParticleTypes.SMOKE, 2);
                    world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.06F, world.random.nextFloat() * 0.4F + 2.0F);
                }
                update();
                break;

            case 2: // ignite candle
                if( hand.getItem() == net.minecraft.item.Items.FLINT_AND_STEEL ) {
                    state.ignite(candle);
                    if( isServer() ) {
                        world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    }
                    hand.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                    update();
                }
                break;

            case 3: // place candle
                if( hand.getItem() == ModItems.CANDLE || hand.getItem() == ModItems.EXTINGUISHED_CANDLE ) {
                    state.put( candle, hand, player.isCreative() );
                    if( isServer() ) {
                        world.playSound(null, pos, Sounds.CANDLE.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    }
                    update();
                }
                break;

            case 4: // take candle
                player.giveItemStack( state.drop( candle ) );
                update();
                break;

            default:
                StylishOccult.LOGGER.error( "Invalid candle operation ID!" );

        }

    }

    public VoxelShape getOutline() {
        return state.getOutline();
    }

    public ArrayList<ItemStack> dropAll() {
        return state.dropAll();
    }

    public void randomUpdate() {
        if( world != null && isServer() ){
            if( (world.random.nextInt( StylishOccult.SETTINGS.candleHolderLayerBurnoutRarity ) == 0) || world.hasRain(pos) ) {
                if( state.randomUpdate(world.random) ){
                    BlockState state = world.getBlockState(pos).with(Properties.LIT, this.state.isLit());
                    this.world.setBlockState(pos, state);
                    this.update();
                }
            }
        }
    }

    public void tick() {
        if( isClient() ) {
            if( toNextParticle -- <= 0 ) {
                toNextParticle = (toNextParticle <= -2 ? StylishOccult.RANDOM.nextInt( 40 ) : 40);
                state.emitParticles( this.pos, this.world, Particles.CANDLE_FLAME );
            }
        }else{
            if( lit != state.isLit() && world != null ) {
                world.setBlockState( this.pos, this.world.getBlockState( this.pos ).with(CandelabraBlock.LIT, state.isLit()) );
                lit = state.isLit();
            }
        }
    }
}
