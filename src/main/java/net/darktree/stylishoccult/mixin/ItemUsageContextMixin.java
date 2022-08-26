package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemUsageContext.class)
public abstract class ItemUsageContextMixin {

	@Shadow @Final private BlockHitResult hit;

	@Inject(method="getSide", at=@At("HEAD"), cancellable=true)
	public void stylish_getSide(CallbackInfoReturnable<Direction> info) {
		if (hit.getSide() == Directions.UNDECIDED) {
			info.setReturnValue(Direction.UP);
		}
	}

}
