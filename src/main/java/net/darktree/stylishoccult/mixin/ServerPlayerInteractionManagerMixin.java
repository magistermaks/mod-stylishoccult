package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.ShiftUsable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    private GameMode gameMode;

    @Inject(at = @At("HEAD"), method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    public void interactBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if( gameMode != GameMode.SPECTATOR ) {

            BlockState state = world.getBlockState(hit.getBlockPos());
            boolean notEmpty = !player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty();
            boolean canceled = player.shouldCancelInteraction() && notEmpty;

            if( canceled && state.getBlock() instanceof ShiftUsable) {

                ActionResult result = ((ShiftUsable) state.getBlock()).shiftUse(state, world, hit.getBlockPos(), player, hand, hit);

                if( result.isAccepted() ) {
                    info.setReturnValue( ActionResult.SUCCESS );
                }

            }

        }
    }

}
