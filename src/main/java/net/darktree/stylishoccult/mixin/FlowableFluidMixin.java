package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.FluidReplaceable;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {

    @Inject(method="canFill", at=@At("HEAD"), cancellable=true)
    private void canFill(BlockView world, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if( world.getBlockState(pos).getBlock() instanceof FluidReplaceable ) {
            info.setReturnValue(true);
        }
    }

}
