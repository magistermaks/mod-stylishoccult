package net.darktree.stylishoccult.entities.goal;

import net.darktree.stylishoccult.entities.CorruptedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;
import java.util.function.Predicate;

public class FollowNonCorruptedGoal extends BasicFollowGoal {

    public FollowNonCorruptedGoal(MobEntity mob, boolean checkVisibility) {
        this(mob, checkVisibility, false);
    }

    public FollowNonCorruptedGoal(MobEntity mob, boolean checkVisibility, boolean checkCanNavigate) {
        this(mob, 10, checkVisibility, checkCanNavigate, null);
    }

    public FollowNonCorruptedGoal(MobEntity mob, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, Predicate<LivingEntity> targetPredicate) {
        super(mob, checkVisibility, checkCanNavigate, reciprocalChance);

        this.setControls(EnumSet.of(Goal.Control.TARGET));
        this.targetPredicate = new NonCorruptedEntityPredicate()
                .setBaseMaxDistance(this.getFollowRange())
                .setPredicate(targetPredicate);
    }

    public static class NonCorruptedEntityPredicate extends TargetPredicate {

        public NonCorruptedEntityPredicate() {
            super(true);
        }

        @Override
        public boolean test( LivingEntity baseEntity, LivingEntity targetEntity ) {

            if( targetEntity instanceof CorruptedEntity) {
                return false;
            }

            return super.test(baseEntity, targetEntity);
        }

    }

}
