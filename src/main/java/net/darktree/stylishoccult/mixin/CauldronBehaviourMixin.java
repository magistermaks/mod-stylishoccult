package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.blocks.BloodCauldronBehaviour;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviourMixin {

	@Inject(method="registerBucketBehavior", at=@At("HEAD"))
	private static void stylish_registerBucketBehavior(Map<Item, CauldronBehavior> behavior, CallbackInfo info) {
		BloodCauldronBehaviour.addBehaviourMap(behavior);
	}

}
