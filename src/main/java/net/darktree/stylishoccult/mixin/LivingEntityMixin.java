package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.ThinFleshBlock;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method="isClimbing", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/entity/LivingEntity;getBlockStateAtPos()Lnet/minecraft/block/BlockState;"), locals=LocalCapture.CAPTURE_FAILHARD, cancellable=true)
	public void isClimbing(CallbackInfoReturnable<Boolean> info, BlockPos pos, BlockState state) {
		if(state.getBlock() == ModBlocks.GROWTH) {
			if(ThinFleshBlock.hasSide(state)) {
				info.setReturnValue(true);
			}
		}
	}

	@Inject(method="onDeath", at=@At("TAIL"))
	public void onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		BlockPos pos = entity.getBlockPos();

		if(entity.world != null && pos != null && !entity.world.isClient) {
			OccultHelper.sacrifice(entity.world, pos, entity);
		}
	}

}
