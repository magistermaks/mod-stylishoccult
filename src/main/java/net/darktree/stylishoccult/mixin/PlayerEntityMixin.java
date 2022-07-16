package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.overlay.PlayerEntityDuck;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityDuck {

	@Unique
	private static final UUID MADNESS_SLOW_ID = UUID.fromString("1eaff00f-7207-1234-b3ff-d7a07ab1cdef");

	@Unique
	private float madness;

	@Inject(method="tickMovement", at=@At("HEAD"))
	public void stylish_tickMovement(CallbackInfo ci) {
		stylish_addMadness(-0.01f);

		float value = MathHelper.sin(madness * MathHelper.HALF_PI);

		EntityAttributeInstance entityAttributeInstance = ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		if (entityAttributeInstance != null) {
			if(entityAttributeInstance.getModifier(MADNESS_SLOW_ID) != null) {
				entityAttributeInstance.removeModifier(MADNESS_SLOW_ID);
			}

			float f = -0.10f * value;
			entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(MADNESS_SLOW_ID, "Madness slow", f, EntityAttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public void stylish_addMadness(float value) {
		madness = MathHelper.clamp(madness + value, 0f, 1f);
	}

	@Override
	public float stylish_getMadness() {
		return madness;
	}

}
