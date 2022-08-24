package net.darktree.stylishoccult.effect;

import net.darktree.stylishoccult.entity.damage.ModDamageSource;
import net.darktree.stylishoccult.mixin.StatusEffectInstanceAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CorruptedBloodEffect extends SimpleStatusEffect {

	protected CorruptedBloodEffect() {
		super(StatusEffectCategory.HARMFUL, false, 0x801a00);
	}

	@Override
	public void onUpdate(LivingEntity entity, int amplifier) {
		if (entity.world.isClient) {
			return;
		}

		int a = Math.abs(amplifier);

		if (entity.world.random.nextInt(20) == 0) {
			BlockPos pos = entity.getBlockPos();
			float r = 4 + a / 2.0f;

			Box box = new Box(
					pos.getX() - r,
					pos.getY() - r + 1,
					pos.getZ() - r,
					pos.getX() + r,
					pos.getY() + r - 1,
					pos.getZ() + r
			);

			List<LivingEntity> list = entity.world.getNonSpectatingEntities(LivingEntity.class, box);
			int target = MathHelper.clamp( (entity.getHealth() <= 1.0f || entity.world.random.nextInt(60) == 0) ? a + 1 : a, 0, 6 );
			int time = target * 160;

			for (LivingEntity livingEntity : list) {
				applyToEntity(livingEntity, target, time);
			}
		}

		StatusEffectInstanceAccessor accessor = (StatusEffectInstanceAccessor) entity.getStatusEffect(this);
		if (accessor != null) {
			accessor.stylish_setStoredParticlesFlag(amplifier > 0);
		}

		if (amplifier > 0 && entity.world.getTime() % 4 == 0) {
			if(entity.getHealth() > a / 3.0f || a >= 6) {
				entity.damage(ModDamageSource.CORRUPTION, a / 4.0f);
			}
		}
	}

	private void applyToEntity(@NotNull LivingEntity entity, int amplifier, int bonusTime ) {
		if (entity.world.random.nextInt(10) == 0) {
			if (!entity.hasStatusEffect(this) || (entity.world.random.nextInt(20) == 0)) {
				int time = 800 + entity.world.random.nextInt(300) + bonusTime;
				entity.addStatusEffect(new StatusEffectInstance(this, time, amplifier));
			}
		}
	}

}
