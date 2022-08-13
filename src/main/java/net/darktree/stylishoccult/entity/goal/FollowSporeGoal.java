package net.darktree.stylishoccult.entity.goal;

import net.darktree.stylishoccult.entity.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;
import java.util.function.Predicate;

public class FollowSporeGoal extends BasicFollowGoal {

	public FollowSporeGoal(MobEntity mob, boolean checkVisibility) {
		this(mob, checkVisibility, false);
	}

	public FollowSporeGoal(MobEntity mob, boolean checkVisibility, boolean checkCanNavigate) {
		this(mob, 10, checkVisibility, checkCanNavigate, null);
	}

	public FollowSporeGoal(MobEntity mob, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, Predicate<LivingEntity> targetPredicate) {
		super(mob, checkVisibility, checkCanNavigate, reciprocalChance);

		this.setControls(EnumSet.of(Goal.Control.TARGET));
		this.targetPredicate = new CorruptedEntityPredicate()
				.setBaseMaxDistance(this.getFollowRange())
				.setPredicate(targetPredicate);
	}

	public static class CorruptedEntityPredicate extends TargetPredicate {

		public CorruptedEntityPredicate() {
			super(true);
		}

		@Override
		public boolean test(LivingEntity baseEntity, LivingEntity target) {
			return super.test(baseEntity, target) && target.getType() == ModEntities.SPORE;
		}

	}

}
