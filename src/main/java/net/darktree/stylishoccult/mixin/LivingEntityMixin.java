package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method="onDeath", at=@At("TAIL"))
	public void onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		BlockPos pos = entity.getBlockPos();

		if(entity.world != null && pos != null && !entity.world.isClient) {
			OccultHelper.sacrifice(entity.world, pos, entity);
		}
	}

}
