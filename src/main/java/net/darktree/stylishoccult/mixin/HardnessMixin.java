package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.MutableHardness;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class HardnessMixin {

    @Inject(at = @At("HEAD"), method = "getHardness(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F", cancellable = true)
    void getHardness(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        BlockState state = world.getBlockState(pos);

        if( state.getBlock() instanceof MutableHardness ) {
            MutableHardness hardness = (MutableHardness) state.getBlock();
            info.setReturnValue( hardness.getHardness(state, world, pos) );
        }
    }

}
