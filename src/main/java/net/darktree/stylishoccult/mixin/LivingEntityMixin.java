package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.GrowthBlock;
import net.darktree.stylishoccult.duck.LivingEntityDuck;
import net.darktree.stylishoccult.entity.damage.ModDamageSource;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityDuck {

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Shadow
	public abstract float getHealth();

	@Unique
	private long lastShockTaken = 0;

	@Override
	public float stylish_applyShock(long tick, float damage) {
		if (tick - lastShockTaken > 20) {
			this.lastShockTaken = tick;
			damage = Math.min(this.getHealth(), damage);
			this.damage(ModDamageSource.SHOCK, damage);
			return damage;
		}

		return 0;
	}

	@Inject(method="isClimbing", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/entity/LivingEntity;getBlockStateAtPos()Lnet/minecraft/block/BlockState;"), locals=LocalCapture.CAPTURE_FAILHARD, cancellable=true)
	public void stylish_isClimbing(CallbackInfoReturnable<Boolean> info, BlockPos pos, BlockState state) {
		if (state.getBlock() == ModBlocks.GROWTH) {
			if (GrowthBlock.hasSide(state)) {
				info.setReturnValue(true);
			}
		}
	}

	@Inject(method="onDeath", at=@At("TAIL"))
	public void stylish_onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		BlockPos pos = entity.getBlockPos();

		if (entity.world != null && pos != null && !entity.world.isClient) {
			OccultHelper.sacrifice(entity.world, pos, entity);
		}
	}

}
