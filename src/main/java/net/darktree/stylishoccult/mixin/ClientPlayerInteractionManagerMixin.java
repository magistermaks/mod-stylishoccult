package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.blocks.AbstractCandleHolderBlock;
import net.darktree.stylishoccult.items.CandleItem;
import net.darktree.stylishoccult.utils.ShiftUsable;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    private GameMode gameMode;

    @Final
    @Shadow
    private ClientPlayNetworkHandler networkHandler;

    @Inject(at = @At("HEAD"), method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    public void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if( gameMode != GameMode.SPECTATOR ) {

            BlockState state = world.getBlockState(hit.getBlockPos());
            boolean notEmpty = !player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty();
            boolean canceled = player.shouldCancelInteraction() && notEmpty;

            if( canceled && state.getBlock() instanceof ShiftUsable ) {

                ActionResult result = ((ShiftUsable) state.getBlock()).shiftUse(state, world, hit.getBlockPos(), player, hand, hit);

                if( result.isAccepted() ) {
                    networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hit));
                    info.setReturnValue( result );
                }

            }

        }
    }

}
